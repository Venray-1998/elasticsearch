package com.fwr.elasticsearch.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fwr.elasticsearch.entity.AjaxResult;
import com.fwr.elasticsearch.entity.Book;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author fwr
 * @date 2020-12-02
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class RHLController {

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private RestClient restClient;

    @GetMapping("/insert")
    public void insert() {
        Book book = new Book();
        book.setId(10);
        book.setName("测试名称");
        book.setText("测试文本");
        book.setDate(new Date());
        //json转换日期要在日期字段那里加@JSONField(format = "yyyy-MM-dd")
        String source = JSON.toJSONString(book);
        IndexRequest indexRequest = new IndexRequest("book").id("10").source(source, XContentType.JSON);
        IndexResponse response = null;
        try {
            response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/update")
    public void update() {
        Book book = new Book();
        book.setId(11);
        book.setName("测试名称1");
        book.setText("测试文本1");
        book.setDate(new Date());
        String source = JSON.toJSONString(book);
        UpdateRequest updateRequest = new UpdateRequest("book", "10").doc(source, XContentType.JSON);
        try {
            UpdateResponse update = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
            System.out.println(update);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/delete")
    public void delete() {
        DeleteRequest deleteRequest = new DeleteRequest("book","10");
//        DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest("book");
//        deleteByQueryRequest.setQuery(QueryBuilders.matchAllQuery());
        try {
            DeleteResponse response = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
//            BulkByScrollResponse bulkByScrollResponse = restHighLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/bulk")
    public void bulk() {
        Book book1 = new Book();
        book1.setId(20);
        book1.setName("测试");
        book1.setText("测试文本20，测试名称20");
        book1.setDate(new Date());
        Book book2 = new Book();
        book2.setId(21);
        book2.setName("测试名称21");
        book2.setText("测试文本21");
        book2.setDate(new Date());

        List<Book> bookList = new ArrayList<>();
        bookList.add(book1);
        bookList.add(book2);

        BulkRequest bulkRequest = new BulkRequest();
        for (Book book : bookList) {
            String s = JSON.toJSONString(book);
            IndexRequest indexRequest = new IndexRequest("book").id(book.getId().toString()).source(s, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        try {
            BulkResponse bulkResponses = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            for (BulkItemResponse bulkResponse : bulkResponses) {
                System.out.println(bulkResponse.getResponse());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/search")
    public AjaxResult search() {
        SearchRequest searchRequest = new SearchRequest("book");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("name","测试"))
                //查询book内部user对象的字段使用nested嵌套查询，scoreMode用于计算根文档的分数策略
                //.query(QueryBuilders.nestedQuery("user",QueryBuilders.matchAllQuery(), ScoreMode.Avg))
                .postFilter(QueryBuilders.matchAllQuery())
                .sort("id", SortOrder.DESC)
                .from(0)
                .size(2)
                .highlighter(new HighlightBuilder().field("*").requireFieldMatch(false).preTags("<span style = 'color:red'>").postTags("</span>"));
//        布尔查询
//        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
//        boolQueryBuilder.must(QueryBuilders.matchAllQuery());
//        boolQueryBuilder.filter();
//        searchSourceBuilder.query(boolQueryBuilder);

//        字段高亮
//        HighlightBuilder highlightBuilder = new HighlightBuilder();
//        HighlightBuilder.Field highlightName = new HighlightBuilder.Field("name");
//        highlightBuilder.field(highlightName).requireFieldMatch(false).preTags("<span style = 'color:red'>").postTags("</span>");
//        HighlightBuilder.Field highlightId = new HighlightBuilder.Field("id");
//        highlightBuilder.field(highlightId).requireFieldMatch(false).preTags("<span style = 'color:red'>").postTags("</span>");
//        searchSourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = search.getHits().getHits();
            for (SearchHit hit : hits) {
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                if (highlightFields.containsKey("name")) {
                    System.out.println(highlightFields.get("name"));
                    System.out.println(highlightFields.get("name").fragments()[0].toString());
                }
                if (highlightFields.containsKey("text")) {
                    System.out.println(highlightFields.get("text"));
                    System.out.println(highlightFields.get("text").fragments()[0].toString());
                }
            }
            return AjaxResult.success(search);
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.fail("");
        }
    }

}
