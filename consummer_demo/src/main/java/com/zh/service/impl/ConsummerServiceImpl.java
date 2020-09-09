package com.zh.service.impl;

import com.zh.service.ConsummerService;
import org.apache.dubbo.config.annotation.Service;

//注意：提供者接口的实现类@Service注解 必须是dubbo的
@Service
public class ConsummerServiceImpl implements ConsummerService {

    @Override
    public String getConsummer() {
        return "======consummer_server ====提供服务====";
    }
}
