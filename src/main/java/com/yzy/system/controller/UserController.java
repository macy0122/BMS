package com.yzy.system.controller;

import com.yzy.common.annotation.Log;
import com.yzy.common.config.Constant;
import com.yzy.common.controller.BaseController;
import com.yzy.common.domain.Tree;
import com.yzy.common.service.DictService;
import com.yzy.common.utils.MD5Utils;
import com.yzy.common.utils.PageUtils;
import com.yzy.common.utils.Query;
import com.yzy.common.utils.ResultDTO;
import com.yzy.system.domain.DeptDO;
import com.yzy.system.domain.RoleDO;
import com.yzy.system.domain.UserDO;
import com.yzy.system.service.RoleService;
import com.yzy.system.service.UserService;
import com.yzy.system.vo.UserVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/sys/user")
@Controller
public class UserController extends BaseController {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    DictService dictService;
    private String prefix = "system/user";

    @RequiresPermissions("sys:user:user")
    @GetMapping("")
    String user(Model model) {
        return prefix + "/user";
    }

    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, Object> params) {
        // 查询列表数据
        Query query = new Query(params);
        List<UserDO> sysUserList = userService.list(query);
        int total = userService.count(query);
        PageUtils pageUtil = new PageUtils(sysUserList, total);
        return pageUtil;
    }

    @RequiresPermissions("sys:user:add")
    @Log("添加用户")
    @GetMapping("/add")
    String add(Model model) {
        List<RoleDO> roles = roleService.list();
        model.addAttribute("roles", roles);
        return prefix + "/add";
    }

    @RequiresPermissions("sys:user:edit")
    @Log("编辑用户")
    @GetMapping("/edit/{id}")
    String edit(Model model, @PathVariable("id") Long id) {
        UserDO userDO = userService.get(id);
        model.addAttribute("user", userDO);
        List<RoleDO> roles = roleService.list(id);
        model.addAttribute("roles", roles);
        return prefix + "/edit";
    }

    @RequiresPermissions("sys:user:add")
    @Log("保存用户")
    @PostMapping("/save")
    @ResponseBody
    ResultDTO save(UserDO user) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return ResultDTO.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        user.setPassword(MD5Utils.encrypt(user.getUsername(), user.getPassword()));
        if (userService.save(user) > 0) {
            return ResultDTO.ok();
        }
        return ResultDTO.error();
    }

    @RequiresPermissions("sys:user:edit")
    @Log("更新用户")
    @PostMapping("/update")
    @ResponseBody
    ResultDTO update(UserDO user) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return ResultDTO.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        if (userService.update(user) > 0) {
            return ResultDTO.ok();
        }
        return ResultDTO.error();
    }


    @RequiresPermissions("sys:user:edit")
    @Log("更新用户")
    @PostMapping("/updatePeronal")
    @ResponseBody
    ResultDTO updatePeronal(UserDO user) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return ResultDTO.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        if (userService.updatePersonal(user) > 0) {
            return ResultDTO.ok();
        }
        return ResultDTO.error();
    }


    @RequiresPermissions("sys:user:remove")
    @Log("删除用户")
    @PostMapping("/remove")
    @ResponseBody
    ResultDTO remove(Long id) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return ResultDTO.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        if (userService.remove(id) > 0) {
            return ResultDTO.ok();
        }
        return ResultDTO.error();
    }

    @RequiresPermissions("sys:user:batchRemove")
    @Log("批量删除用户")
    @PostMapping("/batchRemove")
    @ResponseBody
    ResultDTO batchRemove(@RequestParam("ids[]") Long[] userIds) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return ResultDTO.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        int r = userService.batchremove(userIds);
        if (r > 0) {
            return ResultDTO.ok();
        }
        return ResultDTO.error();
    }

    @PostMapping("/exit")
    @ResponseBody
    boolean exit(@RequestParam Map<String, Object> params) {
        // 存在，不通过，false
        return !userService.exit(params);
    }

    @RequiresPermissions("sys:user:resetPwd")
    @Log("请求更改用户密码")
    @GetMapping("/resetPwd/{id}")
    String resetPwd(@PathVariable("id") Long userId, Model model) {

        UserDO userDO = new UserDO();
        userDO.setUserId(userId);
        model.addAttribute("user", userDO);
        return prefix + "/reset_pwd";
    }

    @Log("提交更改用户密码")
    @PostMapping("/resetPwd")
    @ResponseBody
    ResultDTO resetPwd(UserVO userVO) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return ResultDTO.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        try {
            userService.resetPwd(userVO, getUser());
            return ResultDTO.ok();
        } catch (Exception e) {
            return ResultDTO.error(1, e.getMessage());
        }

    }

    @RequiresPermissions("sys:user:resetPwd")
    @Log("admin提交更改用户密码")
    @PostMapping("/adminResetPwd")
    @ResponseBody
    ResultDTO adminResetPwd(UserVO userVO) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return ResultDTO.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        try {
            userService.adminResetPwd(userVO);
            return ResultDTO.ok();
        } catch (Exception e) {
            return ResultDTO.error(1, e.getMessage());
        }

    }

    @GetMapping("/tree")
    @ResponseBody
    public Tree<DeptDO> tree() {
        Tree<DeptDO> tree = new Tree<DeptDO>();
        tree = userService.getTree();
        return tree;
    }

    @GetMapping("/treeView")
    String treeView() {
        return prefix + "/userTree";
    }

    @GetMapping("/personal")
    String personal(Model model) {
        UserDO userDO = userService.get(getUserId());
        model.addAttribute("user", userDO);
        model.addAttribute("hobbyList", dictService.getHobbyList(userDO));
        model.addAttribute("sexList", dictService.getSexList());
        return prefix + "/personal";
    }

    @ResponseBody
    @PostMapping("/uploadImg")
    ResultDTO uploadImg(@RequestParam("avatar_file") MultipartFile file, String avatar_data, HttpServletRequest request) {
        if ("test".equals(getUsername())) {
            return ResultDTO.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        Map<String, Object> result = new HashMap<>();
        try {
            result = userService.updatePersonalImg(file, avatar_data, getUserId());
        } catch (Exception e) {
            return ResultDTO.error("更新图像失败！");
        }
        if (result != null && result.size() > 0) {
            return ResultDTO.ok(result);
        } else {
            return ResultDTO.error("更新图像失败！");
        }
    }
}
