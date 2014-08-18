package com.gs.cvoud.solr.search.response;

import java.io.Serializable;

/**
 * solr返回的分组查询条目的Java对象描述
 * @author boyce
 * @version 2014-2-21
 */
public class FilterItem implements Serializable {

	private static final long serialVersionUID = 2722219983772281629L;
	
	public FilterItem(String key, int value) {
		super();
		this.key = key;
		this.value = value;
	}

	/**
	 * item名称，指分组item的名称，如：通信，20-25岁等等
	 */
	private String key;
	
	/**
	 * item的值，指当前分组item的结果集个数
	 */
	private int value;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getKey() {
		return key;
	}

	public int getValue() {
		return value;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	
}
