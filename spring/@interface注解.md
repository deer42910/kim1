## @interface

定义注解（Annotation) 用于提供元数据或额外的信息

**定义注解**

**作用**: 使用 `@interface` 关键字来定义一个注解类型。注解定义类似于接口定义，但它不包含方法的实现。

```java
@interface MyAnnotation {
    // 可以定义元素（类似于方法）
    String value() default "";
    int count() default 0;
}
```

两个元素：`value` 和 `count`。元素可以有默认值，也可以没有

**注解的元素**

注解可以包含零个或多个元素（类似于方法），这些元素可以有默认值。元素在使用注解时可以被设置为不同的值。

```java
@MyAnnotation(value = "example", count = 10)
public class MyClass {
    // class body
}
```

**常用注解**

**内置注解**：java提供了一些内置注解，如：

- `@Override` 用于指示方法覆盖了父类的方法。（重写）

- `@Deprecated` 用于标记已经不推荐使用的代码。

- `@SuppressWarnings` 用于抑制编译器警告。

**自定义注解**

- 自定义注解用于将特定的元数据附加到代码中，以供编译器或其他工具使用。

**注解的应用**

- 类、方法和字段上：测试方法@Test,标记类为Spring组件@Component,标记字段作为序列化的属性@SerializedName
- 代码生成和处理：被工具（编译器、框架、代码生成器）处理，以生成额外的代码或进行特定的处理。

**元注解**

用于注解其他注解的注解

- `@Retention`: 指定注解的保留策略，即注解在什么阶段可用（源码、编译时、运行时）。

- `@Target`: 指定注解可以应用于哪些 Java 元素（类、方法、字段等）。

- `@Documented`: 指示注解应该被包含在 Javadoc 文档中。

  > Javadoc 是一种用于生成 Java 代码文档的工具和格式。在Java源代码中嵌入特定格式化的注释，自动生成API文档。这些文档描述了代码中的类、方法、字段和构造函数，以及它们的用途与行为。（具体看本片后面）

- `@Inherited`: 指示子类会继承父类的注解。

```java
@Retention(RetentionPolicy.RUNTIME)     //表该注解在运行时阶段可用
@Target(ElementType.METHOD)             //表该注解用在方法中
public @interface MyMethodAnnotation {
    String description() default "No description";
}
```

**总结**

`@interface` 用于定义注解类型。

注解可以附加元数据，影响编译、运行时行为，或被工具和框架使用。

注解定义与普通接口类似，但不包含实现代码，注解的元素类似于方法，但不能有实现。

注解可以用于多种用途，包括代码生成、工具集成和配置。

**Javadoc注释**

Javadoc 是一种用于生成 Java 代码文档的工具和格式。它通过在 Java 源代码中嵌入特定格式的注释，自动生成 API 文档。这些文档描述了代码中的类、方法、字段和构造函数，以及它们的用途和行为。Javadoc 文档是 Java 开发中常用的一种文档格式，帮助开发者了解和使用代码库。

### Javadoc 的基本概念

**Javadoc 注释**

Javadoc 注释是以 `/**` 开始，以 `*/` 结束的特殊注释，类、方法、字段的声明之前。

```java
/**
 * 计算两个整数的和。
 * 
 * @param a 第一个整数
 * @param b 第二个整数
 * @return 两个整数的和
 */
public int add(int a, int b) {
    return a + b;
}
```

**Javadoc标签**

- `@param`：描述方法参数。

  `@return`：描述方法的返回值。

  `@throws` 或 `@exception`：描述方法可能抛出的异常。

  `@see`：提供相关信息的链接。

  `@deprecated`：标记已弃用的类、方法或字段。

  `@author`：指定作者。

  `@version`：指定版本。

**生成Javadoc文档**

使用 Javadoc 工具可以从这些注释中**生成 HTML 格式**的文档。可以通过命令行工具 `javadoc` 进行生成，也可以通过 IDE（如 IntelliJ IDEA、Eclipse）提供的功能生成。

```
javadoc -d doc -sourcepath src com.example
```

这个命令会从 `src` 目录下的源代码中生成 Javadoc 文档，并将生成的文档放到 `doc` 目录中

Javadoc 注释通过提供清晰、结构化的描述来帮助开发者理解代码。生成的 Javadoc 文档可以用 HTML 格式展示类的接口、方法和字段，便于查看和使用。