package com.juquren.dubbo.content;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.RpcContext;
import com.juquren.constants.TraceConstants;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import java.util.UUID;

public abstract class TraceLogContext {
    protected static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    protected void dubboTraceId(Invocation invocation) {
        String traceId = invocation.getAttachment("traceId");
        if (StringUtils.isEmpty(traceId)) {
            if (StringUtils.isEmpty(CONTEXT_HOLDER.get())) {
                String s = UUID.randomUUID().toString();
                CONTEXT_HOLDER.set(s);
                RpcContext.getContext().setAttachment("traceId", s);
            } else {
                RpcContext.getContext().setAttachment("traceId", CONTEXT_HOLDER.get());
            }
        } else {
            CONTEXT_HOLDER.set(traceId);
        }
        System.out.println("traceId:" + CONTEXT_HOLDER.get());
        setTraceIdMDC(CONTEXT_HOLDER.get());
    }

    protected void httpTraceId(){
        if(StringUtils.isEmpty(CONTEXT_HOLDER.get())) {
            String s = UUID.randomUUID().toString();
            CONTEXT_HOLDER.set(s);
        }
        setTraceIdMDC(CONTEXT_HOLDER.get());
    }

    protected void clearMDC() {
        MDC.remove(TraceConstants.TRACE_ID);
    }

    private void setTraceIdMDC(String traceId) {
        MDC.put(TraceConstants.TRACE_ID, traceId);
    }

}
