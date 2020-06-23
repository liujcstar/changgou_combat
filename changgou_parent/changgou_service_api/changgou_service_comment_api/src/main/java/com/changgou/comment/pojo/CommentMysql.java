package com.changgou.comment.pojo;


import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_comment")
public class CommentMysql {

    @Id
    private String id; //主键

    private String spuId; //评论商品id

    private String content; //评论内容

    private String username;

    private String level; //评价等级 1：好评，2：中评，3：差评

    private Date createTime; //评论时间

    private String skuId;

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpuId() {
        return spuId;
    }

    public void setSpuId(String spuId) {
        this.spuId = spuId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", spuId='" + spuId + '\'' +
                ", content='" + content + '\'' +
                ", username='" + username + '\'' +
                ", level=" + level +
                ", createTime=" + createTime +
                ", skuId='" + skuId + '\'' +
                '}';
    }
}
