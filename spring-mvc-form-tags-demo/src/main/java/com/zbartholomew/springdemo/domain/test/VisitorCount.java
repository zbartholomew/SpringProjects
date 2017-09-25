package com.zbartholomew.springdemo.domain.test;

import java.io.Serializable;

public class VisitorCount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7401039734834918383L;

	private int count;

	public VisitorCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
