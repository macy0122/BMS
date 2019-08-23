package com.yzy.oa.dao;

import com.yzy.oa.domain.NotifyDO;
import com.yzy.oa.domain.NotifyDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 通知通告
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-10-05 17:11:16
 */
@Mapper
@Repository
public interface NotifyDao {

    NotifyDO get(Long id);

    List<NotifyDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(NotifyDO notify);

    int update(NotifyDO notify);

    int remove(Long id);

    int batchRemove(Long[] ids);

    List<NotifyDO> listByIds(Long[] ids);

    int countDTO(Map<String, Object> map);

    List<NotifyDTO> listDTO(Map<String, Object> map);
}
