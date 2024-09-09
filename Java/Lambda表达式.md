Lambda表达式

操作符：->

```
Lambda表达式：(参数列表)->(方法体部)  //只保留变化的部分，不变的都省略
快速的对某个接口/接口中的方法进行实现，得到一个该接口的实现类。
（匿名内部类）在特殊情况下
```

![image-20240909115240532](D:\a_briup_learn\Java\Lambda表达式.assets\image-20240909115240532.png)

**匿名内部类**

```java
//使用匿名内部类
new Thread(new Runable(){
   public void run(){
       System.out.println("Running...");
   }
}).start();
//使用lambda表达式
new Thread(()->System.out.println("Running...")).start();
```

**简化对函数式接口（即只有一个抽象方法的接口）的实现**

1. 实现`Runnable`接口

   ```
   Runnable r3 = ()->System.out.println("Task running:");
   ```

   ```java
   Runnable task = () -> {
       for (int i = 1; i <= 5; i++) {
           System.out.println("Task running: " + i);
           try {
               Thread.sleep(1000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
   };
   
   Thread thread = new Thread(task);
   thread.start();
   ```

2. 实现`Comparator`接口  比较器

   ```java
   import java.util.Arrays;
   import java.util.Comparator;
   
   public class LambdaComparatorExample {
       public static void main(String[] args) {
           String[] names = {"Alice", "Bob", "Charlie"};
           
           // 使用 Lambda 表达式对数组进行排序
           Arrays.sort(name,(a,b))->b.compareTo(a));//降序排序
           for(String name :names){
               System.out.println(name);
           }
       }
   }
   ```

   关于自然比较与比较器的区别？

3. 处理集合（注意看stream流）

   ```java
   List<String> list = Arrays.aslist("apple","banana","cherry");
   //使用Lambda表达式打印集合中的每个元素
   list.forEach(item->System.out.println(item));
   //使用方法引用--是一种简化lambda表达式的语法
   list.forEach(System.out::println);
   ```

4. 在GUI事件处理

   ```java
   import javax.swing.JButton;
   import javax.swing.JFrame;
   
   public class LambdaGUIExample {
       public static void main(String[] args) {
           JFrame frame = new JFrame("Lambda Example");
           JButton button = new JButton("Click Me");
   
           // 使用 Lambda 表达式处理按钮点击事件
           button.addActionListener(e -> System.out.println("Button clicked"));
   
           frame.add(button);
           frame.setSize(200, 200);
           frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           frame.setVisible(true);
       }
   }
   ```



