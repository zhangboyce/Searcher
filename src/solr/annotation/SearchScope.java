package solr.annotation;

/**
 * 关键字搜索范围
 * 
 * @author boyce
 * @version 2013-9-11
 */
public enum SearchScope {

	/**
	 * 全文搜索
	 */
	ALL_TEXT(new Field[] {
            OriginalField.NAME,
            OriginalField.PHONE,
            OriginalField.EMAIL,
            OriginalField.PROVINCE,
            OriginalField.CITY,
			OriginalField.COMPANY,
            OriginalField.INDUSTRY,
            OriginalField.FUNCTION,
            OriginalField.CV_NAME,
            OriginalField.CV_CONTENT,
			OriginalField.TAGS,
            OriginalField.DEGREE,
            OriginalField.AGE,
            OriginalField.SALARY,
            OriginalField.WORK_YEARS,
            OriginalField.DEGREE,
            OriginalField.COMMENT
    }),

	/**
	 * 从行业职能中搜索
	 */
	INDUSTRY_FUNCTION(new Field[] {
            OriginalField.INDUSTRY,
			OriginalField.FUNCTION
    }),

	/**
	 * 从联系信息中搜索
	 */
	CONTACT_INFO(new Field[] {
            OriginalField.NAME,
            OriginalField.PHONE,
            OriginalField.EMAIL,
            OriginalField.PROVINCE,
            OriginalField.CITY
    }),

	/**
	 * 从工作经验中搜索
	 */
	GRADUATE_YEAR(new Field[] { OriginalField.WORK_YEARS }),

	/**
	 * 从公司中搜索
	 */
	COMPANY(new Field[] { OriginalField.COMPANY }),

	/**
	 * 从备注中搜索
	 */
	COMMENT(new Field[] { OriginalField.COMMENT });

	private SearchScope(Field[] fields) {
		this.fields = fields;
	}

	/**
	 * 搜索范围，在copy域中搜索
	 */
	private Field[] fields;

	public Field[] getFields() {
		return fields;
	}

}
