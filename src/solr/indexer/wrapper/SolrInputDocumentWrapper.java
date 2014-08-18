package com.gs.cvoud.solr.indexer.wrapper;

import org.apache.solr.common.SolrInputDocument;

import com.gs.cvoud.solr.annotation.CopyField;
import com.gs.cvoud.solr.annotation.OriginalField;
import com.gs.cvoud.util.ObjectUtils;
import com.gs.cvoud.util.StringUtils;

/**
 * SolrInputDocument 的包装类
 * 
 * @author boyce
 * @version 2013-9-8
 */
public class SolrInputDocumentWrapper {
	private SolrInputDocument solrInputDocument;

	public SolrInputDocumentWrapper(SolrInputDocument solrInputDocument) {
		if (ObjectUtils.isNull(solrInputDocument)) {
			throw new NullPointerException("Constract a SolrInputDocument error, any argument cannot be null.");
		}
		this.solrInputDocument = solrInputDocument;
	}

	/**
	 * 添加一个字符串到指定域中，默认添加到builder中构造全局索引
	 */
	public void addStringField(final OriginalField field, final String fieldValue) {
		this.addWrapperField(new FieldWrapper(field, fieldValue));
	}

	/**
	 * 添加一个字符串到指定域中
	 */
	public void addWrapperField(final FieldWrapper fieldWrapper) {
		if (ObjectUtils.isNull(fieldWrapper) || ObjectUtils.isNull(fieldWrapper.getOriginalField())) {
			return;
		}

		OriginalField originalField = fieldWrapper.getOriginalField();
		String name = originalField.getName();
		String value = fieldWrapper.getFieldValue();
		Object nonValue = originalField.getNonValue();

		CopyField copyField = originalField.getCopyField();

		if (StringUtils.isNotEmpty(value)) {
			solrInputDocument.addField(name, value);
			if (ObjectUtils.isNotNull(copyField))
				solrInputDocument.addField(copyField.getName(), value);

		} else if (ObjectUtils.isNotNull(nonValue) && ObjectUtils.isNotNull(copyField)) {
			if (!solrInputDocument.containsKey(copyField.getName()) || !solrInputDocument.containsValue(nonValue)) {
				solrInputDocument.addField(copyField.getName(), nonValue);
			}
		}
	}

	/**
	 * 添加一个Object到指定域中
	 */
	public void addObjectAsStringField(final OriginalField field, final Object fieldValue) {
		if (ObjectUtils.isNull(fieldValue)) {
			return;
		}
		this.addStringField(field, fieldValue.toString());
	}

	/**
	 * 添加一个Object到指定域中
	 */
	public void addObjectField(final OriginalField field, final Object fieldValue) {
		if (StringUtils.isNotEmpty(field.getName())) {
			if (ObjectUtils.isNotNull(fieldValue)) {
				solrInputDocument.addField(field.getName(), fieldValue);
			} else if (ObjectUtils.isNotNull(field.getNonValue())) {
				solrInputDocument.addField(field.getName(), field.getNonValue());
			}
		}
	}

	/**
	 * 
	 * 将所有已添加到域值文本添加到一个总的域中，便于全文检索
	 * 
	 * 返回被包装的SolrInputDocument
	 */
	public SolrInputDocument getSolrInputDocument() {
		return this.solrInputDocument;
	}

}
