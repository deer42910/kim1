# Stream

Java8引入的

结合了Lambda表达式，简化集合、数组的操作

**使用步骤**

1. 先得到stream流（流水线），并把数据传上去
2. 中间方法（过滤，转换）方法调用完毕之后，还可以调用其他方法
3. 终结方法（统计，打印）最后一步，调用完毕之后，不能调用其他方法

```java
/*
 * 单列集合  方法 default Stream<E> stream()    Collection中的默认方法
 * 多列集合  方法 无                            无法直接使用stream流（借助keySet、entrySet）
 * 数组     方法 public static<T> Stream<T> stream(T[] array)  Arrays工具类中的静态方法
 * 一堆零散数据 方法 public static<T> Stream<T> of(T...values)  Stream接口中的静态方法
 */
public class CreateStream {
    public static void main(String[] args) {
        //单列集合
        List<String> list = new ArrayList<>(Arrays.asList("我","爱","您","中","国"));
        /*//获取一条流水线，并把集合中的数据放到流水线上
        Stream<String> stream = list.stream();
        //使用终结方法打印一下流水线上的所有数据
        stream.forEach(new Consumer<String>() {

            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        });*/
        list.stream().forEach(l-> System.out.print(l+" "));
        System.out.println();
        //双列集合
        Map<Integer, String> map = new HashMap<>();
        map.put(1,"我");
        map.put(2,"爱");
        map.put(3,"wo");
        map.put(4,"zi");
        map.put(5,"ji");
        //无法直接使用stream流
        map.keySet().stream().forEach(k-> System.out.println(k+" "+map.get(k)));
        map.entrySet().stream().forEach(key-> System.out.println(key));
        //数组
        int[] arr = {1,2,3,4,5};
        String[] arr2 = {"a","b","c","d"};

        Arrays.stream(arr).forEach(s-> System.out.print(s+" "));
        System.out.println("----------");
        Arrays.stream(arr2).forEach(s-> System.out.print(s+" "));

        //一堆零散数据
        Stream.of(1,2,3,4,5).forEach(s-> System.out.print(s+" "));
        Stream.of("a","b","c","d").forEach(s-> System.out.print(s+" "));
    }
}
```

![5663aa79bbc94553116d5ca8da59c32f](D:\a_briup_learn\Java\stream.assets\5663aa79bbc94553116d5ca8da59c32f.jpeg)

```java
/**
 * filter  过滤
 * limit   获取前几个元素
 * ship    跳过前几个元素
 *
 * 细节1：中间方法，返回新的Stream流，原来的流只能使用一次，建议使用链式编程
 * 细节2：修改stream流中的数据，不会影响原来集合或者数组的数据
 */
public class modMethods {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        Collections.addAll(list,"张无尽","张晓梅","张大仙","赵磊","小怪","海绵爸爸","张一行","只狼","嘿吼","泛型");
        /*//filter 过滤 把张开头的留下，其余数据过滤不要
        list.stream().filter(new Predicate<String>() {

            @Override
            public boolean test(String s) {
                return s.startsWith("张");
            }
        }).forEach(s->System.out.println(s));*/
        list.stream().filter(s->s.startsWith("张")).forEach(System.out::println);  //这种写法？？

        System.out.println("-----");
        //前四个
        list.stream().limit(4).forEach(System.out::println);
        System.out.println("-----");
        //跳过5个输出之后
        list.stream().skip(5).forEach(System.out::println);
        System.out.println("-----");
        //保留4-6
        list.stream().skip(3).limit(3).forEach(System.out::println);
        System.out.println("-----");
        list.stream().limit(6).skip(3).forEach(System.out::println);
    }
}
```

```java
/**
 * distinct   元素去重，依赖（hashCode和equals方法）
 * concat     合并a和b两个流合为一个流
 * 注意细节1与2
 */
public class modMethod2 {
    public static void main(String[] args) {
        ArrayList<String> list1 = new ArrayList<>();
        Collections.addAll(list1,"张无尽","张晓梅","张大仙","赵磊","小怪","海绵爸爸","张一行","只狼","嘿吼","泛型");
        ArrayList<String> list2 = new ArrayList<>();
        //Collections.addAll(list1,"成爱婴","吴岚中","西北锤王","赵磊","小怪","海绵爸爸");
        //去重是在一个集合流中
        //list1.stream().distinct().forEach(s-> System.out.print(s+" "));//张无尽 张晓梅 张大仙 赵磊 小怪 海绵爸爸 张一行 只狼 嘿吼 泛型 成爱婴 吴岚中 西北锤王
        System.out.println();

        //合并是两个集合流的合并
        Collections.addAll(list2,"成爱婴","吴岚中","西北锤王","赵磊","小怪","海绵爸爸");
        Stream.concat(list1.stream(),list2.stream()).forEach(s-> System.out.print(s+" "));

    }
}
```

```java
/**
 * map  转换流中的数据类型
 * 细节注意
 */
public class modMethod3 {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list,"张无尽-14","张晓梅-45","张大仙-25","赵磊-87","小怪-54","海绵爸爸-90","张一行-65","只狼-34","嘿吼-54","泛型-32");
        //需求：只获取里边的年龄并进行打印
        //String->int
        //第一个类型：流中原本的数据类型
        //第二个类型：要转成类型

        //apply的形参s:依次表示流中的每一个数据
        //返回值：表示转换之后的数据
        list.stream().map(new Function<String, Integer>() {

            @Override
            public Integer apply(String s) {
                String[] arr = s.split("-");
                String ageString = arr[1];
                int age = Integer.parseInt(ageString);
                return age;
            }
        }).forEach(s->System.out.print(s+" "));
        //当map方法执行完毕之后，流上的数据就变成了整数，forEach 流中的数据就是整数
        System.out.println();
        System.out.println("-------------");
        list.stream().map(s->Integer.parseInt(s.split("-")[1]))
                .forEach(s->System.out.print(s+" "));
    }
}
```

![image-20240821161816754](D:\a_briup_learn\Java\stream.assets\image-20240821161816754.png)

```java
/**
 * void forEach(Consumer action)  遍历
 * long count()     统计
 * toArray()    收集流中的数据放到数组中
 */
public class endMethod {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        Collections.addAll(list,"张三","张无尽","张晓梅","张大仙","赵磊","小怪","海绵爸爸","张一行","只狼","嘿吼","泛型");

        //long count()
        long count = list.stream().count();
        System.out.println("集合中的元素个数是："+count);

        //toArray()    收集流中的数据放到数组中
        Object[] arr = list.stream().toArray();  //注意返回的是Object类型  具体的类型不行
        System.out.println(Arrays.toString(arr));

        //IntFunction<R> 函数型接口
        //泛型<? extends Object[]>表示继承Object[] 的具体类型的数组
        //apply的形参:流中数据的个数 要与数组的长度保持一致
        //apply的返回值:具体类型的数组
        //方法体:就是创建数组

        //toArray的参数:负责创建一个指定类型的数组
        //方法的底层:会依次得到流中的每一个数据然后放到数组当中
        //方法的返回值:装着流中数据的数组
        String[] array = list.stream().toArray(new IntFunction<String[]>() {
            @Override
            public String[] apply(int value) {
                return new String[value];
            }
        });
        System.out.println(Arrays.toString(array));

        String[] array2  = list.stream().toArray((value)->new String[value]);
        System.out.println(Arrays.toString(array2));
    }
}
```

```java
/*
 * collect(Collector collector)  收集流中的数据,放到集合当中{List Set Map}
 */
public class endmethod1 {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        Collections.addAll(list,"张三-男-15","张无尽-男-15","张晓梅-女-45","张大仙-男-46","赵磊-男-34","小怪-女-33","海绵爸爸-男-33","张一行-男-21","只狼-男-98","嘿吼-女-44","泛型-女-23");
        //收集List集合当中
        //需求:
        //我要把所以男性分装起来
        List<String> newList = list.stream().filter(s->"男".equals(s.split("-")[1])).collect(Collectors.toList());
        System.out.println(newList);

        //区别:去重
        //Set集合中
        Set<String> newSet = list.stream().filter(s->"男".equals(s.split("-")[1])).collect(Collectors.toSet());
        System.out.println(newSet);

        //Map
        //谁是key 谁是value
        //把男性全都收集起来
        //键:姓名  值:性别
        /**
         * toMap两个参数
         * 参数一:key的生成规则
         * 参数二:value的生成规则
         *
         * 参数一:
         *     Function泛型一:表示流中每一个数据的类型
         *             泛型二:表示Map中key的数据类型
         *       方法apply形参:依次表示流里边的每一个数据 张三-男-18
         *          方法体:生成键的代码
         *          返回值:已经生成的key
         * 参数二:
         *     Function泛型一:表示流中每一个数据的类型
                       泛型二:表示Map中value的数据类型
         *      方法apply形参:依次表示流里边的每一个数据 张三-男-18
         *          方法体:生成value的代码
         *          返回值:已经生成的value
         */
        Map map = list.stream().filter(s->"男".equals(s.split("-")[1])).collect(Collectors.toMap(new Function<String, String>() {
            @Override
            public String apply(String s) {
                //String s1 = s.split("-")[0];
                return s.split("-")[0];
            }
        }, new Function<String, Integer>() {
            @Override
            public Integer apply(String s) {
                //Integer s1 = Integer.parseInt(s.split("-")[2]);
                return Integer.parseInt(s.split("-")[2]);
            }
        }));
        /*byte[] n = new byte[]{1,2,3,4,5,6,7,8,9};
        System.out.println(n);//地址值*/
        //重点注意:sout中传入的是数组,他会用数组调用数组的toString()方法生成输出
        //而数组的toString()方法不会打印数组的内容,而是打印数组类型信息和其哈希码(即对象的内存地址)
        //System.out.println(map.entrySet().toArray());//[Ljava.lang.Object;@448139f0

        //所以我们要借助数组工具类Arrays 数组元素的内容的字符串表示
        //一维数组:使用Arrays.toString()  二维数组:Arrays.deepToString()
        System.out.println(Arrays.toString(map.entrySet().toArray()));

        Map<String,Integer> map1 = list.stream().filter(s->"男".equals(s.split("-")[1])).collect(Collectors.toMap(s->s.split("-")[0],s->Integer.parseInt(s.split("-")[2])));
        System.out.println(map1);
        System.out.println(Arrays.toString(map1.entrySet().toArray()));
        Set<Map.Entry<String,Integer>> entrySet = map1.entrySet();
        for(Map.Entry<String,Integer> entry : entrySet){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
        //注意:这儿又是错点
        /*
         * 为什么你将map1.entrySet()直接放在 强化for循环中报错,
         * 是因为在前面声明map1的时候,没有确定键值对的类型  Map<String,Integer> map1
         * 没有写泛型，默认泛型是Object传入Entry<String,Integer>中报错
         * 后面的age要类型转换
         */
    }
}
```



## **关于Consumer接口**

```
void accept(T t);  //代表接受单一输入参数并且不返回任何结果的操作
```

主要作用是处理某些操作而不需要返回结果，常用于进行副作用操作，如打印，修改状态等。

**作用**

1. 集合处理

   ```
   List<String> names =Arrays.asList("Alice","Bob","Charlie");
   names.forEach(name->System.out.println(name));
   ```

2. 事件处理

   ```
   Butten butten = new Button("Click me");
   button.setOnAction(event -> handleButtonClick(event));
   ```

   > `event -> handleButtonClick(event)` 是一个 `Consumer<Event>`，它处理按钮点击事件。

3. 操作数据

   可以用来执行数据处理而不**需要返回值**

   ```
   Consumer<String> printUpperCase = str->System.out.println(str.toUpperCase());
   printUpperCase.accept("hello");// 输出 "hello!"
   ```

   **Consumer的另一个方法，组合Consumer接口**

   andThen方法，将两个Consumer进行组合，以顺序执行多个操作。

   ```
   Consumer<String> print = s -> System.out.print(s);
   Consumer<String> printExclamation = s -> System.out.println(s + "!");
   Consumer<String> combinedConsumer = print.addThen(printExclamation);
   
   combinedConsumer.accept("hello");  // 输出 "hello!"
   ```

   > `combinedConsumer` 会先执行 `print`，然后执行 `printExclamation`，因此最终输出的结果是 `"hello!"`。

## 不可变集合

```java
/**
 * 不可变集合的特点：定义完成后不可以修改，或添加、删除
 * List、Set、Map接口中,都存在of方法可以创建不可变集合
 * 细节
 *  List：直接用
 *  Set：元素不能重复
 *  Map：元素不能重复、键值对数量最多是10个，也就是20个数据
 *       超过10个使用ofEntries方法
 *       在jdk10以后会有copyOf()方法
 */
public class noChange {
    public static void main(String[] args) {
       List list = List.of("1","2","3","4","5","6","7","8","9");
        for (Object o : list) {
            System.out.print(o+" ");
        }
        System.out.println();
        //list.add("10");//异常UnsupportedOperationException
        Map map  = new HashMap();
        map.put("1","1");
        map.put("2","2");
        map.put("3","3");
        map.put("4","4");
        map.put("5","5");
        map.put("6","6");
        map.put("7","7");
        map.put("8","8");
        map.put("9","9");
        map.put("10","10");
        map.put("11","11");
        Map map1 = Map.copyOf(map);
        for (Object o : map1.keySet()) {
            System.out.print(o+ " ");
        }
        //map1.put("我i","kun"); 报错，不能添加
    }
}
```

