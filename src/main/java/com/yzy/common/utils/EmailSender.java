package com.yzy.common.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.webservice.SoapClient;

import java.util.HashMap;
import java.util.Map;

/**
 * describe:
 *
 * @author yzy
 * @date 2019/06/19
 * @return 字符串："true" or "false"
 */
public class EmailSender {
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
