# IO流

流向内存是输入流，流出内存的输出流

**分类**

> 参考系是当前程序  

​      输入流                                    输出流 

其他设备 -读取>内存      内存-写出>其他设备

字节流  字符流   顾名思义 字节为单位，字符为单位

**顶级父类**

|            |           ** 输入流**           |              输出流              |
| :--------: | :-----------------------------: | :------------------------------: |
| **字节流** | 字节输入流<br />**InputStream** | 字节输出流<br />**OutputStream** |
| **字符流** |   字符输入流<br />**Reader**    |    字符输出流<br />**Writer**    |

**FileOutputStream**

```java
package com.kim.IO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Author:kim
 * @Description: TODO
 * @DateTime: 2024/8/23 14:45
 **/
public class FileOutputStreamConstructor {
    public static void main(String[] args) throws IOException {
        //构造方法，两种
        /**创建流对象，要么输入文件名，要么输入一个文件对象*/
        File file = new File("a.txt");
        FileOutputStream fos = new FileOutputStream(file);
        //使用文件名称创建流对象
        FileOutputStream fos2 = new FileOutputStream("b.txt");

        /**写出数据*/
        //write(int b)一个字节数据  虽然int是4个字节，但输出还是1个字节信息
        fos.write(97);
        fos.write(98);
        fos.write(99);
        //write(byte[] b)
        byte[] b = "黑马程序员".getBytes();
        fos.write(b);
        //write(byte[] b,int off,int len)
        byte[] c="我爱你kun".getBytes(StandardCharsets.UTF_8);
        fos.write(c,1,2);
        //在文件中乱码  原因：由于中文字符在 UTF-8 中通常占用多个字节，直接写入这两个字节可能导致文件中出现乱码。为了确保中文字符正确显示，你应该写入整个字节数组，或者处理每个字符的字节表示。

        /**数据追加续写*/
        FileOutputStream fos3 = new FileOutputStream("a.txt",true);
        byte[] d = "abcde".getBytes();
        fos3.write(d,1,3);
        //abc黑马程序员��bcd
        /**换行 \r\n   .write("\r\t".getByte());
         * 回车符\r 回到一行开头
         * 换行符\n 下一行
         * */

        /**必须释放资源*/
        //关闭资源
        fos.close();
        
        /**public void flush() ：刷新此输出流并强制任何缓冲的输出字节被写出。*/
    }
}
```

**FileInputStream**

```java
package com.kim.IO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @Author:kim
 * @Description: TODO
 * @DateTime: 2024/8/23 15:18
 **/
public class FileInputStreamConstructor {
    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream("a.txt");
        /**
         * 读取数据 返回一个字节
         */
        int read = fis.read();
        System.out.println((char) read); //不强转只会输出字节码,并且会自动提升为int类型
        read=fis.read();
        System.out.println((char) read);
        //当读取到末尾，就会返回 -1
        /**
         * 改良循环输出 ((b = fis.read())!=-1)
         * read(byte[] b)  读取b的长度个字节到数组中
         */
        // 定义变量，作为有效个数
        int len;
        // 定义字节数组，作为装字节数据的容器
        byte[] b=new byte[3];
        while ((len=fis.read(b))!=-1){
            //每次读取之后，把数组变成字符串打印
            /**
             * new String(b);的弊端|len的作用
             * cd
             * ed
             * 是由于最后一次读取时，只读取一个字节e，数组中，上次读取的数据没有被完全替换，所以要通过len*/
            System.out.println(new String(b,0,len));//len每次读取的有效字节个数
            System.out.println();
        }

        fis.close();
    }
}
```

**copy图片**

```java
package com.kim.IO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Author:kim
 * @Description: 先将文件读出，然后输出到指定目的的。流的关闭原则：先开后关，后开先关。
 * @DateTime: 2024/8/23 15:32
 **/
public class copy {
    public static void main(String[] args) throws IOException {
        // 1.创建流对象
        // 1.1 指定数据源
        FileInputStream fis = new FileInputStream("C:\\Users\\ASUS\\Pictures\\Camera Roll\\1.png")
        // 1.2 指定目的地
        FileOutputStream fos = new FileOutputStream("1.png");

        // 2.读写数据
        // 2.1 定义数组
        byte[] b = new byte[1024];
        // 2.2 定义长度
        int len;
        // 2.3 循环读取
        while ((len=fis.read(b))!=-1){
            //2.4写出数据
            fos.write(b,0,len);
        }
        //关闭资源
        fos.close();
        fis.close();
    }
}
```

**字符读取流FileReader  字符输出流FileWriter**

```java
package com.kim.IO;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @Author:kim
 * @Description: 读取字符流  字符输出流
 * @DateTime: 2024/8/23 16:01
 **/
public class FRRead {
    public static void main(String[] args) throws IOException {

        FileReader fr = new FileReader("read.txt");
        int len;
        //定义字符数组，作为装字符数据的容器
        char[] buf = new char[2];
        while ((len= fr.read(buf))!=-1){
            System.out.println(new String(buf,0,len));
        }
        fr.close();

        /**
         * flush ：刷新缓冲区，流对象可以继续使用。
         * close:先刷新缓冲区，然后通知系统释放资源。流对象不可以再被使用了。
         * 即便是flush方法写出了数据，操作的最后还是要调用close方法，释放系统资源。
         */
        FileWriter fw = new FileWriter("write.txt");
        fw.write("hello");
        fw.flush();
        fw.write(97);
        fw.flush();
        fw.write('b');
        //fw.close();
        fw.write('a');
        //fw.close();  //报错 Stream closed
        fw.write(30000);//文编码表中30000对应一个汉字
        /**
        【注意】
         1.参数为int类型四个字节，但只保留一个字符的信息写出
         2.关闭资源时,与FileOutputStream不同。
         如果不关闭,数据只是保存到缓冲区，并未保存到文件。
        */

        //写出字符数组
        char[] chars = "输出字符流".toCharArray();
        fw.write(chars);
        fw.write("\r\n".toCharArray());
        fw.write(chars,2,3);//输出字符流字符流
        //写出字符串
        String s = "woxiangyizhiyiqi";
        fw.write(s);
        fw.write("\r\n");
        fw.write(s,5,9);
        fw.close();
        /**
         * 字符流，只能操作文本文件，不能操作图片，视频等非文本文件
         * 当我们单纯读或者写文本文件时 使用字符流 其他情况使用字节流
         */
    }
}
```

IO**异常处理**

JDK7以前 try-catch-finally

JDK7时优化try-with-resource语句，确保每个资源在语句结束时关闭。

```java
 try ( FileWriter fw = new FileWriter("fw.txt"); ) {//;可以多个
            // 写出数据
            fw.write("黑马程序员"); //黑马程序员
        } catch (IOException e) {
            e.printStackTrace();
        }
```

JDK9 try-with-resource的改进，对于**引入对象**的方式，更加简洁。被引入的对象，同样可以自动关闭，无需手动close。

```java
final FileReader fr = new FileReader("in.txt");
FileWriter fw = new FileWriter("out.txt");
try(fr;fw){
        //定义变量
       int b;
        //读取对象
        while((b=fr.read())!=-1){
        //写出数据
        fw.write(b);
    }catch(IOException e){
        e.printStackTrace();
    }
}
```

```
节点流总结
在 Java 中，IO 流按照功能划分可以分为两类：
节点流（原始流）
增强流（包装流）
节点流（Node Streams）是最基本的 IO 流，直接与数据源或目标进行交互，但
缺乏一些高级功能。
它们提供了最底层的读写功能，可以直接读取或写入底层数据源或目标，如文
件、网络连接等。
上面我们学习的所有流都是节点流：
FileInputStream 和 FileOutputStream ：用于读取和写入文件的字节
流。
FileReader 和 FileWriter ：用于读取和写入文件的字符流
ByteArrayInputStream 和 ByteArrayOutputStream ：用于读取和写入
字节数组的流
CharArrayReader 和 CharArrayWriter ：用于读取和写入字符数组的流
（省略，与ByteArray字节流类似，可自学）
```

**拷贝文件夹**

```java
package com.kim.IO.test;

import java.io.*;

/**
 * @Author:kim
 * @Description: 拷贝文件夹
 * @DateTime: 2024/8/23 16:51
 **/
public class copyDic {
    public static void main(String[] args) throws IOException {
        //拷贝一个文件夹，考虑子文件夹
        //1.创建对象表示数据源
        File src = new File("D:\\aaa\\src");

        //2.创建对象表示的目的地
        File dest = new File("D:\\aaa\\dest");

        //3.调用方法开始拷贝
        copydir(src,dest);
    }

    /**
     * 作用：拷贝文件夹
     * @param 数据源
     * @param 目的地
     * @throws IOException
     */
    private static void copydir(File src,File dest) throws IOException{
        dest.mkdirs();//创建目的文件夹
        //递归
        //1.进入数据源
        File[] files = src.listFiles();
        //2.遍历数组
        for(File file:files){
            if(file.isFile()){
                //3.判断文件，拷贝
                FileInputStream fis = new FileInputStream(file);
                FileOutputStream fos = new FileOutputStream(new File(dest,file.getName()));
                byte[] b = new byte[1024];
                int len;
                while((len=fis.read(b))!=-1){
                    fos.write(b,0,len);
                }
                fos.close();
                fis.close();
            }else {
                //判断文件夹，递归
                copydir(file,new File(dest,file.getName()));
            }
        }
    }
}
```

**文件加密**

```java
package com.kim.IO.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Author:kim
 * @Description: 文件加密
 * @DateTime: 2024/8/23 17:14
 **/
public class Test02 {
    /**
     * 为了保证文件的安全性，就需要对原始文件进行加密存储，再使用的时候再对其进行解密处理。
     * 加密原理：
     *   对原始文件中的每一个字节数据进行更改，然后将更改以后的数据存储到新的文件中。
     * 解密原理：
     *   读取加密之后的文件，按照加密的规则反向操作，变成原始文件。
     *
     *   ^:异或
     *      两边相同：false
     *      两边不同：true
     *
     *      0:false
     *      1:true
     *   128 64 32 16 8 4 2 1
     *      100: 01100100
     *       10: 00001010
     *       ^--------------
     *           01101110
     *       10: 00001010
     *       ^--------------
     *           01100100  :100
     */
    public static void encryptionAndReduction(File src, File dest) throws IOException {
        FileInputStream fis = new FileInputStream(src);
        FileOutputStream fos = new FileOutputStream(dest);
        int b;
        while ((b=fis.read())!=-1){
            fos.write(b^2);
        }
        fos.close();
        fis.close();
    }
}
```

**缓冲流**

> 缓冲流是增强流，其底层借助其他流实现功能，所以构造器要求一定要传 入一个字节流对象！

```
public BufferedInputStream(InputStream in) ：创建一个 新的缓冲
输入流。
public BufferedOutputStream(OutputStream out) ： 创建一个新的
缓冲输出流。
```

> 整行读，整行写
>
> 输入流的ready()方法，如果流还有数据，ready返回true，如果读取 到文件末尾，ready返回false。
>
> newLine()换行

```
public BufferedReader(Reader in) ：创建一个新的缓冲输入流。
public BufferedWriter(Writer out) ： 创建一个新的缓冲输出流。
```

是将数据暂时存储在内存中的缓冲区中，然后按照一定的 块大小进行读取或写入操作。相比于直接对磁盘或网络进行读写操作，使用缓冲 区可以减少频繁的 I/O 操作，从而提高效率。

**标准流**

预定义的流对象，用于处理标准输入、标准输出和标准错误

```
System.in   System.out  System.err
//System类中的setXXX方法修改标准输入输出流的源头和目的地。
System.setIn(InputStream);
System.setOut(PrintStream);
System.setErr(PrintStream);
```

**printStream打印流**

> 增强流
>
> 但
>
> ```
> PrintStream ps = new PrintStream("src/dir/b.txt");
> ```
>
> 因 源码
>
> ```
> public PrintStream(String fileName) throws FileNotFoundException{
>     this(false,new FileOutputStream(fileName));
> }
> ```
>
> 提供了一系列的print和println方法，可以输出各种数据类型的值，并自动转换为字符串形式。

**转换流**

```
java.io.OutputStreamWriter ，可以将字节输出流转换为字符输出流，并指
定编码
java.io.InputStreamReader ，可以将字节输入流转换为字符输入流，并指
定编码
```

```
//使用默认编码转换
public OutputStreamWriter(OutputStream in);
//使用指定编码转换
public OutputStreamWriter(OutputStream in, String
charsetName);
```

#### 对象流

> ObjectOutputStream序列化  ObjectInputStream读出，反序列化 
>
> 对象需要实现Serialzable序列化接口
>
> 写入对象的次数，和读取对象的次数应该一致，若超过实际写入，出现EOFException异常
>
> 使用集合化序列避免上述把知道读取几个对象的情况 即序列化多个对象，先将所有对象添加到一个集合，然后序列化集合对象，反序列化读取单个集合对象，再从集合中获取所有对象。
>
> transient 短暂的 修饰类中的属性，进行序列化时，忽略掉指定的属性，即读出都是默认值0|null
>
> serialVersionUID序列化版本号 序列化的静态变量，确保序列化与反序列化的版本一致。
>
> 解决：手写版本号 private static final long serialVersionUID = 1L; //在类中

**JavaAPI使用实现序列化接口**

**`java.lang.String`**: 表示不可变的字符序列。

**`java.util.ArrayList`**: 动态数组的实现。

**`java.util.HashMap`**: 基于哈希表的 `Map` 实现。

**`java.util.Date`**: 表示日期和时间。

**`java.io.File`**: 表示文件和目录的路径名。
推荐集合序列化（不知道文件中对象个数）

#### IO数据流

> 专门用于读写基本数据类型和字符串的流
>
> DataOutputStream负责把指定类型的数据，转化为字节并写出去
>
> DataInputStream负责把读取到的若干个字节，转化为指定类型的数据

```
out = new DataOutputStream(new FileOutputStream(file));
out.writeLong(1000L);
out.writeInt(5);
out.writeDouble(10.5D);
out.writeChar('a');
out.writeUTF("hello，中国");
out.flush();

in = new DataInputStream(new FileInputStream(file));
// 注意，数据读出来的顺序要，和之前写进去的顺序一致
System.out.println(in.readLong());
System.out.println(in.readInt());
System.out.println(in.readDouble());
System.out.println(in.readChar());
System.out.println(in.readUTF());
```

**随机访问流**

**注意**：并没有继承之前介绍到的四种抽象父类型

它即 可读取文件内容，又可以往文件中写入数据，同时它还可以任意定位到文件的某 一个位置进行读写操作。

```
//传入文件以及读写方式
public RandomAccessFile(File file, String mode) throws
FileNotFoundException;
//跳过pos字节
public void seek(long pos) throws IOException;
```

```
'r'模式，只读
'rw'模式，读写，若文件不存在，试图创建该文件
'rws读写，还要求对文件内容或元数据的每个更新都同步写入到底层设备。
'rwd'读写，要求对文件内容每个更新都同步写入到底层设备
```

```java
public class RandomAccessFile {
    public static void main(String[] args) {
        //实例化随机流对象
        File file = new File("src/com/kim/chap11/a.txt");
        RandomAccessFile randomAccessFile = new RandomAccessFile(file,"rw");
        int replacePos = 6;
        String replaceContent = "briup";

        // 2.定位到要替换数据的位置
        randomAccessFile.seek(replacePos);
        // 在指定位置，写入需要替换的内容，覆盖原来此位置上的内容
        randomAccessFile.write(replaceContent.getBytes());
        System.out.println("替换成功！");

        //3.定位到文件的开始位置,读文件中的所有内容，并输出到控制台
        randomAccessFile.seek(0);
        byte[] buf = new byte[8];
        int len = -1;
        while ((len=randomAccessFile.read(buf))!=-1){
            System.out.println(new String(buf,0,len]));
        }
        randomAccessFile.close();
    }
}
```

**Properties**

```java
Properties p = new Properties();
//3.从输入流加载key-value到工具类对象中
p.load(new FileInputStream(file));

//4.解析key-value并输出
String driver = p.getProperty("driver");
String url = p.getProperty("url");
String username = p.getProperty("username");
String password = p.getProperty("password");

//5.如果不知道key名称的话，则获取所有key，再根据key获取对应
value，进行遍历
//Enumeration<?> names = p.propertyNames();
Set<String> keySet = p.stringPropertyNames();
for (String key : keySet) {
System.out.println(key + ": " +
p.getProperty(key));
}

//7.修改属性值
p.setProperty("username", "root");
p.setProperty("password", "root");
```

