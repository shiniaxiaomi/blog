package com.lyj.blog.service;

import com.lyj.blog.model.req.EsResult;
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
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    public Map search(EsSearch esSearch, int page) {
//        page--;//前端的页数从1开始，es的页数从0开始，在这里做一下转化
        HashMap<String, Object> map = new HashMap<>();
        map.put("result",Collections.emptyList());
        map.put("pages",1);
        map.put("currentPage",1);
        map.put("total",0);
        map.put("url","/blog/search/");

        int size=5;
        if((esSearch.getKeyword()==null || "".equals(esSearch.getKeyword()))
                && (esSearch.getTagKeyword()==null || "".equals(esSearch.getTagKeyword()))){
            return map;
        }
        SearchRequest searchRequest = new SearchRequest("blog");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 复合查询
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if(!"".equals(esSearch.getKeyword())){
            queryBuilder.should(QueryBuilders.matchQuery("blogName",esSearch.getKeyword()).boost(4f));//增加匹配程度
            queryBuilder.should(QueryBuilders.matchQuery("headingName",esSearch.getKeyword()).boost(2f));//增加匹配程度
            queryBuilder.should(QueryBuilders.matchQuery("content",esSearch.getKeyword()));//必须有出现关键字
        }
        if(esSearch.getTagKeyword()!=null && !"".equals(esSearch.getTagKeyword())){
            queryBuilder.filter(QueryBuilders.matchQuery("tagName",esSearch.getTagKeyword()));
        }
        searchSourceBuilder.query(queryBuilder).size(size).from((page-1)*size);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse=elasticClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return map;
        }

        List<EsResult> collect = Arrays.stream(searchResponse.getHits().getHits()).map(searchHit -> {
            return new EsResult().setScore(searchHit.getScore()).setSourceAsMap(searchHit.getSourceAsMap());
        }).collect(Collectors.toList());

        int total= (int) searchResponse.getHits().getTotalHits().value;
        map.put("result",collect);
        map.put("pages",total%size==0?total/size:total/size+1);
        map.put("currentPage",page);//返回给前端的时候重新转化一下
        map.put("total",total);
        map.put("url","/blog/search/");

        return map;
    }
}
