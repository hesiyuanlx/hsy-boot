package cn.hsy.boot.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * admin启动
 *
 * @author hesiyuan
 * @date 2022-09-07 4:28 PM
 **/
@Slf4j
@SpringBootApplication
public class AdminBootStrap {

    public static void main(String[] args) {
        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder(AdminBootStrap.class);
        ConfigurableApplicationContext configurableApplicationContext = springApplicationBuilder.run(args);

        // 添加退出钩子程序
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("SHUT_DOWN_MSG : Finished to audit admin bootstrap!");
        }));

        log.info("Successful to start audit admin bootstrap.");
    }

}
