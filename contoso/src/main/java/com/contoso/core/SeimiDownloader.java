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
package com.contoso.core;

import com.contoso.http.SeimiCookie;
import com.contoso.struct.Request;
import com.contoso.struct.Response;

import java.util.List;

/**
 * @author SeimiMaster contmaster@gmail.com
 * @since 2016/6/26.
 */
public interface SeimiDownloader {
    /**
     * 处理抓取请求生成response
     * @param request 请求
     * @return {@link Response} 封装响应对象
     * @throws Exception --
     */
    Response process(Request request) throws Exception;

    /**
     * 处理meta标签refresh场景
     *
     * @param nextUrl 重定向URL
     * @return 请求的最终返回体
     * @throws Exception --
     */
    Response metaRefresh(String nextUrl) throws Exception;

    /**
     * http请求状态
     * @return http状态码
     */
    int statusCode();

    /**
     * 添加自定义cookies
     * @param url 目标地址
     * @param contCookies cookies
     */
    void addCookies(String url, List<SeimiCookie> contCookies);

}
