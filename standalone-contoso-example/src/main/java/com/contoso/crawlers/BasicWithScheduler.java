package com.contoso.crawlers;

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
@Crawler(name = "basicWithScheduler")
public class BasicWithScheduler extends BaseSeimiCrawler {
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
                push(Request.build(s.toString(),BasicWithScheduler::getTitle));
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

//    @Scheduled(fixedDelay = 1000)
    public void callByFixedTime(){
        logger.info("我是一个固定间隔调度器,1秒一次");
    }

//    @Scheduled(cron = "0/5 * * * * ?")
    public void callByCron(){
        logger.info("我是一个根据cron表达式执行的调度器，5秒一次");
        // 可定时发送一个Request
        // push(Request.build(startUrls()[0],"start").setSkipDuplicateFilter(true));
    }
}
