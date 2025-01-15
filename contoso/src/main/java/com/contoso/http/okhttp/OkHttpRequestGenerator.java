package com.contoso.http.okhttp;

import com.contoso.config.SeimiConfig;
import com.contoso.def.BaseSeimiCrawler;
import com.contoso.exception.SeimiProcessExcepiton;
import com.contoso.http.HttpMethod;
import com.contoso.http.SeimiAgentContentType;
import com.contoso.spring.common.CrawlerCache;
import com.contoso.struct.CrawlerModel;
import com.alibaba.fastjson.JSON;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @author SeimiMaster contmaster@gmail.com
 * @since 2016/6/26.
 */
public class OkHttpRequestGenerator {
    public static Request.Builder getOkHttpRequesBuilder(com.contoso.struct.Request contReq, CrawlerModel crawlerModel){
        BaseSeimiCrawler crawler = crawlerModel.getInstance();
        Request.Builder requestBuilder = new Request.Builder();
        if (contReq.isUseSeimiAgent()){
            SeimiConfig config = CrawlerCache.getConfig();
            if (config==null||StringUtils.isBlank(config.getSeimiAgentHost())) {
                throw new SeimiProcessExcepiton("SeimiAgentHost is blank.");
            }
            String contAgentUrl = "http://" + config.getSeimiAgentHost() + (config.getSeimiAgentPort() != 80 ? (":" + config.getSeimiAgentPort()) : "") + "/doload";
            FormBody.Builder formBodyBuilder = new FormBody.Builder()
                    .add("url", contReq.getUrl());
            if (StringUtils.isNotBlank(crawler.proxy())){
                formBodyBuilder.add("proxy", crawler.proxy());
            }
            if (contReq.getSeimiAgentRenderTime() > 0){
                formBodyBuilder.add("renderTime", String.valueOf(contReq.getSeimiAgentRenderTime()));
            }
            if (StringUtils.isNotBlank(contReq.getSeimiAgentScript())){
                formBodyBuilder.add("script", contReq.getSeimiAgentScript());
            }
            //如果针对SeimiAgent的请求设置是否使用cookie，以针对请求的设置为准，默认使用全局设置
            if ((contReq.isSeimiAgentUseCookie() == null && crawlerModel.isUseCookie()) || (contReq.isSeimiAgentUseCookie() != null && contReq.isSeimiAgentUseCookie())) {
                formBodyBuilder.add("useCookie", "1");
            }
            if (contReq.getParams() != null && contReq.getParams().size() > 0) {
                formBodyBuilder.add("postParam", JSON.toJSONString(contReq.getParams()));
            }
            if (contReq.getSeimiAgentContentType().val()> SeimiAgentContentType.HTML.val()){
                formBodyBuilder.add("contentType",contReq.getSeimiAgentContentType().typeVal());
            }
            requestBuilder.url(contAgentUrl).post(formBodyBuilder.build()).build();
        }else {
            requestBuilder.url(contReq.getUrl());
            requestBuilder.header("User-Agent", crawlerModel.isUseCookie() ? crawlerModel.getCurrentUA() : crawler.getUserAgent())
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
            //自定义header
            if (!CollectionUtils.isEmpty(contReq.getHeader())) {
                for (Map.Entry<String,String> entry:contReq.getHeader().entrySet()) {
                    requestBuilder.addHeader(entry.getKey(), entry.getValue());
                }
            }
            if (HttpMethod.POST.equals(contReq.getHttpMethod())) {
                if (StringUtils.isNotBlank(contReq.getJsonBody())){
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),contReq.getJsonBody());
                    requestBuilder.post(requestBody);
                }else {
                    FormBody.Builder formBodyBuilder = new FormBody.Builder();
                    if (contReq.getParams() != null) {
                        for (Map.Entry<String, String> entry : contReq.getParams().entrySet()) {
                            formBodyBuilder.add(entry.getKey(), entry.getValue());
                        }
                    }
                    requestBuilder.post(formBodyBuilder.build());
                }
            } else {
                String queryStr = "";
                if (contReq.getParams()!=null&&!contReq.getParams().isEmpty()){
                    queryStr += "?";
                    for (Map.Entry<String, String> entry : contReq.getParams().entrySet()) {
                        queryStr= queryStr+entry.getKey()+"="+entry.getValue()+"&";
                    }
                    requestBuilder.url(contReq.getUrl()+queryStr);
                }
                requestBuilder.get();
            }
        }
        return requestBuilder;
    }
}
