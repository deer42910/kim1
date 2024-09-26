package com.hmall.item.es;

import cn.hutool.json.JSONUtil;
import com.hmall.item.domain.po.ItemDoc;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author:kim
 * @Description: TODO
 * @DateTime: 2024/9/19 11:32
 **/
@SpringBootTest(properties = "spring.profiles.active=local")
public class ElasticSearchTest {

    private RestHighLevelClient client;

    /**
     * 复合查询
     * @throws IOException
     */
    @Test
    void testMatchAll() throws IOException {
        //1.创建request对象
        SearchRequest request = new SearchRequest("items");
        //2.组织DSL参数
        request.source().query(
                QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery("name","脱脂牛奶"))
                        .filter(QueryBuilders.termQuery("brand.keyword","德亚"))
                        .filter(QueryBuilders.rangeQuery("price").lt(30000))
        );
        //3.发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //4.解析结果
        parseResponseResult(response);
    }

    /**
     * 分页与排序
     * @throws IOException
     */
    @Test
    void testSortAndPage() throws IOException {
        //0.前端传递的分页参数(模拟)
        int pageNo = 1, pageSize = 5;

        //1.创建request对象
        SearchRequest request = new SearchRequest("items");
        //2.组织DSL参数
        //2.1查询条件
        request.source().query(QueryBuilders.matchAllQuery());
        //2.2分页
        request.source().from((pageNo-1)*pageSize).size(pageSize);
        //2.3排序
        request.source().sort("sold", SortOrder.DESC)
                .sort("price", SortOrder.ASC);
        //3.发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //4.解析结果
        parseResponseResult(response);
    }

    /**
     * 高亮 得到请求，注意：响应结果要变为高亮结果
     * @throws IOException
     */
    @Test
    void testHighlight() throws IOException {
        //1.创建request对象
        SearchRequest request = new SearchRequest("items");
        //2.组织DSL参数
        //2.1query查询
        request.source().query(QueryBuilders.matchQuery("name","脱脂牛奶"));
        //2.2高亮条件
        request.source().highlighter(new HighlightBuilder().field("name").preTags("<em>").postTags("</em>"));
        //request.source().highlighter(SearchSourceBuilder.highlight().field("name").preTags("<em>").postTags("</em>"));
        //两者相同 SearchSourceBuilder点进去就是new HighlightBuilder()
        //3.发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //4.解析结果
        parseResponseResult(response);
    }

    /**
     * 聚合
     */
    @Test
    void testAgg() throws IOException {
        //1.创建request对象
        SearchRequest request = new SearchRequest("items");
        //2.组织DSL参数
        //2.1分页
        String brandAggName = "brandAgg";
        //2.2聚合条件
        request.source().aggregation(
                AggregationBuilders.terms(brandAggName).field("brand.keyword").size(10)
        );

        //发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        //解析结果
        //4.1拿到聚合结果
        Aggregations aggregations = response.getAggregations();
        //4.2根据聚合名称获取对应的聚合
        Terms brandTerms = aggregations.get(brandAggName);
        //4.3获取buckets桶    注意：Aggregation是父接口，找到子接口MultiBucketsAggregation中的Terms
        List<? extends Terms.Bucket> buckets = brandTerms.getBuckets();
        //4.4遍历获取每一个bucket
        for(Terms.Bucket bucket:buckets){
            System.out.println("品牌brand："+bucket.getKeyAsString());
            System.out.println("count："+bucket.getDocCount());
            //TODO:将数据放入集合，传给前端页面
        }
    }
    /**
     * 响应结果方法
     *   有高亮 输出高亮 无高亮输出ItemDoc
     * @param searchResponse
     */
    private static void parseResponseResult(SearchResponse searchResponse) {
        //解析结果
        SearchHits searchHits = searchResponse.getHits();
        //4.1总条数
        long total = searchHits.getTotalHits().value;
        //4.2命中的数据
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit:hits){
            //4.2.1获取source的数据
            String source = hit.getSourceAsString();
            //4.2.2json转化为ItemDoc
            ItemDoc doc = JSONUtil.toBean(source, ItemDoc.class);
            //4.3处理高亮结果
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightFields!=null&&!highlightFields.isEmpty()){
                //4.3.1根据高亮字段名获取高亮结果
                HighlightField hf = highlightFields.get("name");
                //4.3.2获取高亮结果，覆盖非高亮结果
                String hfname = hf.getFragments()[0].string();  //只有一个片段，若有多个进行拼接，具体看评论区
                doc.setName(hfname);
            }
            System.out.println(doc);
        }
    }

    /**
     * 发送请求
     * 解析结果
     * @throws IOException
     */
    @Test
    void testSearch() throws IOException {
        //1.创建request对象
        SearchRequest request = new SearchRequest("items");
        //2.配置request参数
        request.source()
                .query(QueryBuilders.matchAllQuery());  //查询所有文档
        //3.发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //得到一个大的json结构
        //4.解析结果
        SearchHits searchHits = response.getHits();
        //4.1查询总条数
        long total = searchHits.getTotalHits().value;
        System.out.println("总条数："+total);
        //4.2命中的数据
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit:hits){
            //4.2.1获取source信息
            String json = hit.getSourceAsString();
            //4.2.2转换为ItemDoc对象
            ItemDoc doc = JSONUtil.toBean(json, ItemDoc.class);
            System.out.println(doc);
        }
    }
    @BeforeEach
    void Setup(){  //初始化
        client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.230.132:9200")
                //HttpHost.create("http://localhost:9200"),
                //HttpHost.create("http://localhost:9200")
        ));
    }

    @AfterEach
    void tearDown() throws IOException {
        if (client != null){
            client.close();
        }
    }

    //将创建语句体设置成一个静态常量
    private static final String MAPPING_TEMPLATE = "{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"id\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"name\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_smart\"\n" +
            "      },\n" +
            "      \"price\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"image\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"category\":{      \n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"brand\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"sold\":{          \n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"commentCount\":{\n" +
            "        \"type\": \"integer\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"isAD\":{\n" +
            "        \"type\": \"boolean\"\n" +
            "      },\n" +
            "      \"updateTime\":{\n" +
            "        \"type\": \"date\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";
}
