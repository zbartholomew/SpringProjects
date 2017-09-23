'use strict';

const React = require('react');  // is the main library from Facebook for building this app.
const ReactDOM = require('react-dom');
const when = require('when');
// Client is custom code that configures rest.js to include support for HAL, URI Templates, and other things.
// It also sets the default Accept request header to application/hal+json
const client = require('./client');
const follow = require('./follow');
const stompClient = require('./websocket-listener');

const root = '/api';

/**
 *  An array of employees is fetched from the Spring Data REST backend and stored in this component’s state data.
 */
class App extends React.Component { // create a React component

    constructor(props) {
        super(props);
        this.state = {employees: [], attributes: [], page: 1, pageSize: 2, links: {}};
        this.updatePageSize = this.updatePageSize.bind(this);
        this.onCreate = this.onCreate.bind(this);
        this.onDelete = this.onDelete.bind(this);
        this.onNavigate = this.onNavigate.bind(this);
        this.onUpdate = this.onUpdate.bind(this);
        this.refreshCurrentPage = this.refreshCurrentPage.bind(this);
        this.refreshAndGoToLastPage = this.refreshAndGoToLastPage.bind(this);
    }

    /**
     * Fetch the collection and then use the URIs to retrieve each individual resource.
     * @param pageSize - number of employees you want to display
     */
    loadFromServer(pageSize) {
        follow(client, root, [
                {rel: 'employees', params: {size: pageSize}}]
            // creates a call to fetch JSON Schema data.
        ).then(employeeCollection => {
            return client({
                method: 'GET',
                path: employeeCollection.entity._links.profile.href,
                headers: {'Accept': 'application/schema+json'}
                //  store the metadata and navigational links in the <App/>
            }).then(schema => {
                this.schema = schema.entity;
                this.links = employeeCollection.entity._links;
                return employeeCollection;
            });
            // Converts the collection of employees into an array of GET promises to fetch each individual resource.
            // This is what we will need to fetch an ETag header for each employee.
        }).then(employeeCollection => {
			this.page = employeeCollection.entity.page;
            return employeeCollection.entity._embedded.employees.map(employee =>
                client({
                    method: 'GET',
                    path: employee._links.self.href
                })
            );
            // Takes the array of GET promises and merges them into a single promise with when.all(),
            // resolved when all the GET promises are resolved.
        }).then(employeePromises => {
            return when.all(employeePromises);
            // The UI state is updated using this combination of data.
        }).done(employees => {
            this.setState({
				page: this.page,
                employees: employees,
                attributes: Object.keys(this.schema.properties),
                pageSize: pageSize,
                links: this.links
            });
        });
    }

    /**
     * Callback - creates new employee and adds to end of dataset
     * @param newEmployee - employee to be added
     */
    //New records are typically added to the end of the dataset.
    // Since we are looking at a certain page, it’s logical to expect the
    // new employee record to not be on the current page.
    // To handle this, we need to fetch a new batch of data with the same page size applied.
    // That promise is returned for the final clause inside done()
    onCreate(newEmployee) {
        follow(client, root, ['employees']).done(response => {
            client({
                method: 'POST',
                path: response.entity._links.self.href,
                entity: newEmployee,
                headers: {'Content-Type': 'application/json'}
            })
        })
    }

    /**
     * Delete employee from database
     * @param employee
     */
    onDelete(employee) {
		client({method: 'DELETE', path: employee.entity._links.self.href});
    }

    /**
     * Navigates to specified uri
     * @param navUri - uri you want to navigate to
     */
    onNavigate(navUri) {
        client({
            method: 'GET',
            path: navUri
        }).then(employeeCollection => {
            this.links = employeeCollection.entity._links;
			this.page = employeeCollection.entity.page;

            return employeeCollection.entity._embedded.employees.map(employee =>
                client({
                    method: 'GET',
                    path: employee._links.self.href
                })
            );
        }).then(employeePromises => {
            return when.all(employeePromises);
        }).done(employees => {
            this.setState({
				page: this.page,
                employees: employees,
                attributes: Object.keys(this.schema.properties),
                pageSize: this.state.pageSize,
                links: this.links
            });
        });
    }

    /**
     * Updates employee
     * @param employee
     * @param updatedEmployee
     */
    onUpdate(employee, updatedEmployee) {
        client({
            method: 'PUT',
            path: employee.entity._links.self.href,
            entity: updatedEmployee,
            headers: {
                'Content-Type': 'application/json',
                // If the incoming If-Match value doesn’t match the data store’s version value,
                // Spring Data REST will fail with an HTTP 412 Precondition Failed.
                'If-Match': employee.headers.Etag
            }
        }).done(response => {
            /* Let the websocket handler update the state */
        }, response => {
            if (response.status.code === 412) {
                alert('DENIED: Unable to update ' +
                    employee.entity._links.self.href + '. Your copy is stale.');
            }
        });
    }

    /**
     * Retrieve page size from server
     * @param pageSize
     */
    updatePageSize(pageSize) {
        if (pageSize !== this.state.pageSize) {
            this.loadFromServer(pageSize);
        }
    }

    // callback used to update UI state when WebSocket event is received
    refreshAndGoToLastPage(message) {
        follow(client, root, [{
            rel: 'employees',
            params: {size: this.state.pageSize}
        }]).done(response => {
            if (response.entity._links.last !== undefined) {
                this.onNavigate(response.entity._links.last.href);
            } else {
                this.onNavigate(response.entity._links.self.href);
            }
        })
    }

    // callback used to update UI state when WebSocket event is received
    refreshCurrentPage(message) {
        follow(client, root, [{
            rel: 'employees',
            params: {
                size: this.state.pageSize,
                page: this.state.page.number
            }
        }]).then(employeeCollection => {
            this.links = employeeCollection.entity._links;
            this.page = employeeCollection.entity.page;

            return employeeCollection.entity._embedded.employees.map(employee => {
                return client({
                    method: 'GET',
                    path: employee._links.self.href
                })
            });
        }).then(employeePromises => {
            return when.all(employeePromises);
        }).then(employees => {
            this.setState({
                page: this.page,
                employees: employees,
                attributes: Object.keys(this.schema.properties),
                pageSize: this.state.pageSize,
                links: this.links
            });
        });
    }

    // Is the API invoked after React renders a component in the DOM.
    // Register WebSocket events
    componentDidMount() {
        this.loadFromServer(this.state.pageSize);
        stompClient.register([
            {route: '/topic/newEmployee', callback: this.refreshAndGoToLastPage},
            {route: '/topic/updateEmployee', callback: this.refreshCurrentPage},
            {route: '/topic/deleteEmployee', callback: this.refreshCurrentPage}
        ]);
    }

    // Is the API to "draw" the component on the screen.
    render() {
        return (
            <div>
                <CreateDialog attributes={this.state.attributes} onCreate={this.onCreate}/>
				<EmployeeList page={this.state.page}
							  employees={this.state.employees}
                              links={this.state.links}
                              pageSize={this.state.pageSize}
                              attributes={this.state.attributes}
                              onNavigate={this.onNavigate}
                              onUpdate={this.onUpdate}
                              onDelete={this.onDelete}
                              updatePageSize={this.updatePageSize}/>
            </div>
        )
    }
}

/**
 * Dialog component
 * Using the Metedata, we are adding extra controls to the UI
 */
// Creating a new React Component <CreateDialog>
class CreateDialog extends React.Component {

    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    /**
     * Stops the event from bubbling further up the hierarchy.
     * Uses the same JSON Schema attribute property to find each <input> using React.findDOMNode(this.refs[attribute])
     * @param e - event
     */
    handleSubmit(e) {
        e.preventDefault();
        let newEmployee = {};
        this.props.attributes.forEach(attribute => {
            newEmployee[attribute] = ReactDOM.findDOMNode(this.refs[attribute]).value.trim();
        });
        this.props.onCreate(newEmployee);

        // clear out the dialog's inputs
        this.props.attributes.forEach(attribute => {
            ReactDOM.findDOMNode(this.refs[attribute]).value = '';
        });

        // Navigate away from the dialog to hide it.
        window.location = "#";
    }

    render() {
        const inputs = this.props.attributes.map(attribute =>
            // Converts the array of JSON Schema attributes into an array of HTML inputs,
            // wrapped in paragraph elements for styling.
            <p key={attribute}>
                <input type="text" placeholder={attribute} ref={attribute} className="field"/>
            </p>
        );

        return (
            <div>
                <a href="#createEmployee">Create</a>

                <div id="createEmployee" className="modalDialog">
                    <div>
                        <a href="#" title="Close" className="close">X</a>

                        <h2>Create new employee</h2>

                        <form>
                            {inputs}
                            <button onClick={this.handleSubmit}>Create</button>
                        </form>
                    </div>
                </div>
            </div>
        )
    }
}

/**
 * Allows the editting of existing employee records
 */
class UpdateDialog extends React.Component {

    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    // submit button is linked here
    handleSubmit(e) {
        e.preventDefault();
        let updatedEmployee = {};
        this.props.attributes.forEach(attribute => {
            updatedEmployee[attribute] = ReactDOM.findDOMNode(this.refs[attribute]).value.trim();
        });
        this.props.onUpdate(this.props.employee, updatedEmployee);
        window.location = "#";
    }

    render() {
        const inputs = this.props.attributes.map(attribute =>
            // Similar to the render() in CreateDialog
            // one difference: the fields are loaded with this.props.employee
            <p key={this.props.employee.entity[attribute]}>
                <input type="text" placeholder={attribute}
                       defaultValue={this.props.employee.entity[attribute]}
                       ref={attribute} className="field"/>
            </p>
        );

        const dialogId = "updateEmployee-" + this.props.employee.entity._links.self.href;

        return (
			<div>
                <a href={"#" + dialogId}>Update</a>
                <div id={dialogId} className="modalDialog">
                    <div>
                        <a href="#" title="Close" className="close">X</a>

                        <h2>Update an employee</h2>

                        <form>
                            {inputs}
                            <button onClick={this.handleSubmit}>Update</button>
                        </form>
                    </div>
                </div>
            </div>
        )
    }
}

/**
 * List of employee component
 */
class EmployeeList extends React.Component {

    constructor(props) {
        super(props);
        this.handleNavFirst = this.handleNavFirst.bind(this);
        this.handleNavPrev = this.handleNavPrev.bind(this);
        this.handleNavNext = this.handleNavNext.bind(this);
        this.handleNavLast = this.handleNavLast.bind(this);
        this.handleInput = this.handleInput.bind(this);
    }

    handleInput(e) {
        e.preventDefault();
        let pageSize = ReactDOM.findDOMNode(this.refs.pageSize).value;
        if (/^[0-9]+$/.test(pageSize)) {
            this.props.updatePageSize(pageSize);
        } else {
            ReactDOM.findDOMNode(this.refs.pageSize).value =
                pageSize.substring(0, pageSize.length - 1);
        }
    }

    /*-------------- Navigation methods ---------------- */
    handleNavFirst(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.first.href);
    }

    handleNavPrev(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.prev.href);
    }

    handleNavNext(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.next.href);
    }

    handleNavLast(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.last.href);
    }

    /* -------------- Navigation methods ---------------- */

    render() {
		const pageInfo = this.props.page.hasOwnProperty("number") ?
			<h3>Employees - Page {this.props.page.number + 1} of {this.props.page.totalPages}</h3> : null;

		const employees = this.props.employees.map(employee =>
            <Employee key={employee.entity._links.self.href}
                      employee={employee}
                      attributes={this.props.attributes}
                      onUpdate={this.props.onUpdate}
                      onDelete={this.props.onDelete}/>
        );

        let navLinks = [];
        if ("first" in this.props.links) {
            navLinks.push(<button key="first" onClick={this.handleNavFirst}>&lt;&lt;</button>);
        }
        if ("prev" in this.props.links) {
            navLinks.push(<button key="prev" onClick={this.handleNavPrev}>&lt;</button>);
        }
        if ("next" in this.props.links) {
            navLinks.push(<button key="next" onClick={this.handleNavNext}>&gt;</button>);
        }
        if ("last" in this.props.links) {
            navLinks.push(<button key="last" onClick={this.handleNavLast}>&gt;&gt;</button>);
        }

        return (
            <div>
				{pageInfo}
                <input ref="pageSize" defaultValue={this.props.pageSize} onInput={this.handleInput}/>
                <table>
                    <tbody>
                    <tr>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Description</th>
                        <th></th>
                        <th></th>
                    </tr>
                    {employees}
                    </tbody>
                </table>
                <div>
                    {navLinks}
                </div>
            </div>
        )
    }
}

/**
 * Employee Component
 */
class Employee extends React.Component {

    constructor(props) {
        super(props);
        this.handleDelete = this.handleDelete.bind(this);
    }

    handleDelete() {
        this.props.onDelete(this.props.employee);
    }

    render() {
        return (
            <tr>
                <td>{this.props.employee.entity.firstName}</td>
                <td>{this.props.employee.entity.lastName}</td>
                <td>{this.props.employee.entity.description}</td>
                <td>
                    <UpdateDialog employee={this.props.employee}
                                  attributes={this.props.attributes}
                                  onUpdate={this.props.onUpdate}/>
                </td>
                <td>
                    <button onClick={this.handleDelete}>Delete</button>
                </td>
            </tr>
		)
    }
}

ReactDOM.render(
    <App/>,
    document.getElementById('react')
);