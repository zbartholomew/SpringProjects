package com.zbartholomew.springdemo.domain.test;

public class OrganizationRegistration {

	private String orgName;
	private String country;
	private String turnover;
	private String type;
	private String serviceLength;
	private boolean registeredPreviously = true;
	public String like;
	private String[] optionalServices;
	private String[] premiumServices;
	private String overseaOperations;
	private String employeeStrength;

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTurnover() {
		return turnover;
	}

	public void setTurnover(String turnover) {
		this.turnover = turnover;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getServiceLength() {
		return serviceLength;
	}

	public void setServiceLength(String serviceLength) {
		this.serviceLength = serviceLength;
	}

	public boolean isRegisteredPreviously() {
		return registeredPreviously;
	}

	public void setRegisteredPreviously(boolean registeredPreviously) {
		this.registeredPreviously = registeredPreviously;
	}

	public String getLike() {
		return like;
	}

	public void setLike(String like) {
		this.like = like;
	}

	public String[] getOptionalServices() {
		return optionalServices;
	}

	public void setOptionalServices(String[] optionalServices) {
		this.optionalServices = optionalServices;
	}

	public String[] getPremiumServices() {
		return premiumServices;
	}

	public void setPremiumServices(String[] premiumServices) {
		this.premiumServices = premiumServices;
	}

	public String getOverseaOperations() {
		return overseaOperations;
	}

	public void setOverseaOperations(String overseaOperations) {
		this.overseaOperations = overseaOperations;
	}

	public String getEmployeeStrength() {
		return employeeStrength;
	}

	public void setEmployeeStrength(String employeeStrength) {
		this.employeeStrength = employeeStrength;
	}
}
