package com.hmall.item.es;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @Author:kim
 * @Description: TODO
 * @DateTime: 2024/9/19 11:32
 **/
public class ElasticIndexTest {
    private RestHighLevelClient client;

    @Test
    void testConnection(){
        System.out.println("client="+client);
    }

    /**
     * 创建索引库
     * @throws IOException
     */
    @Test
    void testCreateIndex() throws IOException {
        //1.准备Request对象
        CreateIndexRequest request = new CreateIndexRequest("items");
        //2.准备请求数据
        request.source(MAPPING_TEMPLATE, XContentType.JSON);
        //3.发起请求
        client.indices().create(request, RequestOptions.DEFAULT);  //indices是index的复数形式 请求可选项：默认就行
    }

    /**
     * 查询索引库
     * @throws IOException
     */
    @Test
    void testGetIndex() throws IOException {
        //1.准备Request对象
        GetIndexRequest request = new GetIndexRequest("items");
        //2.查询 不需要请求体
        //3.发起请求
        //client.indices().get(request, RequestOptions.DEFAULT);  //indices是index的复数形式 请求可选项：默认就行
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println("索引库是否存在："+exists);
    }

    /**
     * 删除索引库
     * @throws IOException
     */
    @Test
    void testDeleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("items");
        client.indices().delete(request, RequestOptions.DEFAULT);
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
