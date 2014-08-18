package com.gs.cvoud.solr.search.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.gs.cvoud.solr.annotation.CopyField;
import com.gs.cvoud.solr.annotation.Field;
import com.gs.cvoud.solr.annotation.OriginalField;
import com.gs.cvoud.util.ObjectUtils;

/**
 * Solr 查询结果统计描述，分布统计
 * 
 * @author boyce
 * @version 2013-9-12
 */
public final class DistributionResultStat {
	/**
	 * 需要统计的域
	 */
	public final static List<Field> STAT_ORIGINAL_FIELD = new ArrayList<Field>() {
		private static final long serialVersionUID = 1124529139517244551L;
		{
			add(CopyField.PROVINCE_COPY);
			add(CopyField.INDUSTRY_COPY);
			add(OriginalField.SALARY);
		}
	};

	/**
	 * 统计总数
	 */
	private int total;

	private transient Map<String, Map<String, DistributionCount>> maps = new HashMap<String, Map<String, DistributionCount>>();
	private Map<String, List<DistributionCount>> results = null;

	public DistributionResultStat(final int total) {
		if (total <= 0)
			throw new IllegalArgumentException("Cannot constract a DistributionCount it's total <= 0.");

		this.total = total;
		this.results = new HashMap<String, List<DistributionCount>>();

	}

	/**
	 * 添加一个统计域
	 * 
	 * @param originalField
	 *            域枚举描述
	 * @param count
	 *            域值不为空的结果数量
	 */
	public void addStatField(Field field, String value, int count) {
		if (count > total || count < 0) {
			throw new IllegalArgumentException("The total <= 0 or the field count > total.");
		}

		Map<String, DistributionCount> distributionCounts = maps.get(field.getName());
		if (ObjectUtils.isNull(distributionCounts)) {
			distributionCounts = new HashMap<String, DistributionCount>();
			this.maps.put(field.getName(), distributionCounts);
		}

		DistributionCount distributionCount = distributionCounts.get(value);
		if (ObjectUtils.isNull(distributionCount)) {
			distributionCount = new DistributionCount(value, count, DistributionCount.formatDouble((double) count
					/ total));
			distributionCounts.put(value, distributionCount);
		} else {
			int tcount = distributionCount.getCount() + count;
			distributionCount.setCount(tcount);
			distributionCount.setPercent(DistributionCount.formatDouble((double) tcount / total));
		}
	}

	public int getTotal() {
		return total;
	}

	public Map<String, List<DistributionCount>> getResults() {
		if (ObjectUtils.isNotNull(this.maps) && this.maps.size() != 0) {
			List<DistributionCount> distributionCounts = null;
			for (Entry<String, Map<String, DistributionCount>> entry1 : this.maps.entrySet()) {
				distributionCounts = new ArrayList<DistributionCount>();
				for (Entry<String, DistributionCount> entry2 : entry1.getValue().entrySet()) {
					distributionCounts.add(entry2.getValue());
				}

				results.put(entry1.getKey(), distributionCounts);
			}
		}

		return results;
	}

	/**
	 * 分布统计描述
	 * 
	 * @author boyce
	 * @version 2013-9-12
	 */
	public static class DistributionCount {
		public DistributionCount(String value, int count, double percent) {
			this.count = count;
			this.percent = percent;
			this.value = value;
		}

		private String value;

		/**
		 * 域值不为空的结果数量
		 */
		private int count;

		/**
		 * 域值不为空的结果与总结果的百分比
		 */
		private double percent;

		private static double formatDouble(double target) {
			BigDecimal bg = new BigDecimal(target);
			double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			return f1;
		}

		public int getCount() {
			return count;
		}

		public double getPercent() {
			return percent;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public void setPercent(double percent) {
			this.percent = percent;
		}

		/**
		 * 重写equals
		 */
		public boolean equals(Object obj) {
			if (null == obj || !(obj instanceof DistributionCount)) {
				return false;
			} else {
				DistributionCount other = (DistributionCount) obj;

				return ObjectUtils.equals(this.value, other.value);
			}
		}

		/**
		 * 重写hashCode
		 */
		public int hashCode() {
			int result = 17;

			result = result * 37 + ObjectUtils.hashCode(this.value);

			return result;
		}

	}

}
