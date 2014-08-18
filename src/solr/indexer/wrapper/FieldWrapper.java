package com.gs.cvoud.solr.indexer.wrapper;

import com.gs.cvoud.solr.annotation.OriginalField;

/**
 * 添加到solr的域的包装类
 * 
 * @author boyce
 * @version 2013-9-8
 */
public class FieldWrapper {
	private OriginalField originalField;
	private String fieldValue;

	public FieldWrapper(OriginalField originalField, String fieldValue) {
		this.fieldValue = fieldValue;
		this.originalField = originalField;
	}

	public OriginalField getOriginalField() {
		return originalField;
	}

	public void setOriginalField(OriginalField originalField) {
		this.originalField = originalField;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

}
