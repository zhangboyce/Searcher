package solr.annotation;

/**
 * 范围查询等级的接口
 * 
 * @author boyce
 * @version 2013-10-25
 */
public interface RangeFacetLevel {
	/**
	 * key的名称
	 */
	public String getName();

	/**
	 * 范围的下限值
	 */
	public int getStart();

	/**
	 * 范围的上限值
	 */
	public int getEnd();

}
