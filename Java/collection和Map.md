![image-20240821184142998](D:\a_briup_learn\Java\collection和Map.assets\image-20240821184142998-1724671879297-2.png)

我们按实现类的顺序，分析接口

#### Collection

##### List

ArraysList：数组,查询快，插入删除慢（因为要左右移）

LinkedList: 链表，插入快（只需修改指针），查询慢（遍历）

就根据实际的性能需求选择，需要其他特殊的

offer和add，pop和poll都是一样的，不过offer和poll在该异常的时候不会抛出异常，会返回null。它提供了一种安全的方式来处理空队列，而不必担心引发错误。

```java
  //add方法除了在末尾加，还可以add(index,value),有的特殊结构类是继承下来的，为了语义，尽量不用
        List<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.get(0);
//遍历还有for(:)和Iterator
        System.out.println(Arrays.toString(list.toArray()));
        System.out.println("______________________________");
        Deque<Integer> deque = new LinkedList<>();
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addLast(3); //213
        deque.add(4);  //也是在后面加
        deque.pop();
        deque.addAll(list);//在后面加
        System.out.println(Arrays.toString(deque.toArray()));
        System.out.println("______________________________");
        Queue<Integer> queue=new LinkedList<>();
        queue.add(1);
        queue.offer(2);
        queue.offer(3);  //123
        queue.addAll(list); //也是在后面
        System.out.println(Arrays.toString(queue.toArray()));
        queue.poll();
        System.out.println(queue.peek());
        System.out.println("______________________________");
        Deque<Integer> deque2=new ArrayDeque<>();
        deque2.add(5);
        deque2.offer(6);
        deque2.offer(7);
        deque2.offer(8);
        deque2.poll();
        deque2.pollLast(); // 67
        System.out.println(Arrays.toString(deque2.toArray()));
        System.out.println("______________________________");
        Vector<Integer> list2=new Vector<>();//这里写 List<Integer> 也是一样的 ，无非是面向接口编程的格式，是多态的表现。
      /*这里不想写方法，vector是早期的集合类
        相比于ArraysList，vector是同步的，线程更安全，因为同步的花销，相对应的性能更弱
        我们用得多的只是他的子类 栈 Stack*/
        Stack<Integer> stack=new Stack<>();//这里不能面向接口编程因为他是继承的，不是实现类
        //不用add的原因看最上面
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.pop();
        System.out.println(Arrays.toString(stack.toArray()));
```

##### Set

set无序，不重复

HashSet：允许存null，插入删除查找都是O(1).

TreeSet：在set接口下的sortedset的实现类，有序，不重复，插入删除是O(logn) n为集合中的个数,底层是红黑树（平衡二叉树），对于有序的集合用这个效率更高。

```java
 Set<String> hashSet = new HashSet<>();
        hashSet.add("a");
        hashSet.add("b");
        hashSet.add("c");
        System.out.println(hashSet.size());
        System.out.println(hashSet.isEmpty());
        System.out.println(hashSet.contains("d"));
        Iterator<String> iterator = hashSet.iterator();
        while (iterator.hasNext()){
            System.out.printf(iterator.next()+"\t");
        }
        System.out.println();
        hashSet.remove("a");
        hashSet.clear();
        System.out.println("______________________________");
        SortedSet<String> sortedSet = new TreeSet<>();
        sortedSet.add("f");
        sortedSet.add("c");
        sortedSet.add("g");
        sortedSet.add("o");
        sortedSet.add("a");
        for (String string : sortedSet){
            System.out.printf(string+"\t");
        }
        System.out.println();
        System.out.println(sortedSet.first());  //最小的
        System.out.println(sortedSet.last());   //最大的
        System.out.println(sortedSet.subSet("a","f"));  //【）
        System.out.println(sortedSet.headSet("f"));  //<
        System.out.println(sortedSet.tailSet("g")); //>=
```



#### Map

HashMap：k和v都可以为null；和上面那个一样，里面的具体的类如果是自己写的类，要重写hash code和equals方法；无序

我以User为例子

```java

    // 重写 equals() 方法
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // 引用相同，肯定相等
        if (obj == null || getClass() != obj.getClass()) return false; // 类型不同，不相等
        User user = (User) obj; // 强制转换为 User 类型
        return id == user.id && Objects.equals(name, user.name); // 比较 id 和 name 是否相等
    }

    // 重写 hashCode() 方法
    @Override
    public int hashCode() {
        return Objects.hash(id, name); // 使用 id 和 name 计算 hash 值
    }
```

TreeMap:和上面有点像，是实现Map的子接口sortedMap，对key排序，当然也可以在构造方法里面传过去一个比较器，根据比较器来排序

LinkedHashMap：和HashMap一样，就是加了一个双向链表保证存和取的顺序是一致的

```java
public class MapDemo {
    public static void main(String[] args) {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", "tom");
        hashMap.put("age", 18);
        hashMap.put("alive", true);
        System.out.println(hashMap);//hashMap里面自动改写了toString
        //遍历，第一种
        Set<String> keySet = hashMap.keySet();
        for (String key : keySet) {
            System.out.println(key + ":" + hashMap.get(key));
        }
        System.out.println(hashMap.get("name").hashCode());
        String s = "tom";
        System.out.println(s.hashCode()); //same
        System.out.println("______________________________");
        SortedMap<String, Object> sortedMap = new TreeMap<>();
        sortedMap.put("b", "tom");
        sortedMap.put("c", "james");
        sortedMap.put("a", "rose");
        //第二种遍历   联想一下hash类型的redis的java客户端获取数据的方法
        Set<Map.Entry<String, Object>> entrySet = sortedMap.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        //自定义比较器：通过看源码可以知道，只需要自定义一个实现类实现Comparator<>接口，<>里面写你想传参数比较的类就行
        //注意，这个比较的是key，所以你得确保比较器比较的种类和key种类相同
        SortedMap<User, Object> sortedMap2 = new TreeMap<>(new UserAgeComparator());
        //剩下的懒得写
        System.out.println("______________________________");
        //那个LinkedHashMap懒得写，没啥意义
    }


}

class User {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

}

class UserAgeComparator implements Comparator<User> {
    @Override
    public int compare(User u1, User u2) {
        // 升序排序，如果想降序排序，可以交换 u1 和 u2 的位置
        return Integer.compare(u1.getAge(), u2.getAge());
    }
}
```

---



#### 流操作

待续





map.of之类的

待续