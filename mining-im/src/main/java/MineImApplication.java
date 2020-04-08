import com.mine.service.NettyServer;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import java.net.InetSocketAddress;


@SpringBootApplication(scanBasePackages = {"com.mine.*"})
public class MineImApplication implements CommandLineRunner {

    @Autowired
    private NettyServer socketServer;

    public static void main(String[] args) {
        SpringApplication.run(MineImApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        InetSocketAddress address = new InetSocketAddress(8888);
        ChannelFuture future = socketServer.run(address);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> socketServer.destroy()));
        future.channel().closeFuture().syncUninterruptibly();
    }
}
