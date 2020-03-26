package com.juquren.entity.cdTest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created by zhengfeng on 2017/10/25.
 * @author zhengfeng
 */
@Configuration
public class JavaConfig {
    @Bean
    public MyCD myCD(){
        return new MyCD();
    }
    @Bean(name = "cdPlayer")
    public CDPlayer cdPlayer(){
        System.out.println("hehh");
        return new CDPlayer(myCD());
    }

    @Bean
    @Profile("dev")
    public CDPlayer cdPlayer2(){
        return new CDPlayer(myCD());
    }
}
