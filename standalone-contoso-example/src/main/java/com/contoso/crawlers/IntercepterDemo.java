package com.contoso.crawlers;

import com.contoso.annotations.DoLog;
import com.contoso.annotation.Crawler;
import com.contoso.def.BaseSeimiCrawler;
import com.contoso.struct.Request;
import com.contoso.struct.Response;
import org.seimicrawler.xpath.JXDocument;

import java.util.List;

/**
 * @author github.com/zhegexiaohuozi seimimaster@gmail.com
 * @since 2015/10/21.
 */
@Crawler(name = "intercept")
public class IntercepterDemo extends BaseSeimiCrawler {
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
                push(Request.build(s.toString(),IntercepterDemo::getTitle));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //该方法即可被支持@DoLog注解的拦截器所拦截，打在类上则该类的全部可被调用到的方法均会被拦截
    @DoLog
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
