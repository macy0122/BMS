package com.yzy.common.utils;

import cn.hutool.core.util.StrUtil;

import javax.servlet.http.HttpServletRequest;

public class HttpServletUtils {
    public static boolean jsAjax(HttpServletRequest req) {
        //判断是否为ajax请求，默认不是
        boolean isAjaxRequest = false;
        if (!StrUtil.isBlank(req.getHeader("x-requested-with")) && req.getHeader("x-requested-with").equals("XMLHttpRequest")) {
            isAjaxRequest = true;
        }
        return isAjaxRequest;
    }
}
