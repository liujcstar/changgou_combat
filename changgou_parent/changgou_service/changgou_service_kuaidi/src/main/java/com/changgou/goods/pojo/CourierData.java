package com.changgou.goods.pojo;

public class CourierData {
//     时间
     public String datetime;
//     状态
    public String remark;
//    城市
    public String zone;

    public String getDatatime() {
        return  datetime;
    }

    public void setDatatime(String datetime ) {
        this.datetime=datetime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    @Override
    public String toString() {
        return "CourierData{" +
                "datetime='" + datetime + '\'' +
                ", remark='" + remark + '\'' +
                ", zone='" + zone + '\'' +
                '}';
    }
}
