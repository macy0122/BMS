package com.yzy.oa.service.impl;

import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateUtil;
import com.yzy.common.service.DictService;
import com.yzy.common.utils.PageUtils;
import com.yzy.oa.dao.NotifyDao;
import com.yzy.oa.dao.NotifyRecordDao;
import com.yzy.oa.domain.NotifyDO;
import com.yzy.oa.domain.NotifyDTO;
import com.yzy.oa.domain.NotifyRecordDO;
import com.yzy.oa.service.NotifyService;
import com.yzy.system.dao.UserDao;
import com.yzy.system.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class NotifyServiceImpl implements NotifyService {
    @Autowired
    private NotifyDao notifyDao;
    @Autowired
    private NotifyRecordDao recordDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private DictService dictService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private SimpMessagingTemplate template;

    @Override
    public NotifyDO get(Long id) {
        NotifyDO rDO = notifyDao.get(id);
        rDO.setType(dictService.getName("oa_notify_type", rDO.getType()));
        return rDO;
    }

    @Override
    public List<NotifyDO> list(Map<String, Object> map) {
        List<NotifyDO> notifys = notifyDao.list(map);
        for (NotifyDO notifyDO : notifys) {
            notifyDO.setType(dictService.getName("oa_notify_type", notifyDO.getType()));
        }
        return notifys;
    }

    @Override
    public int count(Map<String, Object> map) {
        return notifyDao.count(map);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int save(NotifyDO notify) {
        notify.setUpdateDate(new Date());
        int r = notifyDao.save(notify);
        // 保存到接受者列表中
        Long[] userIds = notify.getUserIds();
        Long notifyId = notify.getId();
        List<NotifyRecordDO> records = new ArrayList<>();
        for (Long userId : userIds) {
            NotifyRecordDO record = new NotifyRecordDO();
            record.setNotifyId(notifyId);
            record.setUserId(userId);
            record.setIsRead(0);
            records.add(record);
        }
        recordDao.batchSave(records);
        //给在线用户发送通知
//        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
//        executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                for (UserDO userDO : sessionService.listOnlineUser()) {
//                    for (Long userId : userIds) {
//                        if (userId.equals(userDO.getUserId())) {
//                            template.convertAndSendToUser(userDO.toString(), "/queue/notifications", "新消息：" + notify.getTitle());
//                        }
//                    }
//                }
//            }
//        });
//        executor.shutdown();
        return r;
    }

    @Override
    public int update(NotifyDO notify) {
        return notifyDao.update(notify);
    }

    @Transactional
    @Override
    public int remove(Long id) {
        recordDao.removeByNotifbyId(id);
        return notifyDao.remove(id);
    }

    @Transactional
    @Override
    public int batchRemove(Long[] ids) {
        recordDao.batchRemoveByNotifbyId(ids);
        return notifyDao.batchRemove(ids);
    }


    @Override
    public PageUtils selfList(Map<String, Object> map) {
        List<NotifyDTO> rows = notifyDao.listDTO(map);
        for (NotifyDTO notifyDTO : rows) {

            notifyDTO.setBefore(DateUtil.formatBetween(notifyDTO.getUpdateDate(), Calendar.getInstance().getTime(), BetweenFormater.Level.DAY));
            notifyDTO.setSender(userDao.get(notifyDTO.getCreateBy()).getName());
        }
        PageUtils page = new PageUtils(rows, notifyDao.countDTO(map));
        return page;
    }

}
