package com.juquren.bean;

import com.juquren.entity.User;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;

public class TestFactoryBean implements FactoryBean, DisposableBean {
    @Override
    public void destroy() throws Exception {

    }

    @Override
    public Object getObject() throws Exception {
        System.out.println("我被调用了!");
        User user = new User();
        user.setId(666666);
        return user;
    }

    @Override
    public Class<?> getObjectType() {
        return User.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
