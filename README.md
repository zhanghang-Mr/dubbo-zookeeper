# dubbo-zookeeper
dubbo+zookeeper实现多个服务双向调用


#### Dubbo及Zookeeper安装

##### Dubbo

服务监控

- 到Dubbogithub的官网上下载：https://github.com/apache/dubbo-admin/tree/master

- 下载到本地解压，

- 查看dubbo-admin -> src -> main- >resources ->application.properties 配置文件

- 根据自己的zookeeper 修改配置文件的注册地址

- 在项目下使用mavne打包duboo-admin

  >mvn clean package -Dmaven.test.skip=true
  >
  >//clean 表示清除旧的jar包
  >
  >//-Dmaven.test.skip=true  表示不进行测试

- 打完包在运行

>在dubbo-admin\dubbo-admin-master\dubbo-admin\target下找到 dubbo-admin-0.0.1-SNAPSHOT.jar 运行
>
>java -jar  dubbo-admin-0.0.1-SNAPSHOT.jar

- 启动起来后访问测试

  >如果没有改端口的话，默认是7001
  >
  >默认用户密码为 root    root

![image-20200908112057068](C:\Users\zh970\AppData\Roaming\Typora\typora-user-images\image-20200908112057068.png)



##### Zookeeper

注册中心

- 下载Zookeepe4r

  ![image-20200908093443989](C:\Users\zh970\AppData\Roaming\Typora\typora-user-images\image-20200908093443989.png)

没有windows版，Linux版下载解压也可以当作windows版使用

- 运行 /bin/zhServer.cmd , 初次运行如果报错，查看有没有zoo.cfg配置文件；

  可能遇到问题：闪退

  解决方案：编辑zhServer.cmd文件末尾添加pause,这样运行出错就不会退出，会提示错误信息，方便找出原因，相当于debug.

  ![image-20200908094108076](C:\Users\zh970\AppData\Roaming\Typora\typora-user-images\image-20200908094108076.png)

注意：zookeeper 的解压目录不能有中文目录。否则会启动失败

- 启动起来之后 启动zkCli.cmd 测试
- ![image-20200908103846944](C:\Users\zh970\AppData\Roaming\Typora\typora-user-images\image-20200908103846944.png)

输入 ls / 测试

![image-20200908103919725](C:\Users\zh970\AppData\Roaming\Typora\typora-user-images\image-20200908103919725.png)

使用命令创建 create -e /zhanghang 123

![image-20200908104350781](C:\Users\zh970\AppData\Roaming\Typora\typora-user-images\image-20200908104350781.png)

![image-20200908104537706](C:\Users\zh970\AppData\Roaming\Typora\typora-user-images\image-20200908104537706.png)

##### 测试服务注册发现

- 先创建两个基础服务，一个服务提供者，一个服务消费者
- 添加dubbo的依赖

```pom
<!--添加dubbo的依赖-->
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-spring-boot-starter</artifactId>
    <version>2.7.3</version>
</dependency>
```

- 添加zookeeper的依赖，记得排除日志冲突

```pom
 <!--添加zookeeper的客户端依赖zkclient -->
        <dependency>
            <groupId>com.github.sgroschupf</groupId>
            <artifactId>zkclient</artifactId>
            <version>0.1</version>
        </dependency>
        <!--zookeeper 及其依赖包，解决日志冲突，还需要剔除日志依赖 -->
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-framework</artifactId>
            <version>2.12.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-recipes</artifactId>
            <version>2.12.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
            <version>3.4.14</version>
            <!--排除这个slf4j-log4j12-->
            <exclusions>
                <exclusion>
                    <groupId>org.slf4j</groupId>
                    <artifactId>slf4j-log4j12</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
```

- 配置服务提供者注册到zookeeper

  ```yml
  #配置dubbo 和 zookeeper相关
  #服务应用名字
  dubbo:
    application:
      name: provider_server
  #注册中心地址,根据自己的zookeeper 提供的端口号配置
    registry:
      address: zookeeper://127.0.0.1:2181
  #那些服务要被注册,要扫描的包的路径
    scan:
      base-packages: com.zh
  ```

  

- 启动服务提供者，然后看监控

  ![image-20200908155745516](C:\Users\zh970\AppData\Roaming\Typora\typora-user-images\image-20200908155745516.png)

- 在消费者服务配置

```yml
#配置dubbo 和 zookeeper相关
#服务应用名字
dubbo:
  application:
    name: consummer_server
  #注册中心地址,根据自己的zookeeper 提供的端口号配置
  registry:
    address: zookeeper://127.0.0.1:2181
```

- 想要使用服务提供者的接口，两种方法，一种是添加pom坐标，另一种是在消费者服务中创建和提供者一个路径的接口

![image-20200908163418041](C:\Users\zh970\AppData\Roaming\Typora\typora-user-images\image-20200908163418041.png)

- 在使用的实现类中引用

```java
package com.zh.service;


import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

@Service //放到Spring容器中
public class UserService {

    //想要拿到provider_server 服务提供的接口，要去注册中心拿到服务
    @Reference   //引用，有两种方法，一：引用pom坐标， 二： 可以定义提供者接口路径相同的接口名
    TicketService ticketService ;

    public String getTicket(){
        return ticketService.getTicket();
    }
}
```

注意大坑：

1. dubbo 想要实现双向调用,就要设置dubbo的监控端口，不同服务设置不同的

   >```yml
   >#配置dubbo 和 zookeeper相关
   >#服务应用名字
   >dubbo:
   >  application:
   >    name: provider_server
   >#注册中心地址,根据自己的zookeeper 提供的端口号配置
   >  registry:
   >    address: zookeeper://127.0.0.1:2181
   >#那些服务要被注册,
   >  scan:
   >    base-packages: com.zh
   >  protocol:     #dubbo监控暴露端口，如果有多个提供者，端口必须不同
   >    port: 20880
   >```

2. 服务提供者的实现类上的@Service注解 必须是dubbo的
3. 调用方@Reference(check=false,timeout=5000)注解使用的时候check一定要设置为false,代表不检查，否则启动报错，timeout代表超时时间
