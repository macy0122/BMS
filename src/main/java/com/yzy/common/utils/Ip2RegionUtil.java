package com.yzy.common.utils;

import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;

import java.io.File;
import java.lang.reflect.Method;

/**
 * @title: Ip2RegionUtil
 * @description:  ip2region:可以根据他获取一个具体ip的信息，国家、具体地址、网络服务商
 * @package: com.yzy.common.utils
 * @ClassName: com.yzy.common.utils.Ip2RegionUtil.java
 * @author: yzy
 * @date: 2019/9/10 14:22
 * @param:
 * @return:
 * @version: v1.0
 */
public class Ip2RegionUtil {
	public static String getCityInfo(String ip) {
		//db
		String dbPath = Ip2RegionUtil.class.getResource("/ip2region.db").getPath();
		File file = new File(dbPath);
		if (file.exists() == false) {
			System.out.println("Error: Invalid ip2region.db file");
		}
		//查询算法
		int algorithm = DbSearcher.BTREE_ALGORITHM; //B-tree
		//DbSearcher.BINARY_ALGORITHM //Binary
		//DbSearcher.MEMORY_ALGORITYM //Memory
		try {
			DbConfig config = new DbConfig();
			DbSearcher searcher = new DbSearcher(config, dbPath);
			//define the method
			Method method = null;
			switch (algorithm) {
				case DbSearcher.BTREE_ALGORITHM:
					method = searcher.getClass().getMethod("btreeSearch", String.class);
					break;
				case DbSearcher.BINARY_ALGORITHM:
					method = searcher.getClass().getMethod("binarySearch", String.class);
					break;
				case DbSearcher.MEMORY_ALGORITYM:
					method = searcher.getClass().getMethod("memorySearch", String.class);
					break;
			}
			DataBlock dataBlock = null;
			if (Util.isIpAddress(ip) == false) {
				System.out.println("Error: Invalid ip address");
			}
			dataBlock = (DataBlock) method.invoke(searcher, ip);
			return dataBlock.getRegion();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
