package com.kim.pojo;

import lombok.Data;

/**
 * @Author:kim
 * @Description: TODO
 * @DateTime: 2024/9/6 16:32
 **/
@Data
public class PortalVo {
    private String keyWords;
    private Integer type;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
