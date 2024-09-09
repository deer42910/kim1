接收参数

param和json参数比较

param key=value&key=value      name=张三&age=18

json {key:value,key:value}           

- 参数编码 ASCII  UTF-8
- 参数顺序 无序    有序
- 数据类型 仅支持字符串  接收更复杂的数据结构，如数组、对象等
- 嵌套性 不支持 