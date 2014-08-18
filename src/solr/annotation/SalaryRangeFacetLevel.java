package solr.annotation;

import solr.annotation.RangeFacetLevel;

import java.util.HashMap;
import java.util.Map;

/**
 * salary range facet level
 */
public enum SalaryRangeFacetLevel implements RangeFacetLevel {

	ONE("10万以下", 0, 10),
	TWO("10-15万", 10, 15), 
	THREE("15-20万", 15, 20), 
	FOUR("20-30万", 20, 30), 
	FIVE("30-40万", 30, 40), 
	SIX("40-50万", 40, 50), 
	SEVEN("50-70万", 50, 70), 
	EIGHT("70-100万", 70, 100),
	NINE("100万以上", 100, SalaryRangeFacetLevel.MAX_VALUE);

	private String name;
	private int start;
	private int end;

	public final static int MAX_VALUE = 1000;
	public final static int MIN_VALUE = 0;
	public final static int MIN_GAP = 5;

	private SalaryRangeFacetLevel(String name, int start, int end) {
		this.name = name;
		this.start = start;
		this.end = end;
	}

	public String getName() {
		return this.name;
	}

	public int getStart() {
		return this.start;
	}

	public int getEnd() {
		return this.end;
	}

	private final static Map<String, SalaryRangeFacetLevel> LEVELS = new HashMap<String, SalaryRangeFacetLevel>() {
		private static final long serialVersionUID = 1269777843374639565L;
		{
			SalaryRangeFacetLevel[] levels = SalaryRangeFacetLevel.values();
			for (SalaryRangeFacetLevel level : levels) {
				put(level.name, level);
			}
		}
	};

	public static SalaryRangeFacetLevel getRangeFacetLevelByName(String name) {
		return LEVELS.get(name);
	}

	public static SalaryRangeFacetLevel getRangeFacetLevelByValue(int number) {
		SalaryRangeFacetLevel[] levels = SalaryRangeFacetLevel.values();
		for (SalaryRangeFacetLevel level : levels) {
			if (number >= level.getStart() && number < level.getEnd()) {
				return level;
			}
		}
		return null;
	}

}
