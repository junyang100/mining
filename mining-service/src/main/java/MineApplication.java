import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;


@SpringBootApplication(scanBasePackages = {"com.mine.*"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.mine.*"})
@MapperScan(basePackages = {"com.mine.mapper"})
public class MineApplication {
    public static void main(String[] args) {
        SpringApplication.run(MineApplication.class, args);
    }
}
