import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

import java.util.List;

public class ZookeeperRegistryTest {
    @Test
    public void test() throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client =
                CuratorFrameworkFactory.builder()
                        .connectString("115.28.82.158:2181")
                        .sessionTimeoutMs(60000)
                        .connectionTimeoutMs(5000)
                        .retryPolicy(retryPolicy)
                        .build();
        client.start();
        client.create().forPath("/path");
        client.create().forPath("/path/ttt");
        List<String> strings = client.getChildren().forPath("/dubbo/com.alibaba.dubbo.demo.DemoService/providers");
        System.out.println(strings);
    }
}
