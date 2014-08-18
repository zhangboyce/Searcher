package com.gs.cvoud.solr.search.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.gs.cvoud.solr.annotation.OriginalField;
import com.gs.cvoud.util.ObjectUtils;

/**
 * @author boyce
 * @version 2013-9-12
 */
public final class AttributeResultStat {
	private int total;

	private final List<FieldStatCount> counts = new ArrayList<FieldStatCount>();

	public AttributeResultStat(int total) {
		if (total <= 0)
			throw new IllegalArgumentException("Cannot constract a AttributeResultStat it's total <= 0.");

		this.total = total;
	}

	public void addStatField(OriginalField field, int count) {
		if (count > total || count < 0) {
			throw new IllegalArgumentException("The total <= 0 or the field count > total.");
		}

		this.counts.add(new FieldStatCount(field, count, FieldStatCount.formatDouble((double) count / total)));
	}

	public int getTotal() {
		return total;
	}

	public List<FieldStatCount> getCounts() {
		return counts;
	}

	public static class FieldStatCount {
		public FieldStatCount(OriginalField field, int count, double percent) {
			this.count = count;
			this.percent = percent;
			this.fieldKey = field.name();
		}

		private String fieldKey;

		private int count;

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

		public String getFieldKey() {
			return fieldKey;
		}

		public boolean equals(Object obj) {
			if (null == obj || !(obj instanceof FieldStatCount)) {
				return false;
			} else {
				FieldStatCount other = (FieldStatCount) obj;

				return ObjectUtils.equals(this.fieldKey, other.fieldKey);
			}
		}

		public int hashCode() {
			int result = 17;

			result = result * 37 + ObjectUtils.hashCode(this.fieldKey);

			return result;
		}
	}

}
