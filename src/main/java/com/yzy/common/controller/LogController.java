package com.yzy.common.controller;

import com.yzy.common.domain.LogDO;
import com.yzy.common.domain.PageDO;
import com.yzy.common.service.LogService;
import com.yzy.common.utils.Query;
import com.yzy.common.utils.ResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/common/log")
@Controller
public class LogController {
    @Autowired
    LogService logService;
    String prefix = "common/log";

    @GetMapping()
    String log() {
        return prefix + "/log";
    }

    @ResponseBody
    @GetMapping("/list")
    PageDO<LogDO> list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        PageDO<LogDO> page = logService.queryList(query);
        return page;
    }

    @ResponseBody
    @PostMapping("/remove")
    ResultDTO remove(Long id) {
        if (logService.remove(id) > 0) {
            return ResultDTO.ok();
        }
        return ResultDTO.error();
    }

    @ResponseBody
    @PostMapping("/batchRemove")
    ResultDTO batchRemove(@RequestParam("ids[]") Long[] ids) {
        int r = logService.batchRemove(ids);
        if (r > 0) {
            return ResultDTO.ok();
        }
        return ResultDTO.error();
    }
}
