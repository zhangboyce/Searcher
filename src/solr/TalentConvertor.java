package com.gs.cvoud.solr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

import com.gs.cvoud.model.Company;
import com.gs.cvoud.model.Cv;
import com.gs.cvoud.model.Tag;
import com.gs.cvoud.model.Talent;
import com.gs.cvoud.model.TalentIndustry;
import com.gs.cvoud.solr.annotation.OriginalField;
import com.gs.cvoud.solr.indexer.wrapper.SolrInputDocumentWrapper;
import com.gs.cvoud.util.CollectionUtils;
import com.gs.cvoud.util.ObjectUtils;
import com.gs.cvoud.util.StringUtils;

/**
 * 将solr的Document和Object相互装换的接口
 * 
 * @author boyce
 * @version 2013-7-8
 */
public final class TalentConvertor {
	/**
	 * 将Talent对象转换成SolrInputDocument对象
	 */
	public static SolrInputDocument toDocument(Talent talent) {
		SolrInputDocument solrInputDocument = new SolrInputDocument();
		SolrInputDocumentWrapper wrapper = new SolrInputDocumentWrapper(solrInputDocument);

		wrapper.addObjectAsStringField(OriginalField.ID, talent.getId());
		wrapper.addObjectAsStringField(OriginalField.GENDER, talent.getGender());
		wrapper.addObjectAsStringField(OriginalField.USER_ID, talent.getAddUserId());
		wrapper.addObjectAsStringField(OriginalField.COMMENT, talent.getComment());
		wrapper.addObjectField(OriginalField.BIRTH_YEAR, (null == talent.getBirthYear()) ? null :talent.getBirthYear());
		wrapper.addObjectField(OriginalField.GRADUATE_YEAR, (null == talent.getGraduateYear()) ? null : talent.getGraduateYear());
		wrapper.addObjectField(OriginalField.UPDATE_TIME, (null == talent.getUpdateTime()) ? talent.getAddTime()
				: talent.getUpdateTime());
		wrapper.addObjectField(OriginalField.SALARY, talent.getAnualSalary());
		wrapper.addStringField(OriginalField.NAME, talent.getName());
		wrapper.addStringField(OriginalField.PHONE, talent.getPhone());
		wrapper.addStringField(OriginalField.EMAIL, talent.getEmail());

		wrapper.addStringField(OriginalField.LOCATION, talent.getLocation());
		wrapper.addStringField(OriginalField.PROVINCE, talent.getProvince());
		wrapper.addStringField(OriginalField.COMPANY, talent.getCompanyName());
		wrapper.addStringField(OriginalField.JOBORDER, talent.getJobTitle());
		wrapper.addStringField(OriginalField.DEGREE, talent.getDegree());

		TalentIndustry industry = talent.getIndustry();
		if (ObjectUtils.isNotNull(industry)) {
			wrapper.addStringField(OriginalField.INDUSTRY_GROUP, industry.getIndustryGroupName());
			wrapper.addStringField(OriginalField.INDUSTRY, industry.getIndustryName());
			wrapper.addStringField(OriginalField.FUNCTION_GROUP, industry.getFunctionGroupName());
			wrapper.addStringField(OriginalField.FUNCTION, industry.getFunctionName());
		}

		// 候选人简历内容创建索引
		Cv cv = talent.getCv();
		if (ObjectUtils.isNotNull(cv)) {
			wrapper.addStringField(OriginalField.CV_NAME, cv.getName());
			wrapper.addStringField(OriginalField.CV_CONTENT, cv.getTxtContent());
		}

		List<Tag> tags = talent.getTagList(); 
		if (CollectionUtils.isNotEmpty(tags)) {
			for (Tag tag : tags) {
				wrapper.addStringField(OriginalField.TAGS, tag.getName());
			}
		}
		return wrapper.getSolrInputDocument();
	}

	/**
	 * 将solrDocument对象转换成talent对象
	 * 
	 * @param doc
	 * @return
	 */
	public static Talent toObject(SolrDocument doc) {
		Talent talent = new Talent();
		
		//talentId And userId
		String talentIdStr = (String) doc.getFieldValue(OriginalField.ID.getName());
		long talentId = Long.parseLong(talentIdStr);
		talent.setId(talentId);
		String userIdStr = (String) doc.getFieldValue(OriginalField.USER_ID.getName());
		long userId = Long.parseLong(userIdStr);
		talent.setAddUserId(userId);

		//updateTime
		Date updateTime = (Date) doc.getFieldValue(OriginalField.UPDATE_TIME.getName());
		talent.setUpdateTime(updateTime);

		//location
		String location = StringUtils.valueOf(doc.getFieldValue(OriginalField.LOCATION.getName()));
		talent.setLocation(location);
		
		//province
		String province = StringUtils.valueOf(doc.getFieldValue(OriginalField.PROVINCE.getName()));
		talent.setProvince(province);

		//industry
		String industryGroupName = StringUtils.valueOf(doc.getFieldValue(OriginalField.INDUSTRY_GROUP.getName()));
		String industryName = StringUtils.valueOf(doc.getFieldValue(OriginalField.INDUSTRY.getName()));
		String functionGroupName = StringUtils.valueOf(doc.getFieldValue(OriginalField.FUNCTION_GROUP.getName()));
		String functionName = StringUtils.valueOf(doc.getFieldValue(OriginalField.FUNCTION.getName()));
		
		TalentIndustry industry = new TalentIndustry();
		industry.setIndustryGroupName(industryGroupName);
		industry.setIndustryName(industryName);
		industry.setFunctionGroupName(functionGroupName);
		industry.setFunctionName(functionName);
		talent.setIndustry(industry);
		
		//name
		String name = StringUtils.valueOf(doc.getFieldValue(OriginalField.NAME.getName()));
		talent.setName(name);
		
		//phone
		String phone = StringUtils.valueOf(doc.getFieldValue(OriginalField.PHONE.getName()));
		talent.setPhone(phone);	
		
		//email
		String email = StringUtils.valueOf(doc.getFieldValue(OriginalField.EMAIL.getName()));
		talent.setEmail(email);	

		//salary
		String salaryStr = StringUtils.valueOf(OriginalField.SALARY.getName());
		if (StringUtils.isNotEmpty(salaryStr)) {
			try {
				Integer salary = Integer.parseInt(salaryStr);
				talent.setAnualSalary(salary);
			} catch (NumberFormatException e) {
				talent.setAnualSalary(null);
			}
		}
			
		//comment
		String comment = StringUtils.valueOf(doc.getFieldValue(OriginalField.COMMENT.getName()));
		talent.setComment(comment);

		//birthYear
		String birthYearStr = StringUtils.valueOf(doc.getFieldValue(OriginalField.BIRTH_YEAR.getName()));
		if (StringUtils.isNotEmpty(birthYearStr)) {
			try {
				Integer birthYear = Integer.parseInt(birthYearStr);
				talent.setBirthYear(birthYear);
			} catch (NumberFormatException e) {
				talent.setBirthYear(null);
			}
		}
		
		//graduateYear
		String graduateYearStr = StringUtils.valueOf(doc.getFieldValue(OriginalField.GRADUATE_YEAR.getName()));
		if (StringUtils.isNotEmpty(graduateYearStr)) {
			try {
				Integer graduateYear = Integer.parseInt(graduateYearStr);
				talent.setGraduateYear(graduateYear);
				
			} catch (NumberFormatException e) {
				talent.setGraduateYear(null);
			}
		}
		
		//gender
		if (doc.getFieldValue(OriginalField.GENDER.getName()) != null) {
			String gender = (String) doc.getFieldValue(OriginalField.GENDER.getName());
			int genderValue = Integer.parseInt(gender);
			talent.setGender(genderValue);
		}

		//company
		String companyName = StringUtils.valueOf(doc.getFieldValue(OriginalField.COMPANY.getName()));
		talent.setCompany(new Company(companyName));

		//degree
		String degree = StringUtils.valueOf(doc.getFieldValue(OriginalField.DEGREE.getName()));
		talent.setDegree(degree);
				
		//JobOrder
		String jobOrder = StringUtils.valueOf(doc.getFieldValue(OriginalField.JOBORDER.getName()));
		talent.setJobTitle(jobOrder);

		//tagList
		Collection tagColl = doc.getFieldValues(OriginalField.TAGS.getName());
		if (CollectionUtils.isNotEmpty(tagColl)) {
			List<Tag> tags = new ArrayList<Tag>();
			List<String> tagList = (List<String>) tagColl;
			Tag tag = null;
			for (String tagName: tagList) {
				tag = new Tag(tagName);
				tags.add(tag);
			}
			talent.setTagList(tags);
		}
		
		//cv
		String cvName = StringUtils.valueOf(doc.getFieldValue(OriginalField.CV_NAME.getName()));
		String cvContent = StringUtils.valueOf(doc.getFieldValue(OriginalField.CV_CONTENT.getName()));
		Cv cv = new Cv();
		cv.setId(talentId);
		cv.setTxtContent(cvContent);
		cv.setName(cvName);
		talent.setCv(cv);
		
		return talent;
	}

}
