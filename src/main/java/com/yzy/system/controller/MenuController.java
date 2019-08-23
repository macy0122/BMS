package com.yzy.system.controller;

import com.yzy.common.annotation.Log;
import com.yzy.common.config.Constant;
import com.yzy.common.controller.BaseController;
import com.yzy.common.domain.Tree;
import com.yzy.common.utils.ResultDTO;
import com.yzy.system.domain.MenuDO;
import com.yzy.system.service.MenuService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author yzy 1992lcg@163.com
 */
@RequestMapping("/sys/menu")
@Controller
public class MenuController extends BaseController {
    String prefix = "system/menu";
    @Autowired
    MenuService menuService;

    @RequiresPermissions("sys:menu:menu")
    @GetMapping()
    String menu(Model model) {
        return prefix + "/menu";
    }

    @RequiresPermissions("sys:menu:menu")
    @RequestMapping("/list")
    @ResponseBody
    List<MenuDO> list(@RequestParam Map<String, Object> params) {
        List<MenuDO> menus = menuService.list(params);
        return menus;
    }

    @Log("添加菜单")
    @RequiresPermissions("sys:menu:add")
    @GetMapping("/add/{pId}")
    String add(Model model, @PathVariable("pId") Long pId) {
        model.addAttribute("pId", pId);
        if (pId == 0) {
            model.addAttribute("pName", "根目录");
        } else {
            model.addAttribute("pName", menuService.get(pId).getName());
        }
        return prefix + "/add";
    }

    @Log("编辑菜单")
    @RequiresPermissions("sys:menu:edit")
    @GetMapping("/edit/{id}")
    String edit(Model model, @PathVariable("id") Long id) {
        MenuDO mdo = menuService.get(id);
        Long pId = mdo.getParentId();
        model.addAttribute("pId", pId);
        if (pId == 0) {
            model.addAttribute("pName", "根目录");
        } else {
            model.addAttribute("pName", menuService.get(pId).getName());
        }
        model.addAttribute("menu", mdo);
        return prefix + "/edit";
    }

    @Log("保存菜单")
    @RequiresPermissions("sys:menu:add")
    @PostMapping("/save")
    @ResponseBody
    ResultDTO save(MenuDO menu) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return ResultDTO.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        if (menuService.save(menu) > 0) {
            return ResultDTO.ok();
        } else {
            return ResultDTO.error(1, "保存失败");
        }
    }

    @Log("更新菜单")
    @RequiresPermissions("sys:menu:edit")
    @PostMapping("/update")
    @ResponseBody
    ResultDTO update(MenuDO menu) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return ResultDTO.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        if (menuService.update(menu) > 0) {
            return ResultDTO.ok();
        } else {
            return ResultDTO.error(1, "更新失败");
        }
    }

    @Log("删除菜单")
    @RequiresPermissions("sys:menu:remove")
    @PostMapping("/remove")
    @ResponseBody
    ResultDTO remove(Long id) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return ResultDTO.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        if (menuService.remove(id) > 0) {
            return ResultDTO.ok();
        } else {
            return ResultDTO.error(1, "删除失败");
        }
    }

    @GetMapping("/tree")
    @ResponseBody
    Tree<MenuDO> tree() {
        Tree<MenuDO> tree = menuService.getTree();
        return tree;
    }

    @GetMapping("/tree/{roleId}")
    @ResponseBody
    Tree<MenuDO> tree(@PathVariable("roleId") Long roleId) {
        Tree<MenuDO> tree = menuService.getTree(roleId);
        return tree;
    }
}
