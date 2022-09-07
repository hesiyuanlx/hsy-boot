package cn.hsy.boot.admin;

import lombok.SneakyThrows;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;

/**
 * apo测试
 *
 * @author hesiyuan
 * @date 2022-09-01 11:47 AM
 **/
public class AopTest {

    @SneakyThrows
    public static void main(String[] args) {
        Constructor<AdminUserService> constructor1 = AdminUserService.class.getDeclaredConstructor();
        ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();
        Constructor constructor = reflectionFactory.newConstructorForSerialization(AdminUserService.class);
        AdminUserService adminUserService = (AdminUserService) constructor.newInstance();
        System.out.println(adminUserService.adminUser.payNum);
    }

}
