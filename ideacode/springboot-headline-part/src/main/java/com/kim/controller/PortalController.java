package com.kim.controller;

import com.kim.pojo.PortalVo;
import com.kim.pojo.Type;
import com.kim.service.HeadlineService;
import com.kim.service.TypeService;
import com.kim.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author:kim
 * @Description: TODO
 * @DateTime: 2024/9/6 16:18
 **/

/**
 * @CrossOrigin 的作用
 * 允许跨域请求: @CrossOrigin 注解用于指定哪些来源的请求可以访问你的后端资源。它帮助服务器允许或限制特定的源对服务器资源的访问。
 *
 * 配置跨域策略: 通过 @CrossOrigin 注解，你可以配置允许的 HTTP 方法、请求头、响应头等。
 */
@RestController
@RequestMapping("portal")
@CrossOrigin
//用于处理跨域资源共享(CORS)问题  CORS用于允许或限制不同源(域名、协议、端口)之间的资源共享。
public class PortalController {
    @Autowired
    private TypeService typeService;
    @Autowired
    private HeadlineService headlineService;

    /**
     * 查询全部类别信息
     * @return
     */
    @GetMapping("findAllTypes")
    public Result findAllTypes(){
        //直接调取业务层，查询全部数据
        List<Type> list = typeService.list();
        return Result.ok(list);
    }

    /**
     * 首页分页查询
     * @param portalVo
     * @return
     */
    @PostMapping("findNewsPage")
    public Result findNewPage(@RequestBody PortalVo portalVo){
        Result result =headlineService.findNewPage(portalVo);
        return result;
    }

    @PostMapping("showHeadlineDetail")
    public Result showHeadlineDetail(Integer hid){
        Result result = headlineService.showHeadlineDetail(hid);
        return result;
    }
}
