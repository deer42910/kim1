# Linux

**常见英文**

Enterprise企业  RHEL(Red Hot Enterprise Linux)红帽企业

**操作系统**

> 操作系统`Operating System`简称`OS`，是软件的一部分，它是硬件基础上的第一层软件，是硬件和其它软件沟通的桥梁。

**什么是 Linux**

**Linux 系统内核与 Linux 发行套件的区别**

> - `Linux`系统内核指的是由`Linus Torvalds`负责维护，提供硬件抽象层、硬盘及文件系统控制及多任务功能的系统核心程序。
> - `Linux`发行套件系统是我们常说的 `Linux`操作系统，也即是由 `Linux`内核与各种常用软件的集合产品。

**总结:真正的** `Linux`**指的是系统内核，而我们常说的** `Linux`**指的是 “发行版完整的包含一些基础软件的操作系统”。**

**Linux 系统种类**

- 红帽企业版 Linux：RHEL是全世界内使用最广泛的 Linux系统。它具有极强的性能与稳定性，是众多生成环境中使用的（收费的）系统。
- Fedora ：由红帽公司发布的桌面版系统套件，用户可以免费体验到最新的技术或工具，这些技术或工具在成熟后会被加入到RHEL系统中，因此 Fedora也成为RHEL系统的试验版本。

- CentOS ：通过把RHEL系统重新编译并发布给用户免费使用的 Linux系统，具有广泛的使用人群。


- Deepin ：中国发行，对优秀的开源成品进行集成和配置。


- Debian ：稳定性、安全性强，提供了免费的基础支持，在国外拥有很高的认可度和使用率。


- Ubuntu ：是一款派生自Debian的操作系统，对新款硬件具有极强的兼容能力。Ubuntu与Fedora都是极其出色的 Linux桌面系统，而且 Ubuntu也可用于服务器领域。

![image-20240815090148889](D:\a_briup_learn\微服务\Linux\Linux.assets\image-20240815090148889.png)

## 文件和路径

![image-20240815090238047](D:\a_briup_learn\微服务\Linux\Linux.assets\image-20240815090238047.png)

**远程连接操作**

通过[远程连接工具]连接到服务器的操作即为远程连接

> 条件: 
>
> 1> 服务器的 IP 地址
>
> 2> 服务器的账号和密码 
>
> 注意: 必须要确保有⽹络连接条件

## Linux命令

**操作技巧**

```
1. 善⽤ tab 键进⾏⽂件名/路径名/命令的⾃动补全! 
2.  可以使⽤上/下⽅向键, 快速获取之前输⼊过的命令 
3. 如果命令开始执⾏后, 停不下来, 或要取消执⾏, 可以使⽤快捷键: Ctrl + C
```

```
# 命令 [-选项] [参数]
command [-options] [parameter]
说明:
 command : 命令主体
 [-options] : 命令选项(可选), 根据命令可以显示更加丰富的数据
 [parameter] : 命令参数(可选), 命令的操作对象，⼀般⽂件、⽬录、⽤户
和进程等都可以作为参数被命令操作
```

```
pwd 查看当前路径 
ls 查看当前目录下有哪些文件  (list)
mkdir adir 创建文件夹 (makedirectory创建目录）
mkdir adir,bdir,cdir 创建三个文件夹adir bdir cdir
cd adir 切换到adir目录下  (c进入 dir目录)
touch aa  创建一个文件
注意：文件夹名称就文件名需要和命令之间使用 空格 隔开
```

注意：创建文件夹是mkdir  创建文件是touch

cd的其他用法和mv

```java
说明: 在 Linux 系统中, 可以根据如下特点区分相对路径和绝对路径
相对路径: 凡是路径信息以 '.' 或 '..' 开头的均为相对路径
绝对路径: 凡是路径信息以 '/' 或 '~' 开头的均为绝对路径
注释:
 . : 当前路径下(⼀般会省略)
 .. : 上⼀层⽬录
 / : 根⽬录
 ~ : 当前⽤户的家⽬录  用户为root，~：/roor 用户为admin，~：/home/admin
```

```
# cd /home/admin/  使用绝对路径切换到admin目录下 
#pwd 
/home/admin  
# cd .   使用相对路径切换到root目录下
#pwd
/root
```

注意：mv(move)命令默认是用来移动文件到某一路径下的

但是如果目标文件不是文件夹名称或文件名不存在，即为修改文件名操作

![image-20240815112007268](D:\a_briup_learn\微服务\Linux\Linux.assets\image-20240815112007268.png)

> mv aa file 后面的覆盖前面的

**CP(copy)复制文件和文件夹**

将一个文件夹下的文件复制到另一个文件夹下

```
[root@localhost ~]# cp -i adir/aa bdir
[root@localhost ~]# cd bdir
[root@localhost bdir]# ls
aa  bb
[root@localhost bdir]#
```

> 将adir中的aa文件 复制到 bdir中  

![image-20240815184657874](D:\a_briup_learn\微服务\Linux\Linux.assets\image-20240815184657874.png)

> 将adir文件覆盖aa文件  后面的覆盖前面的
>

```
//注意：若目标文件是文件夹，需要使用-r选项。否则无法复制
[root@localhost ]# cp bdir/ cdir/
cp: 略过目录"bdir/"
[root@localhost ~]# cp -r bdir/ cdir/
[root@localhost ~]# cd bdir
[root@localhost bdir]# ls
bb
//将文件夹bdir 复制到 cdir下 
```

**mv/touch创建多个文件/rm删除文件和文件夹及所有文件**

```
[root@localhost ~]# mv bdir/bb cdir/cdir
[root@localhost ~]# cd bdir
[root@localhost bdir]# ls
aa
```

> 将 bdir/bb文件移动到 cdir/cdir文件夹下
>

```
[root@localhost ~]# touch bb cc
[root@localhost ~]# ls
adir  adir,bdir,cdir  anaconda-ks.cfg  bb  bdir  cc  cdir
```

> 创建多个文件

```
[root@localhost ~]# rm -i bb
rm：是否删除普通空文件 "bb"？y
[root@localhost ~]# ls
adir  adir,bdir,cdir  anaconda-ks.cfg  bdir  cc  cdir
[root@localhost ~]#
```

> **注意：危险操作需要交互删除（-i）**  删除bb文件

```
[root@localhost ~]# rm -i cdir/
rm: 无法删除"cdir/": 是一个目录
[root@localhost ~]# rm -r cdir/
rm：是否进入目录"cdir/"? y
rm：是否进入目录"cdir/bdir"? y
rm：是否删除普通空文件 "cdir/bdir/bb"？y
rm：是否删除目录 "cdir/bdir"？y
rm：是否删除普通空文件 "cdir/cc"？y
rm：是否进入目录"cdir/cdir"? y
rm：是否删除普通空文件 "cdir/cdir/bb"？y
rm：是否删除目录 "cdir/cdir"？y
rm：是否删除目录 "cdir/"？y
```

> 直接删除目录不被支持，需要（-r） 
>
>  "（通配符）":表示当前目录下的任意文件、文件名称

```
[root@localhost ~]# rm -f cc
[root@localhost ~]# ls
adir  adir,bdir,cdir  anaconda-ks.cfg  bdir
[root@localhost ~]# rm -f adir/
rm: 无法删除"adir/": 是一个目录
[root@localhost ~]# rm -f *
rm: 无法删除"adir": 是一个目录
rm: 无法删除"adir,bdir,cdir": 是一个目录
rm: 无法删除"bdir": 是一个目录
```

> -f:删除文件不存在也不会提示信息（强制删除）

```
[root@localhost ~]# rm -rf /*
rm: 无法删除"/boot": 设备或资源忙
rm: 无法删除"/dev/mqueue": 设备或资源忙
rm: 无法删除"/dev/hugepages": 设备或资源忙
rm: 无法删除"/dev/pts/0": 不允许的操作
rm: 无法删除"/dev/pts/ptmx": 不允许的操作
rm: 无法删除"/dev/shm": 设备或资源忙
```

> rm -rf /*：删除根目录下所有文件（删库跑路/核弹命令）
>

### cat/more/grep/重定向/管道符/clear

#### cat/重定向

**显示所有文件和详细信息**

```

```

> pwd：显示当前目录
>
> ls:查看当前目录下有哪些文件
>
> ls -a :显示目录下所有文件夹，显示隐藏文件夹
>
> -l：表示以列表的形式展示文件详细信息
>
> ls -al  路径信息

**将命令执行结果信息输出到文件中**

```

```

> ls -al /在终端中执行命令，一般情况下会将命令的执行结果直接显示在终端内
>
> ls -al / > demo 将目录下所有文件的详细信息输出到demo文件中（包含隐藏文件）说明：若想要将终端执行结果发送给其他人查看或使用，则需要使用重定向符号将原本该在终端中显示信息放到文件中

**查看文件内容（较少内容文件）**

```

```

> cat demo 直接查看demo文件内容
>

**追加重定向**

```

```

> 将ls -al /user/bin/ >> demo   将/user/bin目录下的所有文件的详情信息都追加到demo文件中 说明：默认情况下使用，每次执行的结果都会覆盖前一次内容，若想要追加实现，需要改为>>（追加重定向符号）

#### less/more分屏显示文件内容

**以分屏的形式查看demo文件的内容**

```
less -N demo
more demo
```

> less和more都常用于查看内容较多的文件信息
>
> 快捷键，两个命令基本操作快捷键相同，
>
> - 向下翻页 空格
> - 向上翻页 B
> - 退出查看 Q
>
> 注意：想要以分屏样式查看大量内容的文件信息，文件内容至少应该超过当前终端窗口的显示大小

#### grep/|管道符/clear

**查找demo文件内容中包含mysql的信息**

```
grep mysql demo
```

> 说明1：grep 内容 文件名：从文件中查找包含特定内容的信息

在 /user/bin 目录下所有文件的信息中查找包含mysql的信息

```
ls -al /user/bin/ | grep mysql
```

> 说明2：通过管道符可以快捷的将两条指令结合起来，以省去需要先将结果写入文件，在进行查找的操作
>
> 注意：在测试工作中，我们只需要掌握管道符后侧常用的命令为grep

[Linux 命令 day02 随堂笔记.pdf](file:///D:/briup_study/Linux数据库—资料/Linux 命令 day02 随堂笔记.pdf)



## vi命令行文本编辑器

![image-20240815200142359](D:\a_briup_learn\微服务\Linux\Linux.assets\image-20240815200142359.png)

![image-20240815200232259](D:\a_briup_learn\微服务\Linux\Linux.assets\image-20240815200232259.png)

## 数据库

> 专门用来存储数据的软件

![image-20240815200435410](D:\a_briup_learn\微服务\Linux\Linux.assets\image-20240815200435410.png)

### SQL语言

![image-20240815200745830](D:\a_briup_learn\微服务\Linux\Linux.assets\image-20240815200745830.png)

![image-20240815200918402](D:\a_briup_learn\微服务\Linux\Linux.assets\image-20240815200918402.png)
