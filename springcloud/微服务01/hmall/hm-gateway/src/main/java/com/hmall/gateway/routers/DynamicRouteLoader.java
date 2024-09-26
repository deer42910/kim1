package com.hmall.gateway.routers;

import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.hmall.common.utils.CollUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * @Author:kim
 * @Description: 动态路由
 * @DateTime: 2024/9/17 20:36
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicRouteLoader {

    private final NacosConfigManager nacosConfigManager;

    private final RouteDefinitionWriter routeDefinitionWriter;

    //定义一个容器，用来记录路由id  保存更新过的路由id
    private final Set<String> routeIds = new HashSet<>();

    //路由配置文件的id和分组
    private final String dataId = "gateway-routes.json";
    private final String group = "DEFAULT_GROUP";
    /**
     * 初始化路由配置监听器
     */
    @PostConstruct //在Bean初始化之后进行
    public void initRouteConfigListener() throws NacosException {
        //1.项目启动时，先拉取一段配置，并且添加配置监听器
        String configInfo = nacosConfigManager.getConfigService().getConfigAndSignListener(dataId, group, 5000, new Listener() {
            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                //2.监听到配置变更，需要去更新路由表
                updateConfigInfo(configInfo);
            }
        });
        //3.首次启动时，第一个读取到配置，也需要更新到路由表
        updateConfigInfo(configInfo);
    }
    public void updateConfigInfo(String configInfo){
        log.debug("监听到路由信息:"+configInfo);
        //1.解析配置信息，转为RouteDefinition  （反序列化）
        List<RouteDefinition> routeDefinitions = JSONUtil.toList(configInfo, RouteDefinition.class);
        //2.更新前先清空旧路由
        // 2.1删除旧的路由表
        for (String routeId : routeIds) {
            routeDefinitionWriter.delete(Mono.just(routeId)).subscribe();
        }
        routeIds.clear();
        //2.2判断是否有新的路由要更新
        if (CollUtils.isEmpty(routeDefinitions)){
            //无新路由配置，直接结束
            return;
        }
        //3.更新路由表
        for (RouteDefinition routeDefinition : routeDefinitions) {
            //3.1更新路由表
            routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
            //3.2记录路由id,便于下一次更新时删除
            routeIds.add(routeDefinition.getId());
        }
    }
}
