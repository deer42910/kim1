package com.hmall.item.es;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmall.common.utils.CollUtils;
import com.hmall.item.domain.po.Item;
import com.hmall.item.domain.po.ItemDoc;
import com.hmall.item.service.IItemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

/**
 * @Author:kim
 * @Description: TODO
 * @DateTime: 2024/9/19 11:32
 **/
@Slf4j
@SpringBootTest(properties = "spring.profiles.active=local")
public class ElasticDocumentTest {
    private RestHighLevelClient client;

    @Autowired
    private IItemService itemService;

    /**
     * 新增文档
     * @throws IOException
     */
    @Test
    void testIndexDoc() throws IOException {
        //0.准备文档数据
        //0.1根据id查询数据库数据
        Item item = itemService.getById(561178L);
        //0.2把数据库数据转为文档数据  利用hutool工具，注意不要加s
        ItemDoc itemDoc = BeanUtil.copyProperties(item, ItemDoc.class);

        //修改
        //全局更新，再次写入id一样的文档，就会删除旧文档，添加新文档，与新增的javaAPI一致
        itemDoc.setPrice(19990);

        //1.准备request
        IndexRequest request = new IndexRequest("items").id(itemDoc.getId());
        //关于id（）中必须传String类型，所以item.getId().toString() 也可以是itemDoc.getId()
        //2.请求参数
        request.source(JSONUtil.toJsonStr(itemDoc),XContentType.JSON);
        //3.发送请求
        client.index(request,RequestOptions.DEFAULT);
    }

    /**
     * 查询文档
     * @throws IOException
     */
    @Test
    void testGetIndexDoc() throws IOException {
        //1.创建request对象
        GetRequest request = new GetRequest("items","561178");
        //2.发送请求
        GetResponse response = client.get(request, RequestOptions.DEFAULT);//查询后显示响应信息
        //3.解析响应结果  Source中
        String json = response.getSourceAsString();
        ItemDoc doc = JSONUtil.toBean(json, ItemDoc.class);  //将json的字符串转换为java类型
        System.out.println(doc);
    }

    /**
     * 删除文档
     * @throws IOException
     */
    @Test
    void testDeleteDoc() throws IOException {
        //1.创建request对象
        DeleteRequest request = new DeleteRequest("items","561178");
        //2.发送请求
        client.delete(request, RequestOptions.DEFAULT);
    }

    @Test
    void testUpdateDoc() throws IOException {
        //1.创建request对象
        UpdateRequest request = new UpdateRequest("items","561178");
        //2.准备请求参数
        request.doc(
                "price","21324"
                //"字段名","字段值"
        );
        //2.发送请求
        client.update(request, RequestOptions.DEFAULT);
    }

    @Test
    void testBulkDoc() throws IOException {
        int pageNo = 1;
        int pageSize = 500;
        while(true){
        //0.准备文档数据
        Page<Item> page = itemService.lambdaQuery().eq(Item::getStatus, 1).page(new Page<Item>(pageNo,pageSize));
        //非空校验
        List<Item> items = page.getRecords();
        if (CollUtils.isEmpty(items)){
            return;
        }
        log.info("加载第{}页数据，共{}条", pageNo, items.size());
        //1.准备request
        BulkRequest request = new BulkRequest();
        //2.准备请求参数  批量操作在这里进行
        for(Item item:items){
            request.add(new IndexRequest("items")
                    .id(item.getId().toString())
                    .source(JSONUtil.toJsonStr(BeanUtil.copyProperties(item, ItemDoc.class)),XContentType.JSON));
        }
        //3.发送请求
        client.bulk(request,RequestOptions.DEFAULT);

        //5.翻页
        pageNo++;
        }
    }

    @BeforeEach
    void Setup(){  //初始化
        client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.230.132:9200")
        ));
    }

    @AfterEach
    void tearDown() throws IOException {
        if (client != null){
            client.close();
        }
    }

}
