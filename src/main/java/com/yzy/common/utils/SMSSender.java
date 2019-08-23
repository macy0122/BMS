package com.yzy.common.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.webservice.SoapClient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 手机短信发送
 */
@Component
public class SMSSender {

    public boolean sendSMS(String tel, String contents) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("userName", "GWCAI");
        map.put("systemName", "ecusApp");
        map.put("phonenumbers", tel);
        map.put("contents", String.valueOf(contents));
        SoapClient client = SoapClient.create("http://10.130.14.103/SMS/SMSSender.asmx")
                .setMethod("web:SendSMS", "http://tempuri.org/")
                .setCharset(CharsetUtil.CHARSET_UTF_8)
                .setParams(map);
        String result = client.sendForMessage().getSOAPBody().getTextContent();
        return "0".equals(result);
    }
//
//    public static void main(String[] args) {
//        boolean b = sendSMS("15234159749");
//    }

}
