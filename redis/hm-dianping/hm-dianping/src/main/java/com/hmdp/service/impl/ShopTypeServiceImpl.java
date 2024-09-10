package com.hmdp.service.impl;

import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.hmdp.utils.RedisConstants.CACHE_SHOP_TYPE_KEY;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryTypeList() {
        //1.从redis查询店铺类型的缓存
        List<String> lists = stringRedisTemplate.opsForList().range(CACHE_SHOP_TYPE_KEY, 0, -1);//获取list集合中所有的元素（json字符串）
        ArrayList<ShopType> typeList = new ArrayList<>(); //用来存ShopType对象

        // 2、判断是缓存是否命中
        if(!lists.isEmpty()){
            //命中，直接返回 （返回的一定是对象，所以需要将json字符串转换，遍历一下，逐一转换，然后存到我们上面定义的typeList中）
            for (String list : lists) {
                ShopType shopType = JSONUtil.toBean(list, ShopType.class);
                typeList.add(shopType);
            }
            return Result.ok(typeList);
        }
        //注意到这个地方：cache是空的
        //4.缓存未命中，数据库查list
        List<ShopType> shopTypeList = query().orderByAsc("sort").list();//数据库中
        if(shopTypeList.isEmpty()){
            //5.不存在，报错
            return Result.fail("不存在该分类！");
        }
        //6.存在，将数据放入缓存  数据库中查询到的list集合是对象，要存入到cache需要转换为JSON
        for (ShopType shopType : shopTypeList) {
            String jsonStr = JSONUtil.toJsonStr(shopType);
            lists.add(jsonStr);
        }
        //存入缓存
        stringRedisTemplate.opsForList().rightPushAll(CACHE_SHOP_TYPE_KEY,lists);

        //7.返回数据
        return Result.ok(shopTypeList);
    }
}
