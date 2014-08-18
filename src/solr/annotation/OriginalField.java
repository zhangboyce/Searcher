package solr.annotation;

/**
 * 对加入solr索引的原始域的枚举描述
 * 
 * @author boyce
 * @version 2013-9-11
 */
public enum OriginalField implements Field {
	ID("id"), 
	NAME("name"),
	PHONE("phone"), 
	EMAIL("email"), 
	GENDER("gender"), 
	AGE("age"),
	WORK_YEARS("workYears"),
	SALARY("salary"),
	CV_NAME("cvName"),
	CV_CONTENT("cvContent"), 
	UPDATE_TIME("updateTime"),
	COMMENT("comment"),
    COMPANY("company"),

    INDUSTRY("industry", CopyField.INDUSTRY_COPY),
    FUNCTION("function", CopyField.FUNCTION_COPY),
    PROVINCE("province", CopyField.PROVINCE_COPY),
    TAGS("tags", CopyField.TAGS_COPY),
    DEGREE("degree", CopyField.DEGREE_COPY),
    CITY("city", CopyField.CITY_COPY);

	private OriginalField(String name, CopyField copyField) {
		this.name = name;
		this.copyField = copyField;
	}


	private OriginalField(String name) {
		this(name, null);
	}

	/**
	 * Field Name 在solr中建立域的名称，用于根据Field精确搜索，分组搜索等
	 */
	private String name;

	/**
	 * 该属性的分组字段，提供solr分组查询
	 */
	private CopyField copyField;

	public String getName() {
		return name;
	}

	public CopyField getCopyField() {
		return copyField;
	}

}
