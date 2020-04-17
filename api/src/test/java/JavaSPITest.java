import com.juquren.spi.Robot;
import org.junit.Test;

import java.util.ServiceLoader;

/**
 * 简单的测试java 的spi
 */
public class JavaSPITest {
    @Test
    public void sayHello() throws Exception {
        ServiceLoader<Robot> serviceLoader = ServiceLoader.load(Robot.class);
        System.out.println("Java SPI");
        serviceLoader.forEach(Robot::sayHello);
        System.out.println(String.format("%s哈哈%<s,嗯%s", "11", "22"));
    }
}
