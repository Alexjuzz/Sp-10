package Spring.Home10.tests;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TestsApplication {

	public static void main(String[] args) {

		SpringApplication.run(TestsApplication.class, args);
	}

}
