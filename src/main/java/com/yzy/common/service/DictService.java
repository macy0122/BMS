package com.yzy.common.service;

import com.yzy.common.domain.DictDO;
import com.yzy.system.domain.UserDO;

import java.util.List;
import java.util.Map;

/**
 * @title:
 * @description: 字典表
 *
 * @package: com.yzy.common.service.DictService.java
 * @author: yzy
 * @date: 2019-09-23 09:11:58
 * @version: v1.0
 */
public interface DictService {

    DictDO get(Long id);

    List<DictDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(DictDO dict);

    int update(DictDO dict);

    int remove(Long id);

    int batchRemove(Long[] ids);

    List<DictDO> listType();

    String getName(String type, String value);

    /**
     * 获取爱好列表
     *
     * @param userDO
     * @return
     */
    List<DictDO> getHobbyList(UserDO userDO);

    /**
     * 获取性别列表
     *
     * @return
     */
    List<DictDO> getSexList();

    /**
     * 根据type获取数据
     *
     * @param map
     * @return
     */
    List<DictDO> listByType(String type);

}
