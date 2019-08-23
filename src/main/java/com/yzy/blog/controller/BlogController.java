package com.yzy.blog.controller;

import cn.hutool.core.date.DateUtil;
import com.yzy.blog.domain.ContentDO;
import com.yzy.blog.service.ContentService;
import com.yzy.common.utils.PageUtils;
import com.yzy.common.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yzy
 */
@RequestMapping("/blog")
@Controller
public class BlogController {
    @Autowired
    ContentService bContentService;

    @GetMapping()
    String blog() {
        return "blog/index/main";
    }

    @ResponseBody
    @GetMapping("/open/list")
    public PageUtils opentList(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        List<ContentDO> bContentList = bContentService.list(query);
        int total = bContentService.count(query);
        PageUtils pageUtils = new PageUtils(bContentList, total);
        return pageUtils;
    }

    @GetMapping("/open/post/{cid}")
    String post(@PathVariable("cid") Long cid, Model model) {
        ContentDO bContentDO = bContentService.get(cid);
        model.addAttribute("bContent", bContentDO);
        model.addAttribute("gtmModified", DateUtil.format(bContentDO.getGtmModified(), "yyyy-MM-dd"));
        return "blog/index/post";
    }

    @GetMapping("/open/page/{categories}")
    String about(@PathVariable("categories") String categories, Model model) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("categories", categories);
        ContentDO bContentDO = null;
        if (bContentService.list(map).size() > 0) {
            bContentDO = bContentService.list(map).get(0);
        }
        model.addAttribute("bContent", bContentDO);
        return "blog/index/post";
    }
}
