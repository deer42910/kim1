package com.kim.utils;

import com.alibaba.druid.util.StringUtils;
import io.jsonwebtoken.*;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author:kim
 * @Description: TODO
 * @DateTime: 2024/9/5 14:32
 **/
@Data
@Component
@ConfigurationProperties(prefix = "jwt.token")
//将配置属性绑定到 Java Bean 的注解  application.yaml中的前缀
public class JwtHelper {
    private  long tokenExpiration; //有效时间,单位毫秒 1000毫秒 == 1秒
    private  String tokenSignKey;  //当前程序签名秘钥

    //生成token字符串
    public  String createToken(Long userId) {
        System.out.println("tokenExpiration = " + tokenExpiration);
        System.out.println("tokenSignKey = " + tokenSignKey);
        String token = Jwts.builder()   //Jwts.builder() 是 JWT 生成库（如 JJWT）的一个方法，用于开始构建 JWT
                .setSubject("YYGH-USER")  //设置主题：用于标识令牌的主体或用户的身份
                .setExpiration(new Date(System.currentTimeMillis()+tokenExpiration*1000*60))
                //（最终）单位分钟 ：系统当前毫秒值+自己设置的毫秒值（tokenExpiration分钟*1000*60）
                .claim("userId",userId)//自定义的声明:可在令牌中携带用户的唯一标识符
                .signWith(SignatureAlgorithm.HS512,tokenSignKey)
                //用于jwt签名，以保证令牌的完整性和真实性
                //SignatureAlgorithm.ES512 签名算法  HMAC SHA-512  //tokenSignKey自定义安全密钥：一般是随机字符串
                .compressWith(CompressionCodecs.GZIP)
                //用于对JWT进行压缩，以减少令牌的大小 使用GZIP算法进行压缩
                .compact();//方法完成 JWT 的生成，并将其转换为一个字符串。这个字符串就是最终的 JWT，可以用于传递和验证。
        return token;
    }

    //从token字符串获取userid
    public  Long getUserId(String token) {
        if(StringUtils.isEmpty(token)) return null;
        //Jwts.parser() 创建一个 JWT 解析器。
        //setSigningKey(tokenSignKey) 设置用于验证 JWT 签名的密钥。这个密钥必须与生成 JWT 时使用的密钥匹配。
        //parseClaimsJws(token) 解析 JWT，并验证其签名，返回一个 Jws<Claims> 对象，包含 JWT 的声明（Claims）部分。
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();//getBody() 方法从 Jws 对象中提取 Claims 对象
        Integer userId = (Integer)claims.get("userId");//获取用户id
        return userId.longValue();  //转换为Long类型
    }
   /* //从token获取userid
    public Long getUserId(String token){
        if (StringUtils.isEmpty(token)) return null;
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        Integer userId = (Integer) claims.get("userId");
        return userId.longValue();
    }*/
    //判断token是否有效
    public boolean isExpiration(String token){
      try{
          boolean isExpire = Jwts.parser()
                  .setSigningKey(tokenSignKey)
                  .parseClaimsJws(token)
                  .getBody()
                  .getExpiration().before(new Date());
          //没有过期，有效，返回false
          return isExpire;
      }catch (Exception e){
          //过期出现异常，返回true
          return true;
      }

    }
}
