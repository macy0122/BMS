package com.yzy.common.exception;

import com.yzy.common.config.Constant;
import com.yzy.common.domain.LogDO;
import com.yzy.common.service.LogService;
import com.yzy.common.utils.HttpServletUtils;
import com.yzy.common.utils.ResultDTO;
import com.yzy.common.utils.ShiroUtils;
import com.yzy.system.domain.UserDO;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 异常处理器
 */
@RestControllerAdvice
public class BDExceptionHandler {
    @Autowired
    LogService logService;
    private Logger logger = LoggerFactory.getLogger(getClass());
//
//    /**
//     * 自定义异常
//     */
//    @ExceptionHandler(BDException.class)
//    public ResultDTO handleBDException(BDException e) {
//        logger.error(e.getMessage(), e);
//        ResultDTO r = new ResultDTO();
//        r.put("code", e.getCode());
//        r.put("msg", e.getMessage());
//        return r;
//    }
//
//    @ExceptionHandler(DuplicateKeyException.class)
//    public ResultDTO handleDuplicateKeyException(DuplicateKeyException e) {
//        logger.error(e.getMessage(), e);
//        return ResultDTO.error("数据库中已存在该记录");
//    }
//
//    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
//    public ResultDTO noHandlerFoundException(org.springframework.web.servlet.NoHandlerFoundException e) {
//        logger.error(e.getMessage(), e);
//        return ResultDTO.error(404, "没找找到页面");
//    }

    @ExceptionHandler(AuthorizationException.class)
    public Object handleAuthorizationException(AuthorizationException e, HttpServletRequest request) {
        logger.error(e.getMessage(), e);
        if (HttpServletUtils.jsAjax(request)) {
            return ResultDTO.error(403, "未授权");
        }
        return new ModelAndView("error/403");
    }


    @ExceptionHandler({Exception.class})
    public Object handleException(Exception e, HttpServletRequest request) {
        LogDO logDO = new LogDO();
        logDO.setGmtCreate(new Date());
        logDO.setOperation(Constant.LOG_ERROR);
        logDO.setMethod(request.getRequestURL().toString());
        logDO.setParams(e.toString());
        UserDO current = ShiroUtils.getUser();
        if (null != current) {
            logDO.setUserId(current.getUserId());
            logDO.setUsername(current.getUsername());
        }
        logService.save(logDO);
        logger.error(e.getMessage(), e);
        if (HttpServletUtils.jsAjax(request)) {
            return ResultDTO.error(500, "服务器错误，请联系管理员");
        }
        return new ModelAndView("error/500");
    }
}
