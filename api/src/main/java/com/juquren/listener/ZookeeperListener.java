package com.juquren.listener;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 主要是用于监听zookeeper中节点的变化，配合dubbo demo provide连接到115.28.82.158
 */
@Component
public class ZookeeperListener {
    @PostConstruct
    private void init() throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client =
                CuratorFrameworkFactory.builder()
                        .connectString("115.28.82.158:2181")
                        .sessionTimeoutMs(60000)
                        .connectionTimeoutMs(5000)
                        .retryPolicy(retryPolicy)
                        .build();
        client.start();
        client.getChildren().usingWatcher(new CuratorWatcherImpl(client)).forPath("/dubbo/com.alibaba.dubbo.demo.DemoService/providers");
    }
}
