package com.yzy.common.utils;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.img.ImgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author yzy
 */
@Component
public class RandomValidateCodeUtil {

    public static final String RANDOMCODEKEY = "RANDOMVALIDATECODEKEY";//放到session中的key
    private static final int width = 95;// 图片宽
    private static final int height = 25;// 图片高
    private static final int lineSize = 50;// 干扰线数量
    private static final int stringNum = 4;// 随机产生字符数量

    private static final Logger logger = LoggerFactory.getLogger(RandomValidateCodeUtil.class);

    /**
     * 生成随机码字符串图片并回写到前端页面
     */
    public void getRandcode(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(width, height, stringNum, lineSize);
        session.removeAttribute(RANDOMCODEKEY);
        session.setAttribute(RANDOMCODEKEY, lineCaptcha.getCode());
        try {
            // 将内存中的图片通过流动形式输出到客户端
            ImgUtil.write(lineCaptcha.getImage(), "JPEG", response.getOutputStream());
        } catch (Exception e) {
            logger.error("将内存中的图片通过流动形式输出到客户端失败>>>> ", e);
        }

    }
}
