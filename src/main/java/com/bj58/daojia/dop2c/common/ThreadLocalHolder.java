/*
* Copyright (c) 2015 daojia.com. All Rights Reserved.
*/
package com.bj58.daojia.dop2c.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wuguangxu
 * @description 线程变量管理器
 * @since 1.0
 */
public class ThreadLocalHolder {

    private static ThreadLocal<String> localToken = new ThreadLocal<String>();
    private static ThreadLocal<HttpServletRequest> localRequest = new ThreadLocal<HttpServletRequest>();
    private static ThreadLocal<HttpServletResponse> localResponse = new ThreadLocal<HttpServletResponse>();

    public static void setToken(String token) {
        localToken.set(token);
    }

    public static String getToken() {
        return localToken.get();
    }

    public static void removeToken() {
        localToken.remove();
    }

    public static void setRequest(HttpServletRequest request) {
        localRequest.set(request);
    }

    public static void removeRequest() {
        localRequest.remove();
    }

    public static HttpServletResponse getResponse() {
        return localResponse.get();
    }

    public static void setResponse(HttpServletResponse response) {
        localResponse.set(response);
    }

    public static void removeResponse() {
        localResponse.remove();
    }

    public static HttpServletRequest getRequest() {
        return localRequest.get();
    }


}
