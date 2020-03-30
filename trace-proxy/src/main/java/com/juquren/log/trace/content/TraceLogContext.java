package com.juquren.log.trace.content;

import com.alibaba.dubbo.rpc.RpcContext;
import org.springframework.util.StringUtils;

import java.util.UUID;

public class TraceLogContext {
    protected static final ThreadLocal<String> CONTEXT_HOLDER              = new ThreadLocal<>();

    protected void dubboTraceId(){
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
    }

    protected void httpTraceId(){
        if(StringUtils.isEmpty(CONTEXT_HOLDER.get())) {
            String s = UUID.randomUUID().toString();
            CONTEXT_HOLDER.set(s);
        }
    }
}
