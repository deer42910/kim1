ioc 生命周期配与作用域配置

![image-20240831212832849](C:\Users\ASUS\AppData\Roaming\Typora\typora-user-images\image-20240831212832849.png)

![image-20240831213121701](C:\Users\ASUS\AppData\Roaming\Typora\typora-user-images\image-20240831213121701.png)

![image-20240831213140135](C:\Users\ASUS\AppData\Roaming\Typora\typora-user-images\image-20240831213140135.png)

实例化一个ioc容器之后，他会自动死亡。不调用.close()是不会出现销毁方法中的信息的。

**作用域**

![image-20240831214344597](C:\Users\ASUS\AppData\Roaming\Typora\typora-user-images\image-20240831214344597.png) 

scope属性：

默认单列模式  一个bean 对应一个BeanDefinition 对应一个组件信息  ：就是不论获取几个bean对象，得到的都是同一个。

多列模式prototype：bean-BeanDefinition-组件对象，只要getBean()就是创建一个组件对象



![image-20240831221105355](D:\a_briup_learn\spring\ioc 生命周期配与作用域配置.assets\image-20240831221105355.png)

![image-20240831215456394](D:\a_briup_learn\spring\ioc 生命周期配与作用域配置.assets\image-20240831215456394.png

![image-20240831220909720](D:\a_briup_learn\spring\ioc 生命周期配与作用域配置.assets\image-20240831220909720.png)

![image-20240831220935484](D:\a_briup_learn\spring\ioc 生命周期配与作用域配置.assets\image-20240831220935484.png)

**面试题**

![image-20240831221230406](D:\a_briup_learn\spring\ioc 生命周期配与作用域配置.assets\image-20240831221230406.png)