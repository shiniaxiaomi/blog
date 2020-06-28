package bak.service;

//import com.lyj.blog.util.VarUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Elasticsearch相关的操作
 * @author yingjie.lu
 * @version 1.0
 * @date 2019/12/11 12:50 下午
 */

//@Service
@Slf4j
public class ElasticsearchService {

    @Autowired
    RestHighLevelClient client;

    //初始化所有header数据(如果没有header的博客是不允许出现的)
    public void addHeaderBulk() throws IOException {
        //创建index请求
        BulkRequest request = new BulkRequest();

//        Iterator<String> iterator = ESHeader.map.keySet().iterator();
//        while(iterator.hasNext()){
//            List<ESHeader> headerList = ESHeader.map.get(iterator.next());
//            for(ESHeader header:headerList){
//                //添加数据
//                request.add(new IndexRequest("header", "doc", String.valueOf(Randomness.get().nextInt()))
//                        .source(XContentType.JSON,
//                                "headerName", header.getHeaderName(),
//                                "blogId",header.getBlogId(),
//                                "blogName",header.getBlogName(),
//                                "headerContent",header.getHeaderContent(),
//                                "level",header.getLevel()));
//            }
//        }

        //通过client创建批量添加索引
        BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
        if (bulkResponse.status().getStatus()== 200) {
            log.info("elasticsearch初始化批量添加成功");
        }else{
            log.error("elasticsearch初始化批量添加成功");
        }
    }

    //查询所有的header
//    public SearchHit[] searchAllHeader(String keyword,int page) throws IOException {
//        //构造查询条件
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        sourceBuilder.query(QueryBuilders.matchQuery("headerName",keyword));//使用正则匹配查询
//
//        sourceBuilder.from(page*VarUtil.pageSize);
//        sourceBuilder.size((page+1)*VarUtil.pageSize);
//
//        //指定查询的索引
//        SearchRequest searchRequest = new SearchRequest().indices("header").source(sourceBuilder);
//
//        //通过client进行查询
//        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
//        return search.getHits().getHits();
//    }
//
//
//    public SearchHit[] searchAll(String keyword,int page) throws IOException {
//
//        //构造查询条件
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//
//        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
//                //默认首个关键字很重要
//                .should(QueryBuilders.matchQuery("blogName", keyword).boost(2f)) //设置必须匹配blogText
//                .should(QueryBuilders.matchQuery("headerContent", keyword)) //设置headerName匹配
//                .should(QueryBuilders.matchQuery("headerName", keyword).boost(1.5f)) //文章名称设置得分的比例为为5
//                ;
//        sourceBuilder.query(queryBuilder);
//
////        QueryBuilders.moreLikeThisQuery();//做内容推荐
////        sourceBuilder.fetchSource();//排除返回的source数据的一些字段
//
//        sourceBuilder.from(page*VarUtil.pageSize);
//        sourceBuilder.size((page+1)*VarUtil.pageSize);
//
//        //指定查询的索引
//        SearchRequest searchRequest = new SearchRequest().indices("header").source(sourceBuilder);
//
//        //通过client进行查询
//        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
//        return search.getHits().getHits();
//    }
//
//    //删除索引
//    public void deleteIndex(String index) throws IOException {
//        DeleteIndexRequest request = new DeleteIndexRequest(index);
//        AcknowledgedResponse deleteIndexResponse = client.indices().delete(request, RequestOptions.DEFAULT);
//    }
//
//    //查询是否存在索引
//    public boolean existIndex(String index) throws IOException {
//        GetIndexRequest request = new GetIndexRequest("header");
//        return client.indices().exists(request, RequestOptions.DEFAULT);
//    }

}
