import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;


@SpringBootApplication(scanBasePackages = {"com.mine.*"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.mine.*"})
public class MineApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(MineApiApplication.class, args);
    }
}
