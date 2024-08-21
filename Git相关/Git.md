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

1. 打开用户目录，创建.bashrc**(文本文档)**

   ```
   #或者
   touch ~/.bashrc
   ```

2. 在文本文档中输入以下内容

   ![image-20240817165254919](D:\a_briup_learn\Git相关\Git.assets\image-20240817165254919.png)

   ```
   #用于输出git的执行日志
   alias git-log='git log --pretty=oneline --all --graph --abbrev-commit'
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

直接退出：**exit**

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

有些文件我们是不需要添加进git管理的，不要他出现在untacked列表，我们可以在**工作目录**中创建一个名为**.gitignore**

![image-20240817172332827](D:\a_briup_learn\Git相关\Git.assets\image-20240817172332827.png)

## 分支

每个人有独立的分支，互不影响，最后合并到一起

```
git branch #查看本地分支
git branch 分支名  #创建分支
```

> 以上，一个git-log全出来

**切换分支**

```
git checkout 分支名  #切换分支
git checkout -b 分支名  #一个新分支并切换（切换到一个不存在的分支）
```

> HEAD->指到谁 谁是当前分支

**合并分支**

> 分支上的东西必须先提交，才能切换分支
>
> - 将所有人提交到仓库（repositories）的代码都汇总到一起

```
git merge 分支名称  #一个分支上的提交可以合并到另一个分支
```

**删除分支**

```
git branch -d 分支名 #删除时需要在各种检查
git branch -D 分支名 #不需要各种检查 强制删除
```

**解决冲突**

合并分支后，两个或多个分支对同一个文件进行修改，git不知道要取哪一个就会conflict（冲突）

![image-20240818132012219](D:\a_briup_learn\Git相关\Git.assets\image-20240818132012219.png)

## gitHub

**配置SSH公钥**    SSH secure shell（壳） 加密安全

- 获取公钥
  - cat ~/.ssh/id_rsa.pub

> ...

**操作远程仓库**

- 添加到远程仓库

```
git remote add <自己设置的仓库别称，一般是origin> <仓库路径SSH>
```

- 查看远程仓库

```
git remote 
```

- 推送到远程仓库

```
git push [-f] [–set-upstream] [远端名称 [本地分支名][:远端分支名] ]
```

git push 括号中的都可以不用写

> -f 表强覆盖性
>
> –set-upstream 推送到远端的同时并且建立起和远端分支的关联关系。
>
> #很繁琐，一般远程名称与本地名称相同所以只写本地，上面不看

```
git push origin master:master  #将本地仓库的master分支提交到远程仓库的master上去
git push origin master #本地仓库的当前master推送到远程仓库上去
```

在[Commits · deer42910/kim1 (github.com)](https://github.com/deer42910/kim1/commits/main/)在线查看

- 本地仓库与远程分支的关联关系

```
git branch -vv 
```

![image-20240818140534652](D:\a_briup_learn\Git相关\Git.assets\image-20240818140534652.png)

- 从远程仓库克隆

```
git clone <仓库路径>[本地目录]
```

> 使用SSH好使，路径已经被淘汰
>
> 后面不要本地目录，直接再此目录下gitbash进行clone
>
> ![image-20240818140556564](D:\a_briup_learn\Git相关\Git.assets\image-20240818140556564.png)

**远程仓库上拉起或抓取**

远程分支和本地分支一样，可以进行merge操作，只需将远端仓库里的更新都下载到本地，再进行操作

- 抓取命令 :(感觉多此一举，还要麻烦手动合并)

  ```
  git fetch [remote name][branch name]
  ```

  > 就是将仓库中的更新都抓取到本地，不会进行合并

- 拉取命令（一般直接使用）

  ```
  git pull [remote name][branch name]
  ```

  > 相当于将远端仓库的修改拉到本地并自动进行合并，等同于fetch+merge

**为什么要将远程仓库更新的资源抓取到本地要合并呢？**

> 为了使远程仓库与本地仓库版本一致
>
> push使远程仓库的更新是最新的，而pull使本地仓库的版本是最新的

## 在Idea中配置git

[Git（分布式版本控制工具）-CSDN博客](https://blog.csdn.net/a18307096730/article/details/124586216?spm=1001.2014.3001.5502)





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
