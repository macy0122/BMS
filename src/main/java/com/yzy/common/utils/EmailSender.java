package com.yzy.common.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.webservice.SoapClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @title:
 * @description:  邮件发送
 *
 * @package: com.yzy.common.utils.EmailSender.java
 * @param:
 * @return: 字符串："true" or "false"
 * @author: yzy
 * @date: 2019-09-12 08:22:41
 * @version: v1.0
 */

public class EmailSender {
    /**
     * @title:
     * @description:
     *
     * @param: [mailto, mailcc, mailBcc, mfrom, Subject, Bodys]
     * @return: java.lang.String  字符串："true" or "false"
     * @version: v1.0
     */
    public static String senderEmail(String mailto, String mailcc, String mailBcc, String mfrom,
                                     String Subject, String Bodys) {
        Map<String, Object> map = new HashMap<>();
        map.put("mailto", mailto);
        map.put("mailcc", mailcc);
        map.put("mailBcc", mailBcc);
        map.put("mfrom", mfrom);
        map.put("Subject", Subject);
        map.put("Bodys", Bodys);
        SoapClient client = SoapClient.create("http://10.130.14.86/Service1.asmx")
                .setMethod("web:SendMail", "http://cfag.port.com/")
                .setCharset(CharsetUtil.CHARSET_UTF_8)
                .setParams(map);
        String send = client.send(true);
        String result = StrUtil.subBetween(send, "<SendMailResult>", "</SendMailResult>");
        return result;
    }

//    @Test
//    public void fun() {
//        String b = EmailSender.senderEmail("macy0122@126.com", "", "", "cfa-it-sx@foxconn.com", "Test", "test");
//        System.out.println(b);
//    }

}
