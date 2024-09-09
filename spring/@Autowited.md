@Autowited

//<property userService ->对应类型的bean配置

自动装配注解（DI依赖注入）：1.在ioc容器中查找符号类型的组件对象 2.设置给当前属性（di）

JSR-250注解 **@Resource**是 @Autowired+@Qualifier(value = "maomiService222")

没有@Qualifier：=根据@Autowired标记位置的变量名作为bean的id进行配置，（若此次是接口，他又多个实现类就会装配失败）

没有 @Qualifier 注解：根据 @Autowired 标记位置成员变量的变量名作为 bean 的 id继续匹配

@Resourse(name=“    ”)

```xml
<dependency>
    <groupId>jakarta.annotation</groupId>
    <artifactId>jakarta.annotation-api</artifactId>
    <version>2.1.1</version>
</dependency>
```

> 在高于jdk11引入依赖

@Bean是配置类中的，区别上面的

```java
public @interface Bean {
    //前两个注解可以指定Bean的标识
    @AliasFor("name")
    String[] value() default {};
    @AliasFor("value")
    String[] name() default {};
  
    //autowireCandidate 属性来指示该 Bean 是否候选用于自动装配。
    //autowireCandidate 属性默认值为 true，表示该 Bean 是一个默认的装配目标，
    //可被候选用于自动装配。如果将 autowireCandidate 属性设置为 false，则说明该 Bean 不是默认的装配目标，不会被候选用于自动装配。
    boolean autowireCandidate() default true;

    //指定初始化方法
    String initMethod() default "";
    //指定销毁方法
    String destroyMethod() default "(inferred)";
}
```

@Import(ConfigA.class)

引用，被应用的也一定是配置类@Configuration