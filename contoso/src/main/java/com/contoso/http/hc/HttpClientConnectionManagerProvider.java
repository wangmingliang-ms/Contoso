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
package com.contoso.http.hc;


import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * @author github.com/zhegexiaohuozi contmaster@gmail.com
 *         Date: 2014/11/13.
 */
public class HttpClientConnectionManagerProvider {
    private static class HCConnectionManagerProviderHolder {
        public static HttpClientCMPBox httpClientCMPBox = new HttpClientCMPBox();
    }
    public static PoolingHttpClientConnectionManager getHcPoolInstance(){
        return HCConnectionManagerProviderHolder.httpClientCMPBox.instance();
    }
}
