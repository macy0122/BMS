package com.yzy.blog.controller;

import com.yzy.blog.domain.ContentDO;
import com.yzy.blog.service.ContentService;
import com.yzy.common.config.Constant;
import com.yzy.common.controller.BaseController;
import com.yzy.common.utils.PageUtils;
import com.yzy.common.utils.Query;
import com.yzy.common.utils.ResultDTO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 文章内容
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-09 10:03:34
 */
@Controller
@RequestMapping("/blog/bContent")
public class ContentController extends BaseController {
    @Autowired
    ContentService bContentService;

    @GetMapping()
    @RequiresPermissions("blog:bContent:bContent")
    String bContent() {
        return "blog/bContent/bContent";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("blog:bContent:bContent")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        List<ContentDO> bContentList = bContentService.list(query);
        int total = bContentService.count(query);
        PageUtils pageUtils = new PageUtils(bContentList, total);
        return pageUtils;
    }

    @GetMapping("/add")
    @RequiresPermissions("blog:bContent:add")
    String add() {
        return "blog/bContent/add";
    }

    @GetMapping("/edit/{cid}")
    @RequiresPermissions("blog:bContent:edit")
    String edit(@PathVariable("cid") Long cid, Model model) {
        ContentDO bContentDO = bContentService.get(cid);
        model.addAttribute("bContent", bContentDO);
        return "blog/bContent/edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions("blog:bContent:add")
    @PostMapping("/save")
    public ResultDTO save(ContentDO bContent) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return ResultDTO.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        if (bContent.getAllowComment() == null) {
            bContent.setAllowComment(0);
        }
        if (bContent.getAllowFeed() == null) {
            bContent.setAllowFeed(0);
        }
        if (null == bContent.getType()) {
            bContent.setType("article");
        }
        bContent.setGtmCreate(new Date());
        bContent.setGtmModified(new Date());
        int count;
        if (bContent.getCid() == null || "".equals(bContent.getCid())) {
            count = bContentService.save(bContent);
        } else {
            count = bContentService.update(bContent);
        }
        if (count > 0) {
            return ResultDTO.ok().put("cid", bContent.getCid());
        }
        return ResultDTO.error();
    }

    /**
     * 修改
     */
    @RequiresPermissions("blog:bContent:edit")
    @ResponseBody
    @RequestMapping("/update")
    public ResultDTO update(ContentDO bContent) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return ResultDTO.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        bContent.setGtmCreate(new Date());
        bContentService.update(bContent);
        return ResultDTO.ok();
    }

    /**
     * 删除
     */
    @RequiresPermissions("blog:bContent:remove")
    @PostMapping("/remove")
    @ResponseBody
    public ResultDTO remove(Long id) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return ResultDTO.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        if (bContentService.remove(id) > 0) {
            return ResultDTO.ok();
        }
        return ResultDTO.error();
    }

    /**
     * 删除
     */
    @RequiresPermissions("blog:bContent:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    public ResultDTO remove(@RequestParam("ids[]") Long[] cids) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return ResultDTO.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        bContentService.batchRemove(cids);
        return ResultDTO.ok();
    }
}
