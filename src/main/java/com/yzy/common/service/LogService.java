package com.yzy.common.service;

import com.yzy.common.domain.LogDO;
import com.yzy.common.domain.PageDO;
import com.yzy.common.utils.Query;
import org.springframework.stereotype.Service;

/**
 * @title: 
 * @description: 
 * 
 * @package: com.yzy.common.service.LogService.java
 * @author: yzy
 * @date: 2019-09-23 09:12:12
 * @version: v1.0
 */
@Service
public interface LogService {
    void save(LogDO logDO);

    PageDO<LogDO> queryList(Query query);

    int remove(Long id);

    int batchRemove(Long[] ids);
}
