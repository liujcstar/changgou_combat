package com.changgou.test;

import com.changgou.comment.pojo.CommentMysql;
import com.changgou.util.IdWorker;
import com.changgou.goods.dao.SpuMapper;
import com.changgou.goods.pojo.Spu;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class IdTest {

    public static void main(String[] args) {
        IdWorker idWorker = new IdWorker(1,1);

        for(int i=0;i<1000;i++){
            long id = idWorker.nextId();
            System.out.println(id);
        }

    }

    @Autowired
    private SpuMapper spuMapper;

    @Test
    public void testAdd(){

        Spu spu = new Spu();
        spu.setName("测试");
        spu.setId("111");

        spuMapper.insertSelective(spu);
    }



    @Test
    public void testAddd(){
        CommentMysql commentMysql = new CommentMysql();

        System.out.println(commentMysql);
    }
}
