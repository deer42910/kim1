package com.kim.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kim.pojo.Type;
import com.kim.service.TypeService;
import com.kim.mapper.TypeMapper;
import org.springframework.stereotype.Service;

/**
* @author ASUS
* @description 针对表【news_type】的数据库操作Service实现
* @createDate 2024-09-04 23:55:53
*/
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type>
    implements TypeService{

}




