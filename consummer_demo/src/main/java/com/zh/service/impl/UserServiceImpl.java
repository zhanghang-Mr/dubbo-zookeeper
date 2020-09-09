package com.zh.service.impl;


import com.zh.service.TicketService;
import com.zh.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;


//@Service //放到Spring容器中
@Service
@Component
public class UserServiceImpl implements UserService {

    //想要拿到provider_server 服务提供的接口，要去注册中心拿到服务
    //设置超时时间
    @Reference(check=false,timeout=5000)   //引用，有两种方法，一：引用pom坐标， 二： 可以定义提供者接口路径相同的接口名
    TicketService ticketService ;

    public String testProvider(){
        return ticketService.getTicket();
    }

}
