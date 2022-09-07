package cn.hsy.boot.admin;

import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 测试类
 *
 * @author hesiyuan
 * @date 2022-09-01 11:47 AM
 **/
@Service
public class AdminUserService implements Serializable {

    public final User adminUser = new User("202101166");

    public void login() {
        System.out.println("admin user login...");
    }
}