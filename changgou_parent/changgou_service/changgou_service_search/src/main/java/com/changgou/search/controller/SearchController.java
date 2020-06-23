package com.changgou.search.controller;

import com.changgou.entity.Page;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.entity.Result;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;




    @GetMapping("/list")
    public String list(@RequestParam Map<String,String> searchMap,Model model){



        //特殊符号处理
        this.handleSearchMap(searchMap);


        //获取查询结果
        Map resultMap = searchService.search(searchMap);
        model.addAttribute("result",resultMap);
        model.addAttribute("searchMap",searchMap);

        //封装分页数据并返回
        //1.总记录数
        //2.当前页
        //3.每页显示多少条
        Page<SkuInfo> page = new Page<SkuInfo>(
                Long.parseLong(String.valueOf( resultMap.get("total"))),
                Integer.parseInt(String.valueOf(resultMap.get("pageNum"))),
                Page.pageSize
        );
        model.addAttribute("page",page);

        //拼装url
        StringBuilder url = new StringBuilder("/api/search/list");
        if (searchMap != null && searchMap.size()>0){
            //是由查询条件
            url.append("?");
            for (String paramKey : searchMap.keySet()) {
                if (!"sortRule".equals(paramKey) && !"sortField".equals(paramKey) && !"pageNum".equals(paramKey)){
                    url.append(paramKey).append("=").append(searchMap.get(paramKey)).append("&");
                }
            }
            //http://localhost:9009/search/list?keywords=手机&spec_网络制式=4G&
            String urlString = url.toString();
            //去除路径上的最后一个&
            urlString=urlString.substring(0,urlString.length()-1);
            model.addAttribute("url",urlString);
        }else{
            model.addAttribute("url",url);
        }
        return "search";
    }

    @Autowired
    private SkuFeign skuFeign;


    /**
     * 查询热卖商品
     * @return
     */
    @GetMapping("/hotgoods")
    @ResponseBody
    public Result hotGoods(){
        return skuFeign.hotGoods();
    }




    @GetMapping
    @ResponseBody
    public Map search(@RequestParam Map<String,String> searchMap){
        //特殊符号处理
        this.handleSearchMap(searchMap);
        Map searchResult = searchService.search(searchMap);
        return searchResult;
    }

    private void handleSearchMap(Map<String, String> searchMap) {
        Set<Map.Entry<String, String>> entries = searchMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            if (entry.getKey().startsWith("spec_")){
                searchMap.put(entry.getKey(),entry.getValue().replace("+","%2B"));
            }
        }
    }
}
