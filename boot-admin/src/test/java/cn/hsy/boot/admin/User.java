package cn.hsy.boot.admin;

/**
 * 用户
 *
 * @author hesiyuan
 * @date 2022-09-01 11:48 AM
 **/

public class User {

    public String payNum;

    public User(String payNum) {
        this.payNum = payNum;
    }

    public String getPayNum() {
        return payNum;
    }

    public void setPayNum(String payNum) {
        this.payNum = payNum;
    }

}
