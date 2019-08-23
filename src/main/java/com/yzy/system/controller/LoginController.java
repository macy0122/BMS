package com.yzy.system.controller;

import cn.hutool.core.util.StrUtil;
import com.yzy.common.annotation.Log;
import com.yzy.common.config.BmsConfig;
import com.yzy.common.controller.BaseController;
import com.yzy.common.domain.FileDO;
import com.yzy.common.domain.Tree;
import com.yzy.common.service.FileService;
import com.yzy.common.utils.MD5Utils;
import com.yzy.common.utils.RandomValidateCodeUtil;
import com.yzy.common.utils.ResultDTO;
import com.yzy.common.utils.ShiroUtils;
import com.yzy.system.domain.MenuDO;
import com.yzy.system.service.MenuService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class LoginController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MenuService menuService;
    @Autowired
    FileService fileService;
    @Autowired
    BmsConfig bmsConfig;
    @Autowired
    RandomValidateCodeUtil randomValidateCodeUtil;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping({"/", "", "/login"})
    String welcome(Model model) {
        model.addAttribute("username", bmsConfig.getUsername());
        model.addAttribute("password", bmsConfig.getPassword());
        return "login";
    }

    @Log("请求访问主页")
    @GetMapping({"/index"})
    String index(Model model) {
        List<Tree<MenuDO>> menus = menuService.listMenuTree(getUserId());
        model.addAttribute("menus", menus);
        model.addAttribute("name", getUser().getName());
        FileDO fileDO = fileService.get(getUser().getPicId());
        if (fileDO != null && fileDO.getUrl() != null) {
            if (fileService.isExist(fileDO.getUrl())) {
                model.addAttribute("picUrl", fileDO.getUrl());
            } else {
                model.addAttribute("picUrl", "/img/photo_s.jpg");
            }
        } else {
            model.addAttribute("picUrl", "/img/photo_s.jpg");
        }
        model.addAttribute("username", getUser().getUsername());
        return "index_v1";
    }

    @Log("登录")
    @PostMapping("/login")
    @ResponseBody
    ResultDTO ajaxLogin(String username, String password, String verify, HttpServletRequest request) {
        try {
            //从session中获取随机数
            String random = (String) request.getSession().getAttribute(RandomValidateCodeUtil.RANDOMCODEKEY);
            if (StrUtil.isBlank(verify)) {
                return ResultDTO.error("请输入验证码");
            }
            if (random.equals(verify)) {
            } else {
                return ResultDTO.error("请输入正确的验证码");
            }
        } catch (Exception e) {
            logger.error("验证码校验失败", e);
            return ResultDTO.error("验证码校验失败");
        }
        password = MD5Utils.encrypt(username, password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            return ResultDTO.ok();
        } catch (AuthenticationException e) {
            return ResultDTO.error("用户或密码错误");
        }
    }

    @GetMapping("/logout")
    String logout() {
        ShiroUtils.logout();
        return "redirect:/login";
    }

    @GetMapping("/main")
    String main() {
        return "main";
    }

    /**
     * 生成验证码
     */
    @GetMapping(value = "/getVerify")
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            randomValidateCodeUtil.getRandcode(request, response);//输出验证码图片方法

            //从session中获取随机数
            String random = (String) request.getSession().getAttribute(RandomValidateCodeUtil.RANDOMCODEKEY);
            //手机验证码发送
            rabbitTemplate.convertAndSend("login_exchange", "login_binding", "18635583249," + random);
            logger.info("随机字符串: {},已发送！", random);

        } catch (Exception e) {
            logger.error("获取验证码失败>>>> ", e);
        }
    }

}
