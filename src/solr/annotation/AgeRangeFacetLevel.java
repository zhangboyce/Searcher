package solr.annotation;

import java.util.HashMap;
import java.util.Map;

import solr.annotation.RangeFacetLevel;

/**
 * age range facet level
 */
public enum AgeRangeFacetLevel implements RangeFacetLevel {

	ONE("20岁以下", AgeRangeFacetLevel.MIN_VALUE, 20),
	TWO("20-25岁", 20, 25),
	THREE("25-30岁", 25, 30),
	FOUR("30-35岁", 30, 35),
	FIVE("35-40岁", 35, 40),
	SIX("40-50岁", 40, 50),
	SEVEN("50岁以上", AgeRangeFacetLevel.MAX_VALUE, 50);

	private String name;
	private int start;
	private int end;

    public final static int MIN_VALUE = 0;
    public final static int MAX_VALUE = 70;
    public final static int MIN_GAP = 5;

	private AgeRangeFacetLevel(String name, int start, int end) {
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

	private final static Map<String, AgeRangeFacetLevel> LEVELS = new HashMap<String, AgeRangeFacetLevel>() {
		private static final long serialVersionUID = 1269777843374639565L;
		{
			AgeRangeFacetLevel[] levels = AgeRangeFacetLevel.values();
			for (AgeRangeFacetLevel level : levels) {
				put(level.name, level);
			}
		}
	};

	public static AgeRangeFacetLevel getRangeFacetLevelByName(String name) {
		return LEVELS.get(name);
	}

	public static AgeRangeFacetLevel getRangeFacetLevelByValue(int number) {
		AgeRangeFacetLevel[] levels = AgeRangeFacetLevel.values();
		for (AgeRangeFacetLevel level : levels) {
			if (number >= level.getStart() && number < level.getEnd()) {
				return level;
			}
		}

		return null;
	}
}
