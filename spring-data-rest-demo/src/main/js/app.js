'use strict';

const React = require('react');  // is the main library from Facebook for building this app.
const ReactDOM = require('react-dom');
// Client is custom code that configures rest.js to include support for HAL, URI Templates, and other things.
// It also sets the default Accept request header to application/hal+json
const client = require('./client');

/**
 *  An array of employees is fetched from the Spring Data REST backend and stored in this component’s state data.
 */
class App extends React.Component { // create a React component

    constructor(props) {
        super(props);
        this.state = {employees: []};
    }

    // Is the API invoked after React renders a component in the DOM.
    componentDidMount() {
        client({method: 'GET', path: '/api/employees'}).done(response => {
            this.setState({employees: response.entity._embedded.employees});
        });
    }

    // Is the API to "draw" the component on the screen.
    render() {
        return (
            <EmployeeList employees={this.state.employees}/>
        )
    }
}

class EmployeeList extends React.Component{
    render() {
        const employees = this.props.employees.map(employee =>
            <Employee key={employee._links.self.href} employee={employee}/>
        );
        return (
            <table>
                <tbody>
                <tr>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Description</th>
                </tr>
                {employees}
                </tbody>
            </table>
        )
    }
}

class Employee extends React.Component{
    render() {
        return (
            <tr>
                <td>{this.props.employee.firstName}</td>
                <td>{this.props.employee.lastName}</td>
                <td>{this.props.employee.description}</td>
            </tr>
        )
    }
}

ReactDOM.render(
    <App />,
    document.getElementById('react')
)