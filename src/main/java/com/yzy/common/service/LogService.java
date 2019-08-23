package com.yzy.common.service;

import com.yzy.common.domain.LogDO;
import com.yzy.common.domain.PageDO;
import com.yzy.common.utils.Query;
import org.springframework.stereotype.Service;

@Service
public interface LogService {
    void save(LogDO logDO);

    PageDO<LogDO> queryList(Query query);

    int remove(Long id);

    int batchRemove(Long[] ids);
}
