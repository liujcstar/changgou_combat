package com.changgou.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.CategoryFeign;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.service.DetailService;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
@Service
public class DetailServiceImpl implements DetailService{

    @Autowired
    private SpuFeign spuFeign;

    @Autowired
    private CategoryFeign categoryFeign;

    @Autowired
    private SkuFeign skuFeign;

    //获取静态化页面的相关数据
    public Map<String, Object> getItemData(String spuId) {
        Map<String, Object> resultMap = new HashMap<>();
        //获取spu
        Spu spu = spuFeign.findSpuById(spuId).getData();
        resultMap.put("spu",spu);
        //获取图片信息
        if (spu != null){
            if (StringUtils.isNotEmpty(spu.getImages())){
                resultMap.put("imageList",spu.getImages().split(","));
            }
        }
        //获取商品的分类信息
        Category category1 = categoryFeign.findById(spu.getCategory1Id()).getData();
        resultMap.put("category1",category1);

        Category category2 = categoryFeign.findById(spu.getCategory2Id()).getData();
        resultMap.put("category2",category2);

        Category category3 = categoryFeign.findById(spu.getCategory3Id()).getData();
        resultMap.put("category3",category3);

        //获取sku的相关信息
        List<Sku> skuList = skuFeign.findSkuListBySpuId(spuId);
        resultMap.put("skuList",skuList);

        //获取商品规格信息
        resultMap.put("specificationList", JSON.parseObject(spu.getSpecItems(),Map.class));
        return resultMap;
    }
}
