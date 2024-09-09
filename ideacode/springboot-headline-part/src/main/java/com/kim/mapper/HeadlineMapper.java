package com.kim.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kim.pojo.Headline;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kim.pojo.PortalVo;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
* @author ASUS
* @description 针对表【news_headline】的数据库操作Mapper
* @createDate 2024-09-04 23:55:54
* @Entity com.kim.pojo.Headline
*/
public interface HeadlineMapper extends BaseMapper<Headline> {

    IPage<Map> selectPageMap(IPage<Headline> page, @Param("portalVo") PortalVo portalVo);

    Map queryDetailMap(Integer hid);
}




