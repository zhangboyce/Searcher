package com.gs.cvoud.solr.common;

import java.rmi.AccessException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.gs.cvoud.solr.annotation.AgeRangeFacetLevel;
import com.gs.cvoud.solr.annotation.OriginalField;
import com.gs.cvoud.solr.annotation.RangeFacetLevel;
import com.gs.cvoud.solr.annotation.SalaryRangeFacetLevel;
import com.gs.cvoud.solr.annotation.WorkYearRangeFacetLevel;
import com.gs.cvoud.solr.search.response.FilterItem;
import com.gs.cvoud.solr.search.response.FilterItemGroup;
import com.gs.cvoud.util.CollectionUtils;

/**
 * 搜索帮助类
 * 
 * @author boyce
 * @version 2013-7-9
 */
public final class SolrHelper {

	private SolrHelper() throws AccessException {
		throw new AccessException("Cannot access the constractor.");
	}

	// 为空的域值
	private final static List<Object> NON_FIELD = new ArrayList<Object>() {
		private static final long serialVersionUID = -7945063104357216256L;
		{
			add(OriginalField.INDUSTRY.getNonKey());
			add(OriginalField.FUNCTION.getNonKey());
			add(OriginalField.DEGREE.getNonKey());
			add(OriginalField.PROVINCE.getNonKey());
			add(OriginalField.TAGS.getNonKey());
			
			add(OriginalField.SALARY.getNonKey());
			add(OriginalField.BIRTH_YEAR.getNonKey());
			add(OriginalField.GRADUATE_YEAR.getNonKey());
		}
	};

	public static Map<RangeFacetLevel, Integer> sortRangeFacetLevelByKey(Map<RangeFacetLevel, Integer> map) {
		return CollectionUtils.sort(map, new Comparator<Map.Entry<RangeFacetLevel, Integer>>() {
			public int compare(Entry<RangeFacetLevel, Integer> o1, Entry<RangeFacetLevel, Integer> o2) {
				RangeFacetLevel o1Key = o1.getKey();
				RangeFacetLevel o2Key = o2.getKey();
				
				Integer t1 = o1Key.getStart() + o1Key.getEnd();
				Integer t2 = o2Key.getStart() + o2Key.getEnd();
				
				//年薪
				if (o1Key instanceof SalaryRangeFacetLevel) {
					
					//将分组中"未标注"的排序在最前面
					if (o1Key == SalaryRangeFacetLevel.NULL)
						t2 = 100000000;
					if (o2Key == SalaryRangeFacetLevel.NULL)
						t1 = 100000000;
					return t1.compareTo(t2);
				}
				
				//工龄和年龄的level中。start和end表示毕业年份和出生日期，年份越大，龄越小
				else 
					//将分组中"未标注"的排序在最前面
					if (o1Key == WorkYearRangeFacetLevel.NULL || o1Key == AgeRangeFacetLevel.NULL)
						t1 = 100000000;
				    if (o2Key == WorkYearRangeFacetLevel.NULL || o2Key == AgeRangeFacetLevel.NULL)
					    t2 = 100000000;
					return t2.compareTo(t1);
			}
		});
	}
	
	public static List<FilterItem> sortFilterItemByKey(List<FilterItem> filterItems) {
		Collections.sort(filterItems, new Comparator<FilterItem>() {
			public int compare(FilterItem o1, FilterItem o2) {
				
				String o1Key = o1.getKey();
				String o2Key = o2.getKey();
				Integer o1Value = o1.getValue();
				Integer o2Value = o2.getValue();

				//将分组中"未标注"的排序在最前面
				if (NON_FIELD.contains(o1Key)) {
					o1Value = 1000000000;
				}
				if (NON_FIELD.contains(o2Key)) {
					o2Value = 1000000000;
				}
				return -(o1Value.compareTo(o2Value));
			}
		});
		return filterItems;
	}
	
	public static List<FilterItemGroup> sortFilterItemGroupByKey(List<FilterItemGroup> filterItemGroups) {
		Collections.sort(filterItemGroups, new Comparator<FilterItemGroup>() {
			public int compare(FilterItemGroup o1, FilterItemGroup o2) {
				
				//优先级越高的排在最前面
				return -(o1.getSortPriority() - o2.getSortPriority());
			}
		});
		return filterItemGroups;
	}

}
