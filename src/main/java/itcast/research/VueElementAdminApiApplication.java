package itcast.research;

import itcast.research.util.DBLogUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "itcast.research")
@EnableJpaRepositories(basePackages = "itcast.research.repository")
@EntityScan("itcast.research.entity")
public class VueElementAdminApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(VueElementAdminApiApplication.class, args);
    }
}
