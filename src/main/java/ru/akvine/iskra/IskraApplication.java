package ru.akvine.iskra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.akvine.iskra.configs.properties.ParallelExecutorProperties;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EnableConfigurationProperties({
		ParallelExecutorProperties.class
})
public class IskraApplication {

	public static void main(String[] args) {
		SpringApplication.run(IskraApplication.class, args);
	}

}
