package com.yzy.common.service.impl;

import com.yzy.common.dao.LogDao;
import com.yzy.common.domain.LogDO;
import com.yzy.common.domain.PageDO;
import com.yzy.common.service.LogService;
import com.yzy.common.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    LogDao logMapper;

    @Async
    @Override
    public void save(LogDO logDO) {
        logMapper.save(logDO);
    }

    @Override
    public PageDO<LogDO> queryList(Query query) {
        int total = logMapper.count(query);
        List<LogDO> logs = logMapper.list(query);
        PageDO<LogDO> page = new PageDO<>();
        page.setTotal(total);
        page.setRows(logs);
        return page;
    }

    @Override
    public int remove(Long id) {
        int count = logMapper.remove(id);
        return count;
    }

    @Override
    public int batchRemove(Long[] ids) {
        return logMapper.batchRemove(ids);
    }
}
