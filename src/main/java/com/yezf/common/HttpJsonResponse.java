/*
* Copyright (c) 2015 yezf.com. All Rights Reserved.
*/
package com.yezf.common;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author wuguangxu
 * @description
 * @since 1.0
 */
public class HttpJsonResponse{

    private HttpServletResponse response;

    public HttpJsonResponse(HttpServletResponse response) {
        this.response = response;
    }

    public void write(Object value) throws IOException {
        if (response == null) {
            return;
        }
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String json = JSON.toJSONString(value);
        out.println(json);
        out.close();
    }

}
