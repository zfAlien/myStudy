package com.juquren.entity.cdTest;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zhengfeng on 2017/10/25.
 * @author zhengfeng
 * @date 2017/10/25.
 */
public class CDPlayer {
    private MyCD cd;
    @Autowired
    public CDPlayer(MyCD cd){
        this.cd = cd;
    }
    public void doplay(){
        cd.play();
    }
}
