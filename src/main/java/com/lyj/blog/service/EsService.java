package com.lyj.blog.service;

import com.lyj.blog.model.req.EsSearch;
import com.lyj.blog.parser.model.ESHeading;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/4 11:35 上午
 */
@Service
public class EsService {

    @Autowired
    RestHighLevelClient elasticClient;

    //根据blogId删除es中的headings
    public void deleteHeadingByBlogIdInES(String index,String blogId){
        GetIndexRequest getIndexRequest = new GetIndexRequest(index);
        try {
            boolean exists = elasticClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
            if(exists){
                DeleteByQueryRequest request = new DeleteByQueryRequest(index);
                request.setQuery(new TermQueryBuilder("blogId", blogId));
                request.setRefresh(true);
                // 删除操作
                elasticClient.deleteByQuery(request, RequestOptions.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //批量插入headings到es中
    public void insertHeadingToESBatch(String index, List<ESHeading> handledContent){
        BulkRequest request = new BulkRequest();

        for (ESHeading esHeading : handledContent) {
            request.add(new IndexRequest(index).id(esHeading.getHeadingId())
                    .source(XContentType.JSON,
                            "headingId", esHeading.getHeadingId(),
                            "blogId",esHeading.getBlogId(),
                            "blogName",esHeading.getBlogName(),
                            "tagName",esHeading.getTagName(),
                            "headingName",esHeading.getHeadingName(),
                            "content",esHeading.getContent()
                    ));
        }

        try {
            elasticClient.bulk(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 搜索关键字
    public SearchHit[] search(EsSearch esSearch) {
        if(esSearch.getKeyword()==null || "".equals(esSearch.getKeyword())){
            return new SearchHit[0];
        }
        SearchRequest searchRequest = new SearchRequest("blog");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 复合查询
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.should(QueryBuilders.matchQuery("blogName",esSearch.getKeyword()).operator(Operator.OR).boost(4f));//增加匹配程度
        queryBuilder.should(QueryBuilders.matchQuery("headingName",esSearch.getKeyword()).operator(Operator.OR).boost(2f));//增加匹配程度
        //todo 先用空格分割，然后
        queryBuilder.should(QueryBuilders.matchQuery("content",esSearch.getKeyword()).operator(Operator.AND));//必须有出现关键字
        if(esSearch.getTagKeyword()!=null && !"".equals(esSearch.getTagKeyword())){
            queryBuilder.filter(QueryBuilders.matchQuery("tagName",esSearch.getTagKeyword()).operator(Operator.AND));
        }
        searchSourceBuilder.query(queryBuilder);


        System.out.println(searchSourceBuilder.toString());


        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = elasticClient.search(searchRequest, RequestOptions.DEFAULT);
            return searchResponse.getHits().getHits();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new SearchHit[0];
    }
}
