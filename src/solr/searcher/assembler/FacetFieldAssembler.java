package solr.searcher.assembler;

import org.apache.solr.client.solrj.SolrQuery;
import solr.annotation.*;
import solr.searcher.SearchRequest;
import utils.ObjectUtils;
import utils.StringUtils;

/**
 * 分组查询组件组装者
 *
 * @author boyce
 * @version 2013-9-11
 */
public class FacetFieldAssembler implements Assembler {
	private SearchRequest request;

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
            .getLogger(FacetFieldAssembler.class);

	public FacetFieldAssembler(SearchRequest request) {
		this.request = request;
	}

	public void assembly(SolrQuery params) {
        params.setFacet(true);
        params.setFacetMinCount(1);

        this.addComplexFacetField(params);
        this.addFacetField(params);
	}

    /**
     * 添加FacetField
     */
    private void addFacetField(SolrQuery params) {
        // 筛选条件：学历Degree
        if (StringUtils.isNotEmpty(request.getDegree()))
            CopyField.DEGREE_COPY.addFilterQuery(params, request.getDegree());
        else
            CopyField.DEGREE_COPY.addFacetField(params);

        // 筛选条件：行业
        if (StringUtils.isNotEmpty(request.getIndustry()))
            CopyField.INDUSTRY_COPY.addFilterQuery(params, request.getIndustry());
        else
            CopyField.INDUSTRY_COPY.addFacetField(params);

        // 筛选条件：职能
        if (StringUtils.isNotEmpty(request.getFunction()))
            CopyField.FUNCTION_COPY.addFilterQuery(params, request.getFunction());
        else
            CopyField.FUNCTION_COPY.addFacetField(params);

        //筛选条件：地区
        if (StringUtils.isNotEmpty(request.getProvince()))
            CopyField.PROVINCE_COPY.addFilterQuery(params, request.getProvince());
        else
            CopyField.PROVINCE_COPY.addFacetField(params);

        //筛选条件：标签，标签不分组
        if (StringUtils.isNotEmpty(request.getTag()))
            CopyField.TAGS_COPY.addFilterQuery(params, request.getTag());
    }

    // 复杂的FacetField添加
    private void addComplexFacetField(SolrQuery params) {
		/* age 是RangeQuery和RangeFacet */
        if (ObjectUtils.isNotNull(request.getAge())) {
            String name = request.getAge();
            AgeRangeFacetLevel level = AgeRangeFacetLevel.getRangeFacetLevelByName(name);
            this.setRangeQuery(params, name, level, OriginalField.AGE);
        } else {
            params.addNumericRangeFacet(OriginalField.AGE.getName(), AgeRangeFacetLevel.MIN_VALUE,
                    AgeRangeFacetLevel.MAX_VALUE, AgeRangeFacetLevel.MIN_GAP);
        }

		/* workYear 工作经验也是RangeQuery和RangeFacet */
        if (ObjectUtils.isNotNull(request.getWorkYears())) {
            String name = request.getWorkYears();
            WorkYearRangeFacetLevel level = WorkYearRangeFacetLevel.getRangeFacetLevelByName(name);
            this.setRangeQuery(params, name, level, OriginalField.WORK_YEARS);
        } else {
            params.addNumericRangeFacet(OriginalField.WORK_YEARS.getName(), WorkYearRangeFacetLevel.MIN_VALUE,
                    WorkYearRangeFacetLevel.MAX_VALUE, WorkYearRangeFacetLevel.MIN_GAP);
        }

		/* salary 年薪也是RangeQuery和RangeFacet */
        if (ObjectUtils.isNotNull(request.getSalary())) {
            String name = request.getSalary();
            SalaryRangeFacetLevel level = SalaryRangeFacetLevel.getRangeFacetLevelByName(name);
            this.setRangeQuery(params, name, level, OriginalField.SALARY);
        } else {
            params.addNumericRangeFacet(OriginalField.SALARY.getName(), SalaryRangeFacetLevel.MIN_VALUE,
                    SalaryRangeFacetLevel.MAX_VALUE, SalaryRangeFacetLevel.MIN_GAP);
        }
    }

    private void setRangeQuery(SolrQuery params, String name, RangeFacetLevel level, OriginalField field) {
        String query = "";
        if (ObjectUtils.isNull(level)) {
            logger.error("Cannot find the " + field.getName() + " level for name: " + name);
        } else {
            query = field.getName() + ":[" + level.getStart() + " TO " + level.getEnd() + "}";
            params.addFilterQuery(query);
        }
    }
}
