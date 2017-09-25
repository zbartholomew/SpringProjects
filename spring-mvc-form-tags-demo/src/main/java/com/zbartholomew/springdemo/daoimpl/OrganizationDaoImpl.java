package com.zbartholomew.springdemo.daoimpl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.zbartholomew.springdemo.dao.OrganizationDao;
import com.zbartholomew.springdemo.domain.Organization;

@Repository
public class OrganizationDaoImpl implements OrganizationDao {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	@Autowired
	public void setDataSource(DataSource ds) {
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ds);
	}

	@Override
	public boolean create(Organization org) {
		SqlParameterSource beansParams = new BeanPropertySqlParameterSource(org);
		String sqlQuery = "INSERT INTO organization (company_name, year_of_incorporation, postal_code, employee_count, slogan) "
				+ "VALUES(:companyName, :yearOfIncorporation, :postalCode, :employeeCount, :slogan)";

		return namedParameterJdbcTemplate.update(sqlQuery, beansParams) == 1;

		// Object[] args = new Object[] { org.getCompanyName(),
		// org.getYearOfIncorporation(), org.getPostalCode(),
		// org.getEmployeeCount(), org.getSlogan() };
		//
		//// Expecting a result of 1 since we are only creating one at a time
		// return namedParameterJdbcTemplate.update(sqlQuery, args) == 1;
	}

	@Override
	public Organization getOrganization(Integer id) {
		SqlParameterSource params = new MapSqlParameterSource("id", id);

		String sqlQuery = "SELECT id, company_name, year_of_incorporation, postal_code, employee_count, slogan "
				+ "FROM organization WHERE id = :id";

		Organization org = namedParameterJdbcTemplate.queryForObject(sqlQuery, params, new OrganizationRowMapper());
		// Object[] args = new Object[] { id };
		return org;
	}

	@Override
	public List<Organization> getAllOrganizations() {
		String sqlQuery = "SELECT * FROM organization";
		List<Organization> orgList = namedParameterJdbcTemplate.query(sqlQuery, new OrganizationRowMapper());

		return orgList;
	}

	@Override
	public boolean delete(Organization org) {
		SqlParameterSource beanParams = new BeanPropertySqlParameterSource(org);
		String sqlQuery = "DELETE FROM organization WHERE id = :id";
		// Object[] args = new Object[] { org.getId() };
		return namedParameterJdbcTemplate.update(sqlQuery, beanParams) == 1;
	}

	@Override
	public boolean update(Organization org) {
		SqlParameterSource beanParams = new BeanPropertySqlParameterSource(org);
		String sqlQuery = "UPDATE organization SET slogan = :slogan WHERE id = :id";
		// Object[] args = new Object[] { org.getSlogan(), org.getId() };
		return namedParameterJdbcTemplate.update(sqlQuery, beanParams) == 1;
	}

	@Override
	public void cleanup() {
		String sqlQuery = "TRUNCATE TABLE organization ";
		namedParameterJdbcTemplate.getJdbcOperations().execute(sqlQuery);
	}

}
