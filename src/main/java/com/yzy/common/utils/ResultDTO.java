package com.yzy.common.utils;

import java.util.HashMap;
import java.util.Map;

public class ResultDTO extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public ResultDTO() {
        put("code", 0);
        put("msg", "操作成功");
    }

    public static ResultDTO error() {
        return error(1, "操作失败");
    }

    public static ResultDTO error(String msg) {
        return error(500, msg);
    }

    public static ResultDTO error(int code, String msg) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.put("code", code);
        resultDTO.put("msg", msg);
        return resultDTO;
    }

    public static ResultDTO ok(String msg) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.put("msg", msg);
        return resultDTO;
    }

    public static ResultDTO ok(Map<String, Object> map) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.putAll(map);
        return resultDTO;
    }

    public static ResultDTO ok() {
        return new ResultDTO();
    }

    @Override
    public ResultDTO put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
