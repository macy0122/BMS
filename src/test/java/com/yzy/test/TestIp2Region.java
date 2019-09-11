package com.yzy.test;

import com.yzy.common.utils.Ip2RegionUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @title:
 * @description:
 * @package: com.yzy.test
 * @ClassName: com.yzy.test.TestIp2Region.java
 * @author: yzy
 * @date: 2019/9/10 14:37
 * @param:
 * @return:
 * @version: v1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class TestIp2Region {

	@Test
	public void testIp2region(){
		System.out.println(Ip2RegionUtil.getCityInfo("119.75.217.109"));
	}

}
