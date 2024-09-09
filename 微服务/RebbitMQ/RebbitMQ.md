RebbitMQ

消息队列 异步

##### 同步 

1. 功能耦合度高 事件紧密联系在一起，链式，中间错导致后面也错
2. 响应时间长 依次响应
3. 并发压力传递    流量起伏过大 超过硬件承受能力容易造成服务器宕机
4. 系统弹性不够 若要接入新功能 需修改代码

#####  异步

1. 功能解耦 存入消息队列 然后分支各功能
2. 快速响应 存入消息队列后返回响应
3. 削峰限流
4. 便于扩展 分支直接添加新功能 无需修改代码

![2b28c8bb6e04fd4d0da41b9bcb4fde0e](D:\a_briup_learn\微服务\RebbitMQ\RebbitMQ.assets\2b28c8bb6e04fd4d0da41b9bcb4fde0e-1724634609784-3.jpg)

![95c9134814596e1be93faf7c4f68bfe4](D:\a_briup_learn\微服务\RebbitMQ\RebbitMQ.assets\95c9134814596e1be93faf7c4f68bfe4.jpg)

#### 常见单词

queue队列

protocol协议

advanced先进的，领先的

fanout广播

direct定向

topic通配符

routing路由选择

template模板、样本

durable持久性

convert转变
