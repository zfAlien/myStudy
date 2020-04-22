package com.juquren.log.trace.proxy;

import com.juquren.log.trace.content.TraceLogContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JDKDynamicProxy extends TraceLogContext implements InvocationHandler {
    private Object target;

    public JDKDynamicProxy(Object target) {
        this.target = target;
    }

    /**
     * 获取被代理接口实例对象
     * @param <T>
     * @return
     */
    public <T> T getProxy() {
        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        dubboTraceId();
        System.out.println("我是jdk动态代理调用的" + CONTEXT_HOLDER.get());
        Object result = method.invoke(target, args);
        return result;
    }
}
