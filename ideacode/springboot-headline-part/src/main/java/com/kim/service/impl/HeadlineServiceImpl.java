package com.kim.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kim.pojo.Headline;
import com.kim.pojo.PortalVo;
import com.kim.service.HeadlineService;
import com.kim.mapper.HeadlineMapper;
import com.kim.utils.JwtHelper;
import com.kim.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
* @author ASUS
* @description 针对表【news_headline】的数据库操作Service实现
* @createDate 2024-09-04 23:55:54
*/
@Service
public class HeadlineServiceImpl extends ServiceImpl<HeadlineMapper, Headline>
    implements HeadlineService{

    @Autowired
    private HeadlineMapper headlineMapper;
    @Autowired
    private JwtHelper jwtHelper;

    /**
     * 根据条件搜索分页信息，返回含页码数，页大小，总页数，当前页数据
     * 根据时间降序，浏览量降序排序
     * @param portalVo
     * @return
     */
    @Override
    public Result findNewPage(PortalVo portalVo) {
        //条件拼接 需要非空判断
        LambdaQueryWrapper<Headline> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(portalVo.getKeyWords()),Headline::getTitle,portalVo.getKeyWords())
        .eq(portalVo.getType()!=null,Headline::getType,portalVo.getType());

        //2.分页参数 Page->当前页 页容量
        IPage<Headline> page = new Page<>(portalVo.getPageNum(), portalVo.getPageSize());

        //3.分页查询
        //查询结果"pastHours":"3"  //发布时间已过小时数，我们查询返回一个map
        //自定义方法
        headlineMapper.selectPageMap(page,portalVo);

        //结果封装
        //分页数据封装
        Map<String,Object> pageInfo = new HashMap<>();
        pageInfo.put("pageData",page.getRecords());
        pageInfo.put("pageNum",page.getCurrent());
        pageInfo.put("pageSize",page.getSize());
        pageInfo.put("totalPage",page.getPages());
        pageInfo.put("totalSize",page.getTotal());

        HashMap<String, Object> pageInfoMap = new HashMap<>();
        pageInfoMap.put("pageInfo",pageInfo);
        //响应json
        return Result.ok(pageInfoMap);
    }

    /**
     * 根据id查询用户详情。注意是多变查询 需要更新浏览量加一
     * 2.查询对应的数据即可【多表，头条和用户表，方法需要自定义返回map即可】
     * 1.修改阅读量 +1 【version乐观锁，当前数据对应的版本】
     *
     * @param hid
     * @return
     */
    @Override
    public Result showHeadlineDetail(Integer hid) {

        Map data = headlineMapper.queryDetailMap(hid);
        Map headlineMap = new HashMap();
        headlineMap.put("headline",data);

        //修改阅读量
        Headline headline = new Headline();
        headline.setHid((Integer) data.get("hid"));
        headline.setVersion((Integer) data.get("version"));
        //阅读量+1
        headline.setPageViews((Integer) data.get("pageViews")+1);
        headlineMapper.updateById(headline);
        return Result.ok(headlineMap);
    }

    /**
     * 发布头条的方法
     *
     * 1.补全数据
     *
     * @param headline
     * @return
     */
    @Override
    public Result publish(Headline headline,String token) {
        //根据token查询用户id

        int userId = jwtHelper.getUserId(token).intValue();
        //数据装配
        headline.setPublisher(userId);
        headline.setPageViews(0);
        headline.setCreateTime(new Date());
        headline.setUpdateTime(new Date());

        headlineMapper.insert(headline);
        return Result.ok(null);
    }

    /**
     * 修改头条数据
     * 1.hid查询数据的最新version
     * 2.修改数据的修改时间为当前结点
     * @param headline
     * @return
     */
    @Override
    public Result updateData(Headline headline) {
        Integer version = headlineMapper.selectById(headline.getHid()).getVersion();
        headline.setVersion(version);//乐观锁
        headline.setUpdateTime(new Date());
        headlineMapper.updateById(headline);
        return Result.ok(null);
    }
}




