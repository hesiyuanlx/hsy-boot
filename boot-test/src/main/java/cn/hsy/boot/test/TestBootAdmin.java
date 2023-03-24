package cn.hsy.boot.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 测试启动类
 *
 * @author hesiyuan
 * @date 2022-09-20 11:49 AM
 **/
@Slf4j
@SpringBootApplication
public class TestBootAdmin {

    public static void main(String[] args) {
        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder(TestBootAdmin.class);
        ConfigurableApplicationContext configurableApplicationContext = springApplicationBuilder.run(args);

        // 添加退出钩子程序
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("SHUT_DOWN_MSG : Finished to audit admin bootstrap!");
        }));

        log.info("START_SUCCESS_MSG : Successful to start service bootstrap.");
    }

}
