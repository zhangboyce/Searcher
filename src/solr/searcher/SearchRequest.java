package solr.searcher;

import org.apache.solr.client.solrj.SolrQuery;
import solr.annotation.OriginalField;
import solr.annotation.SearchScope;

/**
 * Created by boyce on 2014/8/18.
 * search request
 */
public class SearchRequest {

    private long id;
    private String keyword;

    //根据Field分组查询
    private String industry;
    private String function;
    private String degree;
    private String tag;
    private String province;
    private String city;

    // 年龄,如：25-30岁，30-35岁
    private String age;
    // 工作年限，如：5-10年，
    private String workYears;

    // 年薪范围,如10-15W，15-20W
    private String salary;

    private int pageOffset = 0;
    private int pageSize = 50;
    // 控制是否显示过滤条件 0 关闭(不显示), 1开启(显示)
    private int isFacet = 1;

    // 默认排序字段
    private String sortField = OriginalField.UPDATE_TIME.getName();
    private SolrQuery.ORDER order = SolrQuery.ORDER.desc;

    /**
     * 关键字搜索范围，默认是全文搜索
     */
    private SearchScope searchScope = SearchScope.ALL_TEXT;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWorkYears() {
        return workYears;
    }

    public void setWorkYears(String workYears) {
        this.workYears = workYears;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public int getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(int pageOffset) {
        this.pageOffset = pageOffset;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getIsFacet() {
        return isFacet;
    }

    public void setIsFacet(int isFacet) {
        this.isFacet = isFacet;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public SolrQuery.ORDER getOrder() {
        return order;
    }

    public void setOrder(SolrQuery.ORDER order) {
        this.order = order;
    }

    public SearchScope getSearchScope() {
        return searchScope;
    }

    public void setSearchScope(SearchScope searchScope) {
        this.searchScope = searchScope;
    }
}
