package solr.searcher.assembler;

import org.apache.solr.client.solrj.SolrQuery;
import solr.annotation.Field;
import solr.annotation.SearchScope;
import solr.searcher.SearchRequest;
import utils.LatinUtils;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 查询条件组装者
 * 
 * @author boyce
 * @version 2013-9-11
 */
public class KeyWordQueryAssembler implements Assembler {
	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(KeyWordQueryAssembler.class);

	// 与的条件符号
	private static final String AND = " AND ";
	
	// 或的条件符号
	private static final String OR = " OR ";

	private SearchRequest request;
	
	public static void main(String[] args) {
		
		String keyword = "人力资源?总监";
        SearchRequest request = new SearchRequest();
		request.setKeyword(keyword);
		request.setSearchScope(SearchScope.ALL_TEXT);
		KeyWordQueryAssembler assembler = new KeyWordQueryAssembler(request);
		
		// 针对用户输入的关键词生成Query
		String input = request.getKeyword().trim().toLowerCase();

		// 将用户输入的关键字以空格分隔成数组
		String[] array = null;
		if (LatinUtils.isLatinString(input, true)) {
			array = new String[] { input };
		} else {
			array = input.split(" ");
		}

		// 对关键字数组中的多个关键字进行拆分，分词
		String result = null;
		if (array.length == 1 && StringUtils.isNotEmpty(array[0]))
			result = assembler.parseQuery(array[0]);
		else if(array.length > 1)
		{
			List<String> notEmpty = new ArrayList<String>();
			for (String a : array)
			{
				if (StringUtils.isNotEmpty(a))
					notEmpty.add(a);
			}
			if (notEmpty.size() == 1)
				result = assembler.parseQuery(notEmpty.get(0));
			else if (notEmpty.size() > 1)
			{
				StringBuilder builder = new StringBuilder();
				int i = 0;
				for (String ne: notEmpty)
				{
					if (i > 0)
						builder.append(AND);
					
					// 对关键字进行分词处理，并且添加搜索范围
					String tmp = assembler.parseQuery(ne);
					builder.append("(").append(tmp).append(")");
					i ++;
				}
				
				result = builder.toString();
			}
		}
		
		System.out.println(result);
				
	}

	public KeyWordQueryAssembler(SearchRequest request) {
		this.request = request;
	}

	/**
	 * 根据request组装查询query
	 */
	public void assembly(SolrQuery params) {
		// 设置请求条件
		this.setQuery(params);
	}

	private void setQuery(SolrQuery params) {
		if (StringUtils.isEmpty(request.getKeyword())) {
			params.setSortField(request.getSortField(), request.getOrder());
			return;
		}
		// 针对用户输入的关键词生成Query
		String input = request.getKeyword().trim().toLowerCase();
		// 将用户输入的关键字以空格分隔成数组
		String[] array = null;
		if (LatinUtils.isLatinString(input, true))
			array = new String[] { input };
		 else 
			array = input.split(" ");

		// 对关键字数组中的多个关键字进行拆分，分词
		String result = null;
		if (array.length == 1 && StringUtils.isNotEmpty(array[0]))
			result = this.parseQuery(array[0]);
		else if(array.length > 1)
		{
			List<String> notEmpty = new ArrayList<String>();
			for (String a : array)
			{
				if (StringUtils.isNotEmpty(a))
					notEmpty.add(a);
			}
			if (notEmpty.size() == 1)
				result = this.parseQuery(notEmpty.get(0));
			else if (notEmpty.size() > 1)
			{
				StringBuilder builder = new StringBuilder();
				int i = 0;
				for (String ne: notEmpty)
				{
					if (i > 0)
						builder.append(AND);
					
					// 对关键字进行分词处理，并且添加搜索范围
					String tmp = this.parseQuery(ne);
					builder.append("(").append(tmp).append(")");
					i ++;
				}
				
				result = builder.toString();
			}
		}
		
		if (StringUtils.isEmpty(result))
			return;
		
		LOG.info("The solr query param: " + result);
		params.setQuery(result);
	}

	/**
	 * 解析输入的查询字符串
	 */
	private String parseQuery(String input) {
		// 特殊字符处理
		input = handlerSpechars(input);
		if (StringUtils.isEmpty(input))
			return null;

		// 搜索范围
		SearchScope searchScope = request.getSearchScope();
		Field[] fields = searchScope.getFields();
		return setScopeSearch(fields, input);
	}

	// 根据搜索范围构建查询语句
	private static String setScopeSearch(Field[] fields, String input) {
		if (StringUtils.isEmpty(input))
			return input; 

		 StringBuilder builder = new StringBuilder();
		for (int i = 0; i < fields.length; i++) {
			if (i > 0)
				builder.append(OR);
			builder.append(fields[i].getName()).append(":").append(input);
		}
		return builder.toString();
	}
	
	/**
	 * 特殊字符处理
	 */
	private static String handlerSpechars(String input) {
		StringBuffer buffer = new StringBuffer();  
        String regex = "[+\\-&|!(){}\\[\\]^\"~*?:(\\)]";  
        Pattern pattern = Pattern.compile(regex);  
        Matcher matcher = pattern.matcher(input);  
        while(matcher.find()){  
            matcher.appendReplacement(buffer, "\\\\"+matcher.group());  
        }  
        matcher.appendTail(buffer); 
        return buffer.toString();
	}
}
