package com.trace.one.service.impl;

import com.alibaba.dubbo.rpc.RpcContext;
import com.common.sdk.ContentFacade;

public class contentFacadeImpl implements ContentFacade {
    @Override
    public Integer getNum() {
        System.out.println("我被调用了："+RpcContext.getContext().getAttachment("traceId"));
        return 1;
    }

    @Override
    public String getName() {
        System.out.println("我被调用了："+RpcContext.getContext().getAttachment("traceId"));
        return "zhengfeng";
    }
}
