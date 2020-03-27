package com.trace.one.trace.interceptor;

import com.alibaba.dubbo.rpc.RpcContext;
import com.trace.one.trace.content.TraceLogContext;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.StringUtils;

import java.util.UUID;

public class ServiceDigestLogInterceptor extends TraceLogContext implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String traceId = RpcContext.getContext().getAttachment("traceId");
        if(StringUtils.isEmpty(traceId)) {
            if(StringUtils.isEmpty(CONTEXT_HOLDER.get())) {
                String s = UUID.randomUUID().toString();
                CONTEXT_HOLDER.set(s);
                RpcContext.getContext().setAttachment("traceId", s);
            } else {
                RpcContext.getContext().setAttachment("traceId", CONTEXT_HOLDER.get());
            }
        } else {
            CONTEXT_HOLDER.set(traceId);
        }

        System.out.println("我tm的看看怎么调用的"+CONTEXT_HOLDER.get());
        Object result = invocation.proceed();
        return result;
    }
}
