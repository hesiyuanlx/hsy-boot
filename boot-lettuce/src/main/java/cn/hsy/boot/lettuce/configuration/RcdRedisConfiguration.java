package cn.hsy.boot.lettuce.configuration;

import cn.techwolf.rcd.ds.impl.JedisClusterDSManager;
import cn.techwolf.rcd.ds.impl.LettuceClusterKVDSManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redis 配置初始化
 */
@Configuration
public class RcdRedisConfiguration {

    @Bean(initMethod = "init")
    @ConfigurationProperties(prefix = "redis")
    public JedisClusterDSManager jedisManager() {
        return new JedisClusterDSManager();
    }

//    @Bean(initMethod = "init")
//    @ConfigurationProperties(prefix = "redis")
//    public LettuceClusterKVDSManager lettuceClusterKVDSManager() {
//        return new LettuceClusterKVDSManager();
//    }

}
