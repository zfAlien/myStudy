package com.juquren.listener;

import com.alibaba.fastjson.JSON;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;

import java.util.List;

/**
 * 监听具体类,用于节点发生变化后做相应的改变或调整
 */
public class CuratorWatcherImpl implements CuratorWatcher {
    private final CuratorFramework client;

    public CuratorWatcherImpl(CuratorFramework client) {
        this.client = client;
    }

    @Override
    public void process(WatchedEvent event) throws Exception {
        String path = event.getPath() == null ? "" : event.getPath();
        List<String> strings = client.getChildren().usingWatcher(this).forPath(path);
        System.out.println("我监听到了变化,节点"+ JSON.toJSONString(strings));
    }
}
