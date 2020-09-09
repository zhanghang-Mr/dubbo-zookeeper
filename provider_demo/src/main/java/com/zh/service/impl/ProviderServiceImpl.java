package com.zh.service.impl;

import com.zh.service.ConsummerService;
import com.zh.service.ProviderService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;


@Service
@Component
public class ProviderServiceImpl  implements ProviderService {

    @Reference(check=false,timeout=5000)
    ConsummerService consummerService;

    @Override
    public String testConsummer() {
        return consummerService.getConsummer();
    }
}
