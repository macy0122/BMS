package com.yzy.common.controller;

import com.yzy.common.config.Constant;
import com.yzy.common.domain.DictDO;
import com.yzy.common.service.DictService;
import com.yzy.common.utils.PageUtils;
import com.yzy.common.utils.Query;
import com.yzy.common.utils.ResultDTO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典表
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-29 18:28:07
 */

@Controller
@RequestMapping("/common/dict")
public class DictController extends BaseController {
    @Autowired
    private DictService dictService;

    @GetMapping()
    @RequiresPermissions("common:dict:dict")
    String dict() {
        return "common/dict/dict";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("common:dict:dict")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        // 查询列表数据
        Query query = new Query(params);
        List<DictDO> dictList = dictService.list(query);
        int total = dictService.count(query);
        PageUtils pageUtils = new PageUtils(dictList, total);
        return pageUtils;
    }

    @GetMapping("/add")
    @RequiresPermissions("common:dict:add")
    String add() {
        return "common/dict/add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("common:dict:edit")
    String edit(@PathVariable("id") Long id, Model model) {
        DictDO dict = dictService.get(id);
        model.addAttribute("dict", dict);
        return "common/dict/edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("common:dict:add")
    public ResultDTO save(DictDO dict) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return ResultDTO.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        if (dictService.save(dict) > 0) {
            return ResultDTO.ok();
        }
        return ResultDTO.error();
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("common:dict:edit")
    public ResultDTO update(DictDO dict) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return ResultDTO.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        dictService.update(dict);
        return ResultDTO.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("common:dict:remove")
    public ResultDTO remove(Long id) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return ResultDTO.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        if (dictService.remove(id) > 0) {
            return ResultDTO.ok();
        }
        return ResultDTO.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("common:dict:batchRemove")
    public ResultDTO remove(@RequestParam("ids[]") Long[] ids) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return ResultDTO.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        dictService.batchRemove(ids);
        return ResultDTO.ok();
    }

    @GetMapping("/type")
    @ResponseBody
    public List<DictDO> listType() {
        return dictService.listType();
    }

    ;

    // 类别已经指定增加
    @GetMapping("/add/{type}/{description}")
    @RequiresPermissions("common:dict:add")
    String addD(Model model, @PathVariable("type") String type, @PathVariable("description") String description) {
        model.addAttribute("type", type);
        model.addAttribute("description", description);
        return "common/dict/add";
    }

    @ResponseBody
    @GetMapping("/list/{type}")
    public List<DictDO> listByType(@PathVariable("type") String type) {
        // 查询列表数据
        Map<String, Object> map = new HashMap<>(16);
        map.put("type", type);
        List<DictDO> dictList = dictService.list(map);
        return dictList;
    }
}
