# Docker

### 一、Docker介绍

#### 1.1引言

1. 本地应用没问题啊？

   > 环境不一致问题：自己的软件在别人电脑上不能运行

2. 那个哥们有写死循环了，怎么这么卡

   > 在多用户的操作系统下，会相互影响

3. 淘宝在双十一的时候，用户量暴增

   > 运维成本过高问题

4. 学习一门技术，学习成本过高

   关于安装软件成本过高

#### 1.2Docker的思想

1. 集装箱

   会将所有需要的内容放到不同的集装箱中，谁需要这些环境就直接拿到这个集装箱就可以了

2. 标准化

   1. 运输的标准化：Docker有一个码头，所有的集装箱都放在这个码头上，当谁需要某一个环境，就直接指派大海豚（Docker的logo）去搬运这个集装箱就可以了。
   2. 命令的标准化：docker提供了一些列的命令，帮助我们去获取集装箱的操作。
   3. 提供了REST的API:衍生出了很多的图形化界面，Rancher.

3. 隔离性

   Docker在运行集装箱内的内容时，会在Linux的内核中，单独开辟一片空间，这片空间不会影响到其他程序。

- 注册中心（超级码头，上面放的集装箱）
- 镜像，（集装箱）
- 容器，（运行起来的镜像）

## Docker概念

Docker是一个开源的应用容器引擎

Docker可以让开发者打包它们的应用以及依赖包到一个轻量级，可移植的容器中，然后发布到任何流行的Linux机器上。

容器是完全使用沙箱机制，相互隔离（一个集装箱装MySQL,一个装tomate）

![image-20240815070642381](D:\briup_study\笔记文档\Akimm\Docker.assets\image-20240815070642381.png)

## Docker命令

#### Docker进程相关命令

启动docker服务

```
systemctl start docker
```

停止

```
systemctl stop docker
```

重启

```
systemctl restart docker
```

查看docker状态

```
systemctl status docker
```

![image-20240815071348194](D:\a_briup_learn\微服务\Docker\Docker.assets\image-20240815071348194.png)

- 停止 （dead）

![image-20240815071436052](D:\a_briup_learn\微服务\Docker\Docker.assets\image-20240815071436052.png)

- 启动（running）

设置开机启动docker服务

```
systemctl enable docker
```

## 牛逼

安装vim

```
sudo yum install vim
```

```
vim --version
```

编辑Docker守护进程配置文件（打开json添加镜像加速配置）

```
sudo vim /etc/docker/daemon.json
```

所有镜像加速

```
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
    "registry-mirrors": [
        "https://do.nark.eu.org",
        "https://dc.j8.work",
        "https://docker.m.daocloud.io",
        "https://dockerproxy.com",
        "https://docker.mirrors.ustc.edu.cn",
        "https://docker.nju.edu.cn"
    ]
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker
```

#### Docker镜像相关命令

将远程仓库的命令下载到本地来

- 查看镜像

  ```  
  docker images  //镜像列表
  ```

  ![image-20240815071955638](D:\a_briup_learn\微服务\Docker\Docker.assets\image-20240815071955638.png)

  名称  版本  镜像id 几小时前  大小

- 搜索镜像

  ```
  docker search redis
  ```

  ```
  docker pull redis   //加版本号 不加是默认tag:latest
  ```

  寻找版本号 网址https://hub.docker.com/ 搜索下载配置名称

- 下载镜像

  ```
  docker pull redis:7.4   //加版本号
  ```

- 删除镜像

  ```
  docker rmi IMAGE ID
  ```

![image-20240815073742950](D:\briup_study\笔记文档\Akimm\Docker.assets\image-20240815073742950.png)



#### 容器相关命令

查看容器

```
docker ps    #查看真正运行的容器
docker ps -a #查看所有容器
```

创建容器

```
docker run 参数

[root@localhost ~]# docker run -it --name=ctest1 centos:7 /bin/bash
[root@4fdaf09c1307 /]# 
```

> 参数说明：
>
> -i:保持容器运行。通常与-t同时使用。加入it这两个参数后，容器创建后自动进入容器中，退出容器后，容器自动关闭。
>
> -t:为容器重写分配一个伪输入终端
>
> -d:为守护（后台）模式运行容器。创建一个容器在后台运行，需要使用docker exec进入容器，退出后，容器不会关闭。
>
> -it:创建的容器一般称为交互式容器，-id创建的容器一般称为守护式容器
>
> --name：为创建的容器命名

退出容器

```
exit
```

```
[root@4fdaf09c1307 /]# exit
exit
[root@localhost ~]#
```

查看当前正在运行的容器

```
docker ps    
docker ps -a //关闭的也显示
```

```
[root@localhost ~]# docker ps
CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES
```

```
[root@localhost ~]# docker ps -a
CONTAINER ID   IMAGE      COMMAND       CREATED          STATUS                       PORTS     NAMES
4fdaf09c1307   centos:7   "/bin/bash"   4 minutes ago    Exited (127) 2 minutes ago             ctest1
257dba80c963   centos:7   "/bin/dash"   12 minutes ago   Created                                c1
```

进入容器

```
docker exec 参数 #退出容器，容器不会关闭
```

```shell 
[root@localhost ~]# docker run -id --name=c2 centos:7
156d3cf9bbde6385dc1c98555d955c8bf03a48dd1216feb0ddbb3f8d4314689e
[root@localhost ~]# docker ps -a
CONTAINER ID   IMAGE      COMMAND       CREATED          STATUS                       PORTS     NAMES
156d3cf9bbde   centos:7   "/bin/bash"   9 seconds ago    Up 8 seconds                           c2
4fdaf09c1307   centos:7   "/bin/bash"   6 minutes ago    Exited (127) 4 minutes ago             ctest1
257dba80c963   centos:7   "/bin/dash"   13 minutes ago   Created                                c1

[root@localhost ~]# docker exec -it c2 /bin/bash
[root@156d3cf9bbde /]#
[root@156d3cf9bbde /]# exit
exit
[root@localhost ~]# docker ps -a
CONTAINER ID   IMAGE      COMMAND       CREATED          STATUS                       PORTS     NAMES
156d3cf9bbde   centos:7   "/bin/bash"   2 minutes ago    Up 2 minutes                           c2
4fdaf09c1307   centos:7   "/bin/bash"   8 minutes ago    Exited (127) 6 minutes ago             ctest1
257dba80c963   centos:7   "/bin/dash"   15 minutes ago   Created                                c1
[root@localhost ~]#
#-id 创建的容器不会自动关闭 Up
```

启动容器

```
docker start 容器名称
```

停止容器

```
docker stop 容器名称
```

```
[root@localhost ~]# docker stop c2
c2
#反应比较慢
```

删除容器：若容器是运行状态则删除失败，需要停止容器才能删除

```
docker rm 容器名称
docker rm `docker ps -aq`#删除所有容器
```

```
[root@localhost ~]# docker rm c1
c1
[root@localhost ~]# docker rm `docker ps -aq`
156d3cf9bbde
4fdaf09c1307
[root@localhost ~]# docker ps -a
CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES
[root@localhost ~]#
```

```
[root@localhost ~]# docker rm c1 c2 c3 c4 c5
c1
c2
c3
c4
c5
```

查看容器信息

```
docker inspect 容器名称
```

## Decoker容器的数据卷

> 挂载：绑定
>
> Docker容器会和宿主机交换信息，而不会和容器。

![image-20240816061653624](D:\a_briup_learn\微服务\Docker\Docker.assets\image-20240816061653624.png)

#### 配置数据卷

创建启动容器时，使用-v参数 设置数据卷

```
docker run ... -v 宿主机目录(文件):容器内目录(文件)...
```

> 注意：
>
> 1. 目录必须是绝对路径
> 2. 若目录不存在，会自动创建
> 3. 可以挂载多个数据卷

```
[root@localhost ~]# docker run -it --name=c2 -v /root/data:/root/data_container centos:7 /bin/bash
[root@49b74f1e2733 /]# ~
bash: /root: Is a directory
[root@49b74f1e2733 /]# cd ~
[root@49b74f1e2733 ~]# ll  
total 4
-rw-------. 1 root root 3416 Nov 13  2020 anaconda-ks.cfg
drwxr-xr-x. 2 root root    6 Aug 15 22:26 data_container
```

注意：不要少写斜杠

![image-20240816065124012](D:\a_briup_learn\微服务\Docker\Docker.assets\image-20240816065124012.png)

![image-20240816070008477](D:\a_briup_learn\微服务\Docker\Docker.assets\image-20240816070008477.png)

一个宿主机（数据卷）可以挂载多个容器

![image-20240816070351400](D:\a_briup_learn\微服务\Docker\Docker.assets\image-20240816070351400.png)

一个容器挂载两个容器，容积间进行交互

![image-20240816072603762](D:\a_briup_learn\微服务\Docker\Docker.assets\image-20240816072603762.png)

#### 数据卷容器

![image-20240816073157532](D:\a_briup_learn\微服务\Docker\Docker.assets\image-20240816073157532.png)

##### 配置数据卷容器

> 相当于c1 c2 c3 同时挂到了一个数据卷上面

1. 创建启动c3数据卷容器，使用-v参数 设置数据卷

   ```
   docker run -it --name=c3 -v /volume centos:7 /bin/dash
   ```

2. 创建启动c1 c2容器，使用--volumes-from参数 设置数据卷

   ```
   docker run -it --name=c1 --volumes-from c3 centos:7 /bin/dash
   docker run -it --name=c2 --volumes-from c3 centos:7 /bin/dash
   ```

![image-20240816075648673](D:\a_briup_learn\微服务\Docker\Docker.assets\image-20240816075648673.png)

#### 小结

![image-20240816075155195](D:\a_briup_learn\微服务\Docker\Docker.assets\image-20240816075155195.png)





















not in the sudoers file. this incident will be reported

这个错误消息表明你的用户没有权限使用 `sudo` 命令。你需要以具有管理员权限的用户身份来执行命令。

> 1. **切换到 root 用户**
>
> 2. ```
>    su -
>    ```