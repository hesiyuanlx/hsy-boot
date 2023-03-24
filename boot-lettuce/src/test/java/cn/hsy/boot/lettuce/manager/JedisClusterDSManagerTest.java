//package cn.hsy.boot.lettuce.manager;
//
//import cn.techwolf.rcd.ds.impl.JedisClusterDSManager;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.annotation.Resource;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//
///**
// * 测试
// *
// * @author hesiyuan
// * @date 2023-03-22 5:20 PM
// **/
//@SpringBootTest
//@RunWith(SpringRunner.class)
//public class JedisClusterDSManagerTest {
//
//    @Resource
//    private JedisClusterDSManager jedisClusterDSManager;
//
//    @Test
//    public void testPInc() {
//        Set<String> keySet = new HashSet<String>(){{
//            add("testKey");
//        }};
//        jedisClusterDSManager.pIncr(keySet, (int) TimeUnit.DAYS.toSeconds(10L));
//    }
//
//    @Test
//    void testSetNx() {
//        jedisClusterDSManager.setNx("key1", "value1");
//    }
//
//}
