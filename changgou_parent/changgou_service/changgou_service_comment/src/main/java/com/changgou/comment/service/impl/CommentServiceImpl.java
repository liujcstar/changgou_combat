package com.changgou.comment.service.impl;

//import com.alibaba.fescar.spring.annotation.GlobalTransactional;
import com.changgou.comment.config.TokenDecode;
import com.changgou.comment.dao.CommentMapper;
import com.changgou.comment.pojo.Comment;
import com.changgou.comment.pojo.CommentMysql;
import com.changgou.comment.service.CommentService;
import com.changgou.util.DateUtil;
import com.changgou.util.IdWorker;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private TokenDecode tokenDecode;

    /**
     * 添加评价
     * @param spuId
     * @param content
     * @param level
     */
    @Override
    public void add(String skuId,String spuId, String content, String level) {

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setLevel(level);
        comment.setUsername("");
        comment.setSpuId(spuId);
        long id = idWorker.nextId();
        comment.setId(String.valueOf(id));
        String username = tokenDecode.getUserInfo().get("username");
        comment.setUsername(username);
        comment.setCreateTime(new Date());
        comment.setSkuId(skuId);
        comment.setStatus("0"); //0表示未同步
        mongoTemplate.save(comment);

    }

    /**
     * 根据商品id查询评价
     * @param spuId
     * @return
     */
    @Override
    public List<Comment> list(String spuId) {
        Query query = new Query(Criteria.where("spuId").is(spuId));

        List<Comment> commentList = mongoTemplate.find(query, Comment.class);

        return commentList;
    }

    /**
     * 查询评价等级
     * @return
     */
    @Override
    public List<Map> findLevel(String spuId) {

        String alias = "num";
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("spuId").is(spuId)),
                Aggregation.group("level").count().as(alias)
        );
        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "changgou_comment", Map.class);
        List<Map> mappedResults = results.getMappedResults();
        if (mappedResults != null && mappedResults.size() > 0) {
            Integer num = (Integer) mappedResults.get(0).get(alias);
            System.out.println(num);
        }

        return mappedResults;
    }

    /**
     * 查询指定好评、差评
     * @param level
     * @param spuId
     * @return
     */
    @Override
    public List<Comment> findSpuLevel(String level, String spuId) {
        Comment comment = new Comment();
        comment.setSpuId(spuId);
        comment.setLevel(level);

        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小写


        org.springframework.data.domain.Example<Comment> example = org.springframework.data.domain.Example.of( comment, matcher);

        Query query = new Query(Criteria.byExample(example));

        List<Comment> commentList = mongoTemplate.find(query, Comment.class);

        return commentList;
    }



    @Override
    public int getGoodLevel(String spuId){
        //好评数
        List<Comment> goodLevel = this.findSpuLevel("1", spuId);
        double goodSize = goodLevel.size();


        //差评数
        List<Comment> badList = this.findSpuLevel("3", spuId);
        int badSize = badList.size();

        List<Comment> allList = this.list(spuId);
        double allSize = allList.size();

        //好评计算公式


        int finalgoodLevel = (int) (goodSize / allSize * 100);

        return finalgoodLevel;

    }





    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SpuFeign spuFeign;


    @Autowired
    private CommentMapper commentMapper;

    /**
     * 更新评论数量定时任务
     */
    @Override
//    @GlobalTransactional
    public void updateCommentTask() {

        //把mongodb中满足时间条件的数据查询出来
        //mongodb评论创建时间>=当前时间-24


        //封装sku的评论数量
        List<Map> skuIdMap = this.timeAggregate("skuId").getMappedResults();
        if (skuIdMap!=null&& skuIdMap.size()>0) {
            for (Map map : skuIdMap) {
                String skuId = map.get("_id").toString();
                Integer count = (Integer) map.get("count");
                skuFeign.updateCommentCountBySkuId(skuId, count);
            }
        }


        //封装spu的评论数
        List<Map> spuIdMap = this.timeAggregate("spuId").getMappedResults();

        if (spuIdMap != null) {
            for (Map map : spuIdMap) {
                String spuId = map.get("_id").toString();
                Integer count = (Integer) map.get("count");
                spuFeign.updateCommentCountBySpuId(spuId, count);
            }
        }


        //更新数据库评论表
        List<Comment> commentList = this.findAll();
        if (commentList!=null && commentList.size()>0) {
            for (Comment comment : commentList) {
                CommentMysql commentMysql = new CommentMysql();

                BeanUtils.copyProperties(comment, commentMysql);

                //将所需要的数据编码
                String content = commentMysql.getContent();
                byte[] encode = Base64Utils.encode(content.getBytes());
                commentMysql.setContent(new String(encode));
                commentMapper.insertSelective(commentMysql);

                comment.setStatus("1");
                //改变mongodb状态
                this.update(comment);
            }
        }

      //同步成功，改变mongodb状态评论


    }

    /**
     *
     * @param groupObj ：传递分组对象
     * @return
     */
    private AggregationResults<Map> timeAggregate(String groupObj){
        //mongodb评论创建时间>=当前时间-24
        Date date = DateUtil.addDateHour(new Date(), -24);

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("createTime").gte(date)),
                Aggregation.match(Criteria.where("status").is("0")),//0表示未同步数据
                Aggregation.group(groupObj).count().as("count")

        );

        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "changgou_comment", Map.class);

        return results;

    }






    /**
     * 查询所有未同步的评价
     * @return
     */
    private List<Comment> findAll(){

        Query query = new Query(new Criteria("status").is("0"));
        List<Comment> commentList = mongoTemplate.find(query, Comment.class);
        return commentList;
    }


    /**
     * 更新mongodb数据状态
     * @param comment
     */
    private void update(Comment comment){

        Query query=new Query(new Criteria("id").is(comment.getId()));
        Update update=new Update().set("status","1");
        mongoTemplate.updateMulti(query, update, Comment.class);

    }

}
