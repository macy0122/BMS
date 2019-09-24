/**
 *
 */
package com.yzy.common.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @title:
 * @description:
 *
 * @package: com.yzy.common.service.GeneratorService.java
 * @author: yzy
 * @date: 2019-09-23 09:11:25
 * @version: v1.0
 */
@Service
public interface GeneratorService {
    List<Map<String, Object>> list();

    byte[] generatorCode(String[] tableNames);
}
