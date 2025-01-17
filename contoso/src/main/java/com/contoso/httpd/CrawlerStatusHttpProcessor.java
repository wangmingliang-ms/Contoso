/*
   Copyright 2015 Wang Haomiao<contmaster@gmail.com>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.contoso.httpd;


import com.contoso.core.SeimiQueue;
import com.alibaba.fastjson.JSON;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author SeimiMaster contmaster@gmail.com
 * @since 2015/11/16.
 */
public class CrawlerStatusHttpProcessor extends HttpRequestProcessor {
    public CrawlerStatusHttpProcessor(SeimiQueue contQueue, String crawlerName) {
        super(contQueue, crawlerName);
    }

    @Override
    public void handleHttpRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json; charset=utf-8");
        Map<String, Long> body = new HashMap<>();
        body.put("queueLen", contQueue.len(crawlerName));
        body.put("totalCrawled", contQueue.totalCrawled(crawlerName));
        PrintWriter out = response.getWriter();
        out.println(JSON.toJSONString(body));
    }
}
