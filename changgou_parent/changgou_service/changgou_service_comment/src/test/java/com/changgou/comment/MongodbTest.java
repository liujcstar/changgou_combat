package com.changgou.comment;

import com.alibaba.fastjson.JSON;
import com.changgou.comment.dao.CommentMapper;
import com.changgou.comment.pojo.Comment;
import com.changgou.comment.pojo.CommentMysql;
import com.changgou.util.DateUtil;
import com.mongodb.BasicDBObject;
import io.netty.handler.codec.base64.Base64Encoder;
import org.apache.commons.codec.binary.Base64;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import sun.misc.BASE64Encoder;

import java.util.*;

@SuppressWarnings("all")
@SpringBootTest
@RunWith(SpringRunner.class)
public class MongodbTest {


    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void add() {

        Comment comment = new Comment();
        comment.setId("2");
        comment.setCreateTime(new Date());
        comment.setLevel("2");
        comment.setUsername("张三");


        mongoTemplate.save(comment);
        List<Comment> commentList = mongoTemplate.findAll(Comment.class);

        for (Comment comment1 : commentList) {
            System.out.println(comment1);
        }
    }


    @Test
    public void getAggBucket() {


        String alias = "num";
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("spuId").is("10000000616300")),
                Aggregation.group("level").count().as(alias)
        );
        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "changgou_comment", Map.class);
        List<Map> mappedResults = results.getMappedResults();
        if (mappedResults != null && mappedResults.size() > 0) {
//            Integer num = (Integer) mappedResults.get(0).get(alias);

            Map finMap = new HashMap();

            for (Map mappedResult : mappedResults) {
                System.out.println(mappedResult);
                Object o = mappedResult.get(alias);
                Object id = mappedResult.get("_id");
                System.out.println(id);
                System.out.println(o);
                finMap.put(id, o);
            }

//            System.out.println(num);
            System.out.println(finMap);
        }

        System.out.println(results.getMappedResults());


    }


    @Test
    public void findOneLevel() {

        Comment comment = new Comment();
        comment.setSpuId("889527500");
        comment.setLevel("2");

        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小写


        Example<Comment> example = Example.of( comment, matcher);

        Query query = new Query(Criteria.byExample(example));

        List<Comment> comments = mongoTemplate.find(query, Comment.class);
        System.out.println(comments);




    }

    @Test
    public void getAggBucket2() {


        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("spuId").is("889527500")),
                Aggregation.match(Criteria.where("level").is(2))

        );

        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "changgou_comment", Map.class);

        Document rawResults = results.getRawResults();
        System.out.println(rawResults);

        for (Map result : results) {

            System.out.println(result);
        }

        System.out.println(results.getMappedResults());

    }



    @Test
    public void glt() {


        Date date = DateUtil.addDateHour(new Date(), -24);

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("createTime").gte(date)),
                Aggregation.group("skuId").count().as("skuIdCount")
        );

        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "changgou_comment", Map.class);

        for (Map result : results) {

            System.out.println(result.get("_id"));
        }

//        List<Map> mappedResults = results.getMappedResults();
//        System.out.println(mappedResults);

        System.out.println(results.getMappedResults());

    }

    @Test
    public void findAll(){

        Query query = new Query();
        List<Comment> commentList = mongoTemplate.find(query, Comment.class);
        System.out.println(commentList) ;
    }


    @Autowired
    private CommentMapper commentMapper;

    @Test
    public void testadd(){

        CommentMysql commentMysql = new CommentMysql();
        String s = "测试";
        byte[] encode = Base64Utils.encode(s.getBytes());
        commentMysql.setId("1");
        commentMysql.setLevel("11");

        commentMysql.setContent(new String(encode));
        commentMapper.insertSelective(commentMysql);

    }


    @Test
    public void testselect(){

        CommentMysql commentMysql = commentMapper.selectByPrimaryKey("1");

        String content = commentMysql.getContent();
        byte[] decode = Base64Utils.decode(content.getBytes());
        commentMysql.setContent(new String(decode));
        System.out.println(commentMysql);
    }


    @Test
    public void update(){
        Comment comment = new Comment();

//1274532844256886784
        Query query=new Query(new Criteria("id").is("1274532844256886784"));
        Update update=new Update().set("status","0");
        mongoTemplate.updateMulti(query, update, Comment.class);
    }


    @Test
    public void test2(){
        Date date = DateUtil.addDateHour(new Date(), -24);

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("createTime").gte(date)),
                Aggregation.match(Criteria.where("status").is("1"))//0表示未同步数据

        );

        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "changgou_comment", Map.class);

        List<Map> mappedResults = results.getMappedResults();
        for (Map result : mappedResults) {
            System.out.println(result);
        }

    }


}
