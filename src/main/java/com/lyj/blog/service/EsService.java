package com.lyj.blog.service;

import com.lyj.blog.exception.MessageException;
import com.lyj.blog.model.req.EsResult;
import com.lyj.blog.model.req.EsSearch;
import com.lyj.blog.model.req.Message;
import com.lyj.blog.parser.model.EsHeading;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/4 11:35 上午
 */
@Slf4j
@Service
public class EsService {

    @Autowired
    RestHighLevelClient elasticClient;

    @Autowired
    HttpSession session;

    @Autowired
    RestTemplate restTemplate;

    @Value("http://${myConfig.elasticsearch.url}")
    private String elasticsearchUrl;

    @Autowired
    BlogService blogService;

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

    /**
     * 批量插入headings到es中
     * @param index
     * @param esHeadings
     */
    public void insertHeadingToESBatch(String index, List<EsHeading> esHeadings){
        BulkRequest request = new BulkRequest();

        for (EsHeading esHeading : esHeadings) {
            request.add(new IndexRequest(index).id(esHeading.getHeadingId())
                    .source(XContentType.JSON,
                            "headingId", esHeading.getHeadingId(),
                            "blogId",esHeading.getBlogId(),
                            "blogName",esHeading.getBlogName(),
                            "tagName",esHeading.getTagName(),
                            "headingName",esHeading.getHeadingName(),
                            "content",esHeading.getContent(),
                            "isPrivate",esHeading.isPrivate()  //标记为是否私有
                    ));
        }

        try {
            elasticClient.bulk(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 搜索关键字
     * @param esSearch
     * @param page
     * @return
     */
    public Map search(EsSearch esSearch, int page) {
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
            queryBuilder.should(QueryBuilders.matchQuery("blogName",esSearch.getKeyword()).boost(2.5f));//增加匹配程度
            queryBuilder.should(QueryBuilders.matchQuery("headingName",esSearch.getKeyword()).boost(1.5f));//增加匹配程度
            queryBuilder.should(QueryBuilders.matchQuery("content",esSearch.getKeyword()));//必须有出现关键字
        }
        if(esSearch.getTagKeyword()!=null && !"".equals(esSearch.getTagKeyword())){
            queryBuilder.filter(QueryBuilders.matchQuery("tagName",esSearch.getTagKeyword()));
        }
        // 如果未登入，则过滤掉私有数据
        if(session.getAttribute("isLogin")==null){
            queryBuilder.filter(QueryBuilders.matchQuery("isPrivate","false"));//过滤出isPrivate为false的数据
        }
        searchSourceBuilder.query(queryBuilder).size(size).from((page-1)*size);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;

        int i=2;// 重试两次（避免为查询数据为空）
        do {
            try {
                searchResponse=elasticClient.search(searchRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                log.error("ES搜索错误",e);
                return map;
            }
            i--;
        }while(!(searchResponse!=null && searchResponse.getHits().getHits().length!=0) && i>0 );

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


    /**
     * 根据blogId更新是否私有
     * @param blogId
     * @param isPrivate
     */
    private void updateIsPrivateByBlogId(Integer blogId, Boolean isPrivate) {
        // post请求
        HttpHeaders headers = new HttpHeaders();// 添加请求头
        headers.add("Content-Type","application/json");
        HttpEntity<String> entity = new HttpEntity<>("{\n" +
                "    \"script\": {\n" +
                "        \"source\": \"ctx._source.isPrivate="+isPrivate+"\",\n" +
                "        \"lang\": \"painless\"\n" +
                "    },\n" +
                "    \"query\": {\n" +
                "        \"term\": {\n" +
                "            \"blogId\": \""+blogId+"\"\n" +
                "        }\n" +
                "    }\n" +
                "}", headers);
        try {
            restTemplate.postForObject(elasticsearchUrl+"/blog/_update_by_query?format=JSON&pretty", entity, String.class);
        }catch (Exception e){
            throw new MessageException("es的权限更新失败");
        }
    }

    // 批量根据blogId更新私有状态
    public void updateIsPrivateByBlogIds(List<Integer> blogIds,Boolean isPrivate){
        for(Integer blogId:blogIds){
            updateIsPrivateByBlogId(blogId,isPrivate);
        }
    }

    /**
     * 根据blogId更新blogName
     * @param blogId
     * @param blogName
     */
    public void updateBlogNameByBlogId(Integer blogId,String blogName) {
        // post请求
        HttpHeaders headers = new HttpHeaders();// 添加请求头
        headers.add("Content-Type","application/json");
        HttpEntity<String> entity = new HttpEntity<>("{\n" +
                "    \"script\": {\n" +
                "        \"source\": \"ctx._source.blogName ='"+blogName+"'\",\n" +
                "        \"lang\": \"painless\"\n" +
                "    },\n" +
                "    \"query\": {\n" +
                "        \"term\": {\n" +
                "            \"blogId\": \""+blogId+"\"\n" +
                "        }\n" +
                "    }\n" +
                "}", headers);
        try {
            restTemplate.postForObject(elasticsearchUrl+"/blog/_update_by_query?format=JSON&pretty", entity, String.class);
        }catch (Exception e){
            throw new MessageException("es的权限更新失败");
        }
    }

    /**
     * 根据blogId更新tagNames
     * @param blogId
     * @param tagNames
     */
    public void updateTagNameByBlogId(int blogId, String tagNames) {
        // post请求
        HttpHeaders headers = new HttpHeaders();// 添加请求头
        headers.add("Content-Type","application/json");
        HttpEntity<String> entity = new HttpEntity<>("{\n" +
                "    \"script\": {\n" +
                "        \"source\": \"ctx._source.tagName ='"+tagNames+"'\",\n" +
                "        \"lang\": \"painless\"\n" +
                "    },\n" +
                "    \"query\": {\n" +
                "        \"term\": {\n" +
                "            \"blogId\": \""+blogId+"\"\n" +
                "        }\n" +
                "    }\n" +
                "}", headers);
        try {
            restTemplate.postForObject(elasticsearchUrl+"/blog/_update_by_query?format=JSON&pretty", entity, String.class);
        }catch (Exception e){
            throw new MessageException("es的权限更新失败");
        }
    }

    /**
     * 通过json并发送Get请求搜索数据
     * @param json
     * @return
     */
    public Message searchDataByGet(String json){
        try {
            String data = restTemplate.getForObject(elasticsearchUrl +json, String.class);
            return Message.success(null,data);
        }catch (Exception e){
            return Message.error("请确认是否是请求的方法不对:"+e.getMessage());
        }
    }

    /**
     * 通过json并发送Post请求搜索数据
     * @param json
     * @return
     */
    public Message searchDataByPost(String json){
        HttpHeaders headers = new HttpHeaders();// 添加请求头
        headers.add("Content-Type","application/json");
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        try {
            String data = restTemplate.postForObject(elasticsearchUrl +"/_search?format=JSON&pretty", entity, String.class);
            return Message.success(null,data);
        }catch (Exception e){
            return Message.error(e.getMessage());
        }
    }

    /**
     * 通过传入的json来更新ES中的数据
     * @param json
     * @return
     */
    public Message updateData(String json){
        // post请求
        HttpHeaders headers = new HttpHeaders();// 添加请求头
        headers.add("Content-Type","application/json");
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        try {
            String data = restTemplate.postForObject(elasticsearchUrl +"/blog/_update_by_query?format=JSON&pretty", entity, String.class);
            return Message.success("更新成功",data);
        }catch (Exception e){
            return Message.error(e.getMessage());
        }
    }

    /**
     * 通过传入的json来删除ES中的数据
     * @param json
     * @return
     */
    public Message deleteData(String json){
        // post请求
        HttpHeaders headers = new HttpHeaders();// 添加请求头
        headers.add("Content-Type","application/json");
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        try {
            String data = restTemplate.postForObject(elasticsearchUrl +"/blog/_delete_by_query?format=JSON&pretty", entity, String.class);
            return Message.success("删除成功",data);
        }catch (Exception e){
            return Message.error(e.getMessage());
        }
    }

}
