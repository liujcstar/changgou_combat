package com.changgou.user.pojo;

import javax.persistence.Table;
import java.io.Serializable;

@Table(name="tb_user_sku")
public class Collection implements Serializable{
    private String username;
    private String skuId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }
}
