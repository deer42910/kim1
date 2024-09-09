package com.kim.controller;

import com.kim.pojo.Headline;
import com.kim.service.HeadlineService;
import com.kim.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author:kim
 * @Description: TODO
 * @DateTime: 2024/9/6 22:03
 **/
@RestController
@RequestMapping("headline")
@CrossOrigin
public class HeadLineController {
    @Autowired
    private HeadlineService headlineService;

    /**
     * 发布头条
     * TODO：前提是登录以后才可以访问    登录以后所有的都要验证userid的有效性 所以用拦截器
     * @param headline
     * @param token
     * @return
     */
    @PostMapping("publish")
    public Result push(@RequestBody Headline headline,@RequestHeader String token){
        Result result = headlineService.publish(headline,token);
        return result;
    }

    @PostMapping("findHeadlineByHid")
    public Result findHeadlineByHid(Integer hid){
        Headline headline = headlineService.getById(hid);
        Map data = new HashMap();
        data.put("headline", headline);
        return Result.ok(data);
    }

    @PostMapping("update")
    public Result update(@RequestBody Headline headline){
        Result result = headlineService.updateData(headline);
        return result;
    }

    /**
     * 删除与上面通过id查询头条的一样，单一操作，可以从业务层实现  业务删除也就是逻辑删除（数据库中也删除）
     * @param hid
     * @return
     */
    @PostMapping("removeByHid")
    public Result removeByHid(Integer hid){
        headlineService.removeById(hid);
        return Result.ok(null);
    }
}
