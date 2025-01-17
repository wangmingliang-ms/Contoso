package com.contoso.crawlers;

import com.contoso.annotation.Crawler;
import com.contoso.struct.Request;
import com.contoso.struct.Response;
import com.contoso.def.BaseSeimiCrawler;
import org.seimicrawler.xpath.JXDocument;

import java.util.List;

/**
 * 用注解进行配置,设置后所有的请求都会通过该代理进行请求
 * e.g. http://user:passwd@host:port
 *      https://user:passwd@host:port
 *      socket://user:passwd@host:port
 * @author github.com/zhegexiaohuozi contmaster@gmail.com
 * @since 2015/10/21.
 */
@Crawler(name = "useproxy",proxy = "socket://127.0.0.1:8888")
public class UseProxy extends BaseSeimiCrawler {
    @Override
    public String[] startUrls() {
        return new String[]{"http://www.cnblogs.com/"};
    }

    @Override
    public void start(Response response) {
        JXDocument doc = response.document();
        try {
            List<Object> urls = doc.sel("//a[@class='titlelnk']/@href");
            logger.info("{}", urls.size());
            for (Object s:urls){
                push(new Request(s.toString(),UseProxy::getTitle));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getTitle(Response response){
        JXDocument doc = response.document();
        try {
            logger.info("url:{} {}", response.getUrl(), doc.sel("//h1[@class='postTitle']/a/text()|//a[@id='cb_post_title_url']/text()"));
            //do something
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
