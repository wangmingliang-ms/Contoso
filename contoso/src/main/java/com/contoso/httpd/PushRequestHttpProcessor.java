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
import com.contoso.struct.Request;
import com.contoso.utils.StructValidator;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class PushRequestHttpProcessor extends HttpRequestProcessor {
    private final static String HTTP_API_REQ_DATA_PARAM_KEY = "req";
    private Logger logger = LoggerFactory.getLogger(getClass());
    public PushRequestHttpProcessor(SeimiQueue contQueue, String crawlerName) {
        super(contQueue,crawlerName);
    }

    @Override
    public void handleHttpRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json; charset=utf-8");
        String contReq = request.getParameter(HTTP_API_REQ_DATA_PARAM_KEY);
        Map<String,String> body = new HashMap<>();
        int processCount = 0;
        try {
            Object json = JSON.parse(contReq);
            if (JSONArray.class.equals(json.getClass())){
                JSONArray ja = (JSONArray) json;
                for (int i=0;i<ja.size();i++){
                    Request srq = ja.getObject(i,Request.class);
                    pushRequest(srq);
                }
                processCount = ja.size();
            }else if (JSONObject.class.equals(json.getClass())){
                Request srq = JSON.parseObject(contReq,Request.class);
                pushRequest(srq);
                processCount = 1;
            }

            body.put("msg","ok");
            body.put("total",String.valueOf(processCount));
            body.put("code","0");
        }catch (Exception e){
            logger.error("parse Seimi request error,receive data={}",contReq,e);
            body.put("msg","err:"+e.getMessage());
            body.put("code","1");
        }
        PrintWriter out = response.getWriter();
        out.println(JSON.toJSONString(body));
    }

    private void pushRequest(Request contRequest){
        contRequest.setCrawlerName(crawlerName);
        if (StructValidator.validateAnno(contRequest)){
            contQueue.push(contRequest);
            logger.info("Receive an request from http api,request={}",JSON.toJSONString(contRequest));
        }else {
            logger.warn("SeimiRequest={} is illegal",JSON.toJSONString(contRequest));
        }
    }
}
