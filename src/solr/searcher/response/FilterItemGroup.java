package com.gs.cvoud.solr.search.response;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.gs.cvoud.solr.annotation.CopyField;
import com.gs.cvoud.solr.annotation.Field;
import com.gs.cvoud.solr.annotation.OriginalField;

/**
 * 一个分组查询条目组的描述，多个同类分组查询条目的集合
 * @author boyce
 * @version 2014-2-21
 */
public class FilterItemGroup implements Serializable {

	private static final long serialVersionUID = -1264910861316885731L;
	
	/**
	 * 每一个 Field 对应的中文Item名称
	 */
	private static final Map<Field, Name> KEY_NAME = new HashMap<Field, Name>() {
		private static final long serialVersionUID = 3661336772562602486L;
		{
			put(CopyField.INDUSTRY_COPY, new Name("行业", 8));
			put(CopyField.FUNCTION_COPY, new Name("职能", 7));
			put(CopyField.PROVINCE_COPY, new Name("地区", 6));
			put(CopyField.DEGREE_COPY, new Name("学历", 5));
			put(CopyField.TAGS_COPY, new Name("标签", 4));
			put(OriginalField.BIRTH_YEAR, new Name("年龄", 3));
			put(OriginalField.GRADUATE_YEAR, new Name("工龄", 2));
			put(OriginalField.SALARY, new Name("年薪", 1));
		}
	};

	/**
	 * 组的key，对应solr的Field名称
	 */
	private String key;
	
	/**
	 * 组name，用于前端展示
	 */
	private String name;
	
	/**
	 * 组的条目集合
	 */
	private List<FilterItem> filterItems;
	
	/**
	 * 排序优先级，在FilterItemGroup的集合排序中，sortPriority数值大的FilterItemGroup排在前面
	 */
	private transient int sortPriority;
	
	public FilterItemGroup(String key) {
		this.key = key;
		for (Entry<Field, Name> entry: KEY_NAME.entrySet()) {
			if (entry.getKey().getName().equals(key)) {
				this.name = entry.getValue().value;
				this.sortPriority = entry.getValue().sortPriority;
			}
		}
	}

	/**
	 * FilterItemGroup 的name
	 * @author boyce
	 * @version 2014-2-21
	 */
	private static class Name {
		private String value;
		private int sortPriority;
		public Name(String value, int sortPriority) {
			super();
			this.value = value;
			this.sortPriority = sortPriority;
		}
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<FilterItem> getFilterItems() {
		return filterItems;
	}

	public void setFilterItems(List<FilterItem> filterItems) {
		this.filterItems = filterItems;
	}

	public int getSortPriority() {
		return sortPriority;
	}

	public void setSortPriority(int sortPriority) {
		this.sortPriority = sortPriority;
	}
	
}
