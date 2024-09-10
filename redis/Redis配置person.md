Redis

设置了自启动

注意自己的redis-server 与 redis-6.2.6在不同的目录下 usr 与 root

redis.conf配置文件 vi redis.conf(在redis-6.2.6目录下)

```
# 进入redis安装目录 
cd /root/local/src/redis-6.2.6
#vi redis.conf
# 允许访问的地址，默认是127.0.0.1，会导致只能在本地访问。修改为0.0.0.0则可以在任意IP访问，生产环境不要设置为0.0.0.0
bind 0.0.0.0
# 守护进程，修改为yes后即可后台运行
daemonize yes 
# 密码，设置后访问Redis必须输入密码
requirepass 123321
```

**Redis命令行客户端**

```
#redis-server的地址
cd /usr/local/bin
```

```
# 启动
systemctl start redis
# 停止
systemctl stop redis
# 重启
systemctl restart redis
# 查看状态
systemctl status redis
```

Redis安装完成后就自带了命令行客户端：redis-cli，使用方式如下：

```sh
redis-cli [options] [commonds]
```

其中常见的options有：

- `-h 127.0.0.1`：指定要连接的redis节点的IP地址，默认是127.0.0.1
- `-p 6379`：指定要连接的redis节点的端口，默认是6379
- `-a 123321`：指定redis的访问密码 

其中的commonds就是Redis的操作命令，例如：

- `ping`：与redis服务端做心跳测试，服务端正常会返回`pong`

不指定commond时，会进入`redis-cli`的交互控制台：

![image-20240828152112943](D:\a_briup_learn\redis\Redis配置person.assets\image-20240828152112943.png)

[resp无法连接Redis服务的解决方法_resp连接redis不成功-CSDN博客](https://blog.csdn.net/m0_60861848/article/details/129455739)

```
#直接关闭防火墙
systemctl stop firewalld.service
```

![image-20240910131753291](D:\a_briup_learn\redis\Redis配置person.assets\image-20240910131753291.png)

**注意：一定要管防火墙**
