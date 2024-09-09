在项目**RegexUtils**中

isBlank()方法

```
isblank()Java11引入的string类方法，用于检查一个字符串是否空白。
1、完全为空（即长度为0）
2、仅包含空白字符串（空白、制表符、换行符）
```

```
        String str1 = ""; // 空字符串
        String str2 = "   "; // 仅包含空格
        String str3 = "Hello, World!"; // 普通字符串

        System.out.println(str1.isBlank()); // 输出: true
        System.out.println(str2.isBlank()); // 输出: true
        System.out.println(str3.isBlank()); // 输出: false
```

> 与isEmpty()的区别是：isBlank()只检查字符串为空，不会考虑空字符串

CharSequence是java中的一个接口，表示一系列字符，常见包括：String、StringBuilder和StringBuffer。这个接口运行统一操作这些不同类型的字符系列，如：获取字符、获取长度等

trim()方法

```
trim()方法是Java中String类的一部分，用于去除字符串两端的空白字符，包括空格、制表符和换行符。不影响其他字符
注意：去除的是两端的空白字符，字符串中间的空白符不会被去除。
```

```
//str == null字符串是否为空，判断一个字符串尚未初始化或已被设为null 
//str.trim().isEmpty()表示包含空白字符
return str == null || str.trim().isEmpty();  
//java8中使用上面
return str.isBlank();
两个表示效果相同
```

```
if(str!=null && str.isBlank()){
    //处理空白字符串的情况
}
```

**ThreadLocal** java中的一个类，用于创建线程的局部变量

校验登录中，获取到用户信息，然后就用户保存到ThreadLocal中线程之间数据不会相互干扰。

```
public T get()//返回当前线程的 ThreadLocal 变量的值
public void set(T value)  //设置线程局部变量的值
public void remove()  //清理，防止内存泄漏
protected T initialValue() //初始化值得默认值实现
```

```java
public class ThreadLocalExample {
    //初始化线程ThreadLocal变量得值为1 是私有得，所以set()时，只对当前线性有效
    private static ThreadLocal<Integer> threadLocalValue = ThreadLocal.withInitial(()->0);

    public static void main(String[] args) {
        //ExecutorService：管理线程池，负责任务的调度和线程的复用。
        //创建一个固定大小得线程池，该线程池有2个线程，这个线程池可以并行执行两个任务，线程会被复用，而不是每次任务执行时创建新的线程。
        ExecutorService executor = Executors.newFixedThreadPool(2);
        //lambda表达式 提交一个任务到线程池
        executor.submit(()->{
            try {
                threadLocalValue.set(1);
                System.out.println("Thread 1:"+threadLocalValue.get());
            }finally {
                //确保即使发生异常，ThreadLocal变量得值也会被清理
                threadLocalValue.remove();//清理ThreadLocal变量
            }
        });
        executor.submit(()->{
            try{
                threadLocalValue.set(2);
                System.out.println("Thread 2:"+threadLocalValue.get());
            }finally {
                threadLocalValue.remove();
            }
        });
        executor.shutdown();
    }
}
```

**Executor接口** 上面例子中ExecutorService就继承了它

> Java并发包中的一个核心接口，定义执行异步任务的基本契约。提供了一种将任务提交给线程或其他执行机制的方法，而不关心任务的具体执行细节或线程的创建和管理。

只定义了一个`execute(Runnable command)`方法，用于提交一个Runnable任务。这个方法将任务提交给Executor进行异步执行，而不需要手动创建和线程管理。

```
常见的实现：
ThreadPoolExecutor 是 Executor 的一个常用实现，它提供了一个线程池来管理和复用线程。你可以通过 Executors 工厂类创建不同类型的线程池，如固定大小线程池、缓存线程池等。
```

```java
public class ExecutorExample {
    public static void main(String[] args) {
        //创建一个固定大小的线程池
        Executor executor = Executors.newFixedThreadPool(2);
        //提交任务到线程池
        executor.execute(()->{
            System.out.println("Task 1 is running in"+Thread.currentThread().getName());
        });
        executor.execute(()->{
            System.out.println("Task 2 is running in"+Thread.currentThread().getName());
        });

        //关闭线程池（在实际应用中应考虑使用ExecutorService进行更全面的线程池管理）
        ((ExecutorService)(executor)).shutdown();
    }
}
```

**在程序中**

```java
public class UserHolder {
    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();

    public static void saveUser(UserDTO user){
        tl.set(user);
    }

    public static UserDTO getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
```

