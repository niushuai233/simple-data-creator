package cc.niushuai.datacreator;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@MapperScan("cc.niushuai.datacreator.biz.**.mapper")
@SpringBootApplication
public class DataCreatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataCreatorApplication.class, args);
    }
}
