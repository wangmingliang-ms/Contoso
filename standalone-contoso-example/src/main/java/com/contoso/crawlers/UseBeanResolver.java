package com.contoso.crawlers;

import com.contoso.model.BlogContent;
import com.contoso.annotation.Crawler;
import com.contoso.struct.Request;
import com.contoso.struct.Response;
import com.contoso.def.BaseSeimiCrawler;
import org.seimicrawler.xpath.JXDocument;

import java.util.List;

/**
 * @author github.com/zhegexiaohuozi seimimaster@gmail.com
 * @since 2015/10/21.
 */
@Crawler(name = "BeanResolver")
public class UseBeanResolver extends BaseSeimiCrawler {
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
                push(Request.build(s.toString(),UseBeanResolver::renderBean));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void renderBean(Response response){
        try {
            BlogContent blog = response.render(BlogContent.class);
            logger.info("bean resolve res:{}",blog);
            //接下来拿着个bean可以该干嘛干嘛去了，比如存储到DB中
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
