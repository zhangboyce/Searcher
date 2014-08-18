package solr.searcher.assembler;

import org.apache.solr.client.solrj.SolrQuery;
import solr.annotation.Field;
import solr.annotation.SearchScope;

/**
 * Created by boyce on 2014/8/18.
 */
public class HighlightFieldAssembler implements Assembler {

    public void assembly(SolrQuery params) {
        // 只有通过关键字搜索才能高亮内容，分组查询不能高亮，
        params.setHighlight(true);

        // 所以将所有SearchScope中的域设置高亮
        this.addHighlightField(params, SearchScope.ALL_TEXT);

        params.setHighlightSimplePre("<mark>");// 标记，高亮关键字前缀
        params.setHighlightSimplePost("</mark>");// 后缀
        params.setHighlightSnippets(1);// 结果分片数，默认为1
        params.setHighlightFragsize(30);// 每个分片的最大长度，默认为100
    }

    // 为每一个SearchScope添加高亮
    private void addHighlightField(SolrQuery params, SearchScope searchScope) {
        Field[] fields = searchScope.getFields();
        for (Field field : fields) {
            params.addHighlightField(field.getName());
        }
    }
}
