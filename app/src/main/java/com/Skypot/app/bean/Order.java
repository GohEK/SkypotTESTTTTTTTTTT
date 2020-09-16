package com.Skypot.app.bean;

public class Order {
    private Integer id;
    private Integer commid;
    private String uid;

    public Order() {
    }

    public Order(Integer commid, String uid) {
        this.commid = commid;
        this.uid = uid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCommid() {
        return commid;
    }

    public void setCommid(Integer commid) {
        this.commid = commid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
