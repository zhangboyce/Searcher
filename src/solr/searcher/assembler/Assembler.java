package solr.searcher.assembler;

import org.apache.solr.client.solrj.SolrQuery;

/**
 * Created by boyce on 2014/8/18.
 * a solr query condition assembler
 */
public interface Assembler {

    /**
     * assembly query condition to SolrQuery
     * @param params
     */
    public void assembly(SolrQuery params);
}
