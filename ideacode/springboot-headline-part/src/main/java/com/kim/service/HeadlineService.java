package com.kim.service;

import com.kim.pojo.Headline;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kim.pojo.PortalVo;
import com.kim.utils.Result;

/**
* @author ASUS
* @description 针对表【news_headline】的数据库操作Service
* @createDate 2024-09-04 23:55:54
*/
public interface HeadlineService extends IService<Headline> {

    Result findNewPage(PortalVo portalVo);

    //显示头条详情
    Result showHeadlineDetail(Integer hid);

    //发布头条方法
    Result publish(Headline headline,String token);

    //修改头条数据
    Result updateData(Headline headline);
}
