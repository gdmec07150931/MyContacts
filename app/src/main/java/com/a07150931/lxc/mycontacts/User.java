package com.a07150931.lxc.mycontacts;

/**
 * Created by asus1 on 2016/10/25.
 */
public class User {

    // 字段名常量；
    public final static String NAME ="name";
    public final static String DANWEI = "danwei";
    public final static String MOBILE = "mobile";
    public final static String QQ = "qq";
    public final static String ADDRESS = "address";

    //类属性；
    private String name;
    private String danwei;
    private String modile;
    private String qq;
    private String address;
    private int id_DB = -1;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDanwei() {
        return danwei;
    }

    public void setDanwei(String danwei) {
        this.danwei = danwei;
    }

    public String getModile() {
        return modile;
    }

    public void setModile(String modile) {
        this.modile = modile;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId_DB() {
        return id_DB;
    }

    public void setId_DB(int id_DB) {
        this.id_DB = id_DB;
    }
}



























