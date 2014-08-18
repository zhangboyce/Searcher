package solr.annotation;

import java.util.HashMap;
import java.util.Map;

import solr.annotation.RangeFacetLevel;

/**
 * work years range facet level
 */
public enum WorkYearRangeFacetLevel implements RangeFacetLevel {

	ZERO("1年以下", 1, 0),
	ONE("1-3年", 3, 1), 
	TWO("3-5年", 5, 3), 
	THREE("5-7年", 7, 5), 
	FOUR("7-11年", 11, 7), 
	FIVE("11-15年",15, 11), 
	SIX("15-21年", 21, 15), 
	SEVEN("21年以上", 60, 21);

	private String name;
	private int start;
	private int end;

    public final static int MAX_VALUE = 60;
    public final static int MIN_VALUE = 0;
    public final static int MIN_GAP = 1;

	private WorkYearRangeFacetLevel(String name, int start, int end) {
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

	private final static Map<String, WorkYearRangeFacetLevel> LEVELS = new HashMap<String, WorkYearRangeFacetLevel>() {
		private static final long serialVersionUID = 1269777843374639565L;
		{
			WorkYearRangeFacetLevel[] levels = WorkYearRangeFacetLevel.values();
			for (WorkYearRangeFacetLevel level : levels) {
				put(level.name, level);
			}
		}
	};

	public static WorkYearRangeFacetLevel getRangeFacetLevelByName(String name) {
		return LEVELS.get(name);
	}

	public static WorkYearRangeFacetLevel getRangeFacetLevelByValue(int number) {
		WorkYearRangeFacetLevel[] levels = WorkYearRangeFacetLevel.values();
		for (WorkYearRangeFacetLevel level : levels) {
			if (number >= level.getStart() && number < level.getEnd()) {
				return level;
			}
		}

		return null;
	}

}
