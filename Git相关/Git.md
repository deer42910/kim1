# Git

github是社区,git是一个服务系统。github用的就是git系统来管理它们的网站。

### **开发中的场景**

1. **备份** 模块完成前一秒电脑坏了
2. **代码还原（回滚）**  有点思路就代码修改的面目全非
3. **协同开发**  几个人要同时修改这个代码，我刚下载一个文件，他就新写了一点，麻烦重新下载
4. **追溯代码编写人和编写时间** 

### 集中式与分布式

**集中式**

本地客户端只保存当前版本信息，而所有东西都保存在一个中央服务器如（SVN）上。

> 若一天这个中央服务器奔溃了，所有的东西都丢失了。
>
> 当你想要进行回滚操作时都得向服务器发送请求。

![image-20240817153631097](D:\a_briup_learn\Git相关\Git.assets\image-20240817153631097.png)

**分布式**

没有“中央服务器“每一个电脑上都有一个完整的版本库，工作的时候不需要联网，因为版本就在自己的电脑上。多人协作只需要将各自的修改推送给对方，就能相互看到对方的修改了。

![image-20240817154109126](D:\a_briup_learn\Git相关\Git.assets\image-20240817154109126.png)

> 有共享版本库的概念，但每个人自己的电脑都是一个共享版本库

![image-20240817154435684](D:\a_briup_learn\Git相关\Git.assets\image-20240817154435684.png)

**常用的linux指令**

```
ls 查看当前文件
cat 查看文件内容
touch 创建文件
vi vi编辑器
```

```
Git GUI:GIT提供的图形界面工具
Git Bash:git提供的小的命令行工具   有个复制小技巧：选择上面代码段然后用滚轮
```

**设置git bash中的简洁命令**

1. 打开用户目录，创建.bashrc

   ```
   #或者
   touch ~/.bashrc
   ```

2. 输入以下内容

   ![image-20240817165254919](D:\a_briup_learn\Git相关\Git.assets\image-20240817165254919.png)

   ```
   #用于输出git的执行日志
   $ alias git-log='git log --pretty=oneline --all --graph --abbrev-commit'
   #用于输出当前目录所有文件的基本信息
   alias ll='ls al'
   ```

3. 打开gitBash

   ```
   ASUS@DESKTOP-UR4U184 MINGW64 ~
   $ source ~/.bashrc
   ```

![image-20240817163522431](D:\a_briup_learn\Git相关\Git.assets\image-20240817163522431.png)

![image-20240817155726899](D:\a_briup_learn\Git相关\Git.assets\image-20240817155726899.png)

### 关于配置

`git config --global`全局配置

设置邮箱和用户名，在遇到需要远程登入的仓库比如github时就会自动帮你填好，只需要输入密码

```
git config --global user.name "kim"
git confif --global user.email "2803514404@qq.com"
```

### 本地仓库配置

```
$cd xx/xx/xx   #进入目标目录下  （/d盘也可以其他前面就不加/）
$git init    #会自动将当前仓库设置成master 
```

!(D:\briup_study\笔记文档\Akimm\Git.assets\image-20240817162136384.png)

![image-20240817164753544](D:\a_briup_learn\Git相关\Git.assets\image-20240817164753544.png)

> 上图 touch add status  commit(注释)  log  (基础 log的优化在上面)

**版本回退**

- 版本切换

```
git reset --hard commitID  #hard后面了解
```

- 如何查看已经删除记录

```
git reflog
```

![image-20240817170738495](D:\a_briup_learn\Git相关\Git.assets\image-20240817170738495.png)

**添加文件至忽略列表**

有些文件我们是不需要添加进git管理的，不要他出现在untacked列表，我们可以在工作目录中创建一个名为.giti

![image-20240817172332827](D:\a_briup_learn\Git相关\Git.assets\image-20240817172332827.png)

## 分支

每个人有独立的分支，互不影响，最后合并到一起

```
git branch #查看本地分支
git branch 分支名  #查看分支
```

> 以上，一个git-log全出来

**切换分支**

```
git checkout 分支名  #切换分支
git checkout -b 分支名  #一个新分支并切换（切换到一个不存在的分支）
```

> HEAD->指到谁 谁是当前分支

**合并分支**

```
git merge 分支名称  #一个分支上的提交可以合并到另一个分支
```

**删除分支**

```
git branch -d 分支名 #删除时需要在各种检查
git branch -D 分支名 #不需要各种检查 强制删除
```

































#### 常见英文

```
fatal：pathspec "xxxx" did not match any files
```

注意：是拼写错误

> fatal 致命的  
>
> path spec 路径 检查 inspection检查、视察 
>
> match匹配

```
error:pathspecn 'add new "jiawan.c"' did not match any file(s) know to git
```

在该仓库所在的目录下有.git的隐藏文件，冲突所以找不到。

**git在未提交之前有三种状态**

- Untracked files 未跟踪

  > 我们创建了新的文件准备提交上去的，这种好办只要add了就可以了。
  >
  > git add test.c

- Changes not staged for commit 未提交的更改

  > 即仓库里的文件更改了但是还没提交

- Changes to be committed 提交的更改

**关于改写上一次提交信息**

> (看Vim的笔记)

首先i，进入insert模式，你根据上面的英文，在指定区域修改message然后esc退出insert模式进入命令模式，再 :wq保存（不要忘了：）























https://blog.csdn.net/bjbz_cxy/article/details/116703787?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522172308850316800180619680%2522%252C%2522scm%2522%253A%252220140713.130102334..%2522%257D&request_id=172308850316800180619680&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~top_positive~default-1-116703787-null-null.142^v100^pc_search_result_base3&utm_term=git命令&spm=1018.2226.3001.4187
