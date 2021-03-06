import com.juquren.listener.CuratorWatcherImpl;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

/**
 * 测试zookeeper连接,并创建节点
 */
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
        //判断节点是否存在
        if (client.checkExists().forPath("/path") == null) {
            client.create().forPath("/path");
        }
        if (client.checkExists().forPath("/path/ttt") == null) {
            client.create().forPath("/path/ttt");
        }

        client.getChildren().usingWatcher(new CuratorWatcherImpl(client)).forPath("/dubbo/com.alibaba.dubbo.demo.DemoService/providers");
    }
}
