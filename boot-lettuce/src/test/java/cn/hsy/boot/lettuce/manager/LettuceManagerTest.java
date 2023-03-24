package cn.hsy.boot.lettuce.manager;

import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Maps;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author hesiyuan
 * @date 2023-03-21 6:02 PM
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class LettuceManagerTest {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void testSetAndGetKey() {
        stringRedisTemplate.opsForValue().set("hsyKey", "hsyValue", 30, TimeUnit.SECONDS);
        String value = stringRedisTemplate.opsForValue().get("hsyKey");
        Assert.assertNotNull(value);
    }

    @Test
    public void testTTL() {
        stringRedisTemplate.opsForValue().set("hsyKey", "hsyValue", 30, TimeUnit.SECONDS);
        Long key1TTl = stringRedisTemplate.opsForValue().getOperations().getExpire("hsyKey");
        Assert.assertTrue(key1TTl != null && key1TTl == 30);

        Long key2TTL = stringRedisTemplate.opsForValue().getOperations().getExpire("hsyKey2");
        Assert.assertTrue(key2TTL != null && key2TTL == -2);

        Long key3TTL = stringRedisTemplate.getExpire("hsyKey");
        Assert.assertTrue(key3TTL != null && key3TTL > 10);
    }

    @Test
    public void testMultiGet() {
        List<String> keys = Arrays.asList("key1", "key2");
//        for (String key : keys) {
//            stringRedisTemplate.opsForValue().set(key, "1", 30, TimeUnit.SECONDS);
//        }
        List<Object> res1 = stringRedisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                for (String key : keys) {
                    operations.opsForValue().set(key, "1", 30, TimeUnit.SECONDS);
                }
                return null;
            }
        });
        List<String> res = stringRedisTemplate.opsForValue().multiGet(keys);
        Assert.assertTrue(CollectionUtils.isNotEmpty(res));
    }

    @Test
    public void testPipelinedSessionCallback() {
        List<String> keys = Arrays.asList("hsyKey1", "hsyKey2", "hsyKey3", "hsyKey4");

        List<Object> res = stringRedisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                for (String key : keys) {
                    operations.opsForValue().set((K) key, (V) "1", 30, TimeUnit.SECONDS);
                }
                return null;
            }
        });
        Assert.assertEquals(res.size(), keys.size());

        List<Object> res2 = stringRedisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                for (String key : keys) {
                    operations.opsForValue().get("None");
                    operations.opsForValue().set(key, "1", 30, TimeUnit.SECONDS);
                    operations.opsForValue().get(key);
                }
                return null;
            }
        });
        Assert.assertNotEquals(res2.size(), keys.size());
    }

    @Test
    public void testPipelinedRedisCallback() {
        List<Object> res3 = stringRedisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.set("key1".getBytes(), "1".getBytes(StandardCharsets.UTF_8)); //可以操作数据
            connection.get("key2".getBytes()); // 注意这条默认会返回Long 1
            connection.del("key1".getBytes()); // 没有返回值
            return null;
        });
        Assert.assertEquals(3, res3.size());
    }

    @Test
    public void testBound() {
        BoundValueOperations<String, String> boundKey = stringRedisTemplate.boundValueOps("v1");
        Long incRes = boundKey.increment();
        String getRes = boundKey.get();
        Assert.assertEquals(incRes, Long.valueOf(getRes));
        stringRedisTemplate.delete("v1");
        Boolean res = stringRedisTemplate.hasKey("v1");
        Assert.assertTrue(res != null && !res);
    }


    @Test
    public void testTransaction() {
        try {
            // 开启事务
            stringRedisTemplate.multi();
            // 执行
            stringRedisTemplate.opsForValue().increment("count", 1);
            stringRedisTemplate.opsForValue().increment("count1", 2);
            // 提交事务
            stringRedisTemplate.exec();
        } catch (Exception e){
            // 开启回滚
            e.printStackTrace();
            stringRedisTemplate.discard();
        }
    }

    @Test
    public void testLua() {
//        redisTemplate.opsForValue().setIfAbsent("hsy", "10");
//        redisTemplate.opsForValue().setIfAbsent("zyt", "5");

        DefaultRedisScript<Map> redisScript = new DefaultRedisScript<>();
        //设置返回值类型
        redisScript.setResultType(Map.class);
        //设置lua脚本文件路径
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("test.lua")));
        List<String> keys = Arrays.asList("hsy", "zyt");
        int value = 5;
        Object executeObj = redisTemplate.execute(redisScript, keys, value);
        Assert.assertNotNull(executeObj);

//        redisTemplate.delete("hsy");
//        redisTemplate.delete("zyt");
    }

    @Test
    public void testLua2() {
//        redisTemplate.opsForValue().set("hsy", "10");
//        redisTemplate.opsForValue().set("zyt", "5");

        DefaultRedisScript<SessionCallback> redisScript = new DefaultRedisScript<>();
        //设置返回值类型
//        redisScript.setResultType(Long.class);
        //设置lua脚本文件路径
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("test2.lua")));
        List<String> keys = Arrays.asList("money");
        int value = 5;
        redisTemplate.execute(redisScript,keys,value);


//        redisTemplate.opsForValue().set("hsy", "10");
//        redisTemplate.opsForValue().set("zyt", "5");
//
//        redisTemplate.delete("hsy");
//        redisTemplate.delete("zyt");
//        redisTemplate.delete("money");
    }

    @Test
    public void testMGetException() {
        List<String> keys = Arrays.asList("hsyKey1", "hsyKey2", "hsyKey3", "hsyKey4");
        List<Object> setRes = stringRedisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                for (String key : keys) {
                    operations.opsForValue().set((K) key, (V) "1", TimeUnit.DAYS.toSeconds(30));
                }
                return null;
            }
        });
        List<String> getRes = stringRedisTemplate.opsForValue().multiGet(keys);
        Assert.assertNotNull(getRes);
    }

}