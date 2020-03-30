package com.juquren.dubbo.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.juquren.dubbo.content.TraceLogContext;


@Activate(group = Constants.CONSUMER, order = -10001)
public class ConsumerTraceFilter extends TraceLogContext implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        dubboTraceId(invocation);
        try {
            return invoker.invoke(invocation);
        } finally {
            clearMDC();
        }
    }
}
