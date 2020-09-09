package com.zh;

import com.zh.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProviderDemoApplicationTests {

    @Autowired
    TicketService ticketService;

    @Test
    void contextLoads() {
        System.out.println(ticketService.getTicket());
    }

}
