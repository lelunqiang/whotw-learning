package com.whotw.utils;

import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author EdisonXu
 * @date 2019-08-07
 */
public class WebUtils {

    private static final String BASIC_ = "Basic ";

    /**
     * *从header 请求中的clientId:clientSecret
     */
    public static String[] extractClient(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith(BASIC_)) {
            throw new IllegalArgumentException("请求头中client信息为空");
        }
        return extractHeaderClient(header.substring(BASIC_.length()));
    }

    /**
     * 从header 请求中的clientId:clientSecret
     *
     * @param header header中的参数
     */
    public static String[] extractHeaderClient(String header) {
        byte[] base64Client = header.getBytes(StandardCharsets.UTF_8);
        byte[] decoded = Base64.getDecoder().decode(base64Client);
        String clientStr = new String(decoded, StandardCharsets.UTF_8);
        String[] clientArr = clientStr.split(":");
        if (clientArr.length != 2) {
            throw new IllegalArgumentException("Invalid basic authentication token");
        }
        return clientArr;
    }

    public static String getValueFromHeader(String headerName, HttpServletRequest request){
        return getValueFromHeader(headerName, null, request);
    }

    public static String getValueFromHeader(String headerName, String defaultValue, HttpServletRequest request){
        if(defaultValue!=null)
            return defaultValue;
        Assert.notNull(headerName, "Name of the header cannot be null!");
        String strValue = request.getHeader(headerName);
        return strValue;
    }

    public static Map<String, String> getAllHeaders(HttpServletRequest request) {
        Map<String, String> map = new HashMap();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }
}
