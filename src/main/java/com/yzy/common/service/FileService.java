package com.yzy.common.service;

import com.yzy.common.domain.FileDO;

import java.util.List;
import java.util.Map;

/**
 * @title:
 * @description: 文件上传
 *
 * @package: com.yzy.common.service.FileService.java
 * @author: yzy
 * @date: 2019-09-23 09:11:45
 * @version: v1.0
 */
public interface FileService {

    FileDO get(Long id);

    List<FileDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(FileDO sysFile);

    int update(FileDO sysFile);

    int remove(Long id);

    int batchRemove(Long[] ids);

    /**
     * 判断一个文件是否存在
     *
     * @param url FileDO中存的路径
     * @return
     */
    Boolean isExist(String url);
}
