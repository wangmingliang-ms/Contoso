package com.contoso.crawlers;

import com.contoso.annotation.Crawler;
import com.contoso.def.BaseSeimiCrawler;
import com.contoso.http.SeimiAgentContentType;
import com.contoso.struct.Request;
import com.contoso.struct.Response;
import org.apache.commons.lang3.StringUtils;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;
import org.seimicrawler.xpath.exception.XpathSyntaxErrorException;

import java.util.LinkedList;
import java.util.List;

/**
 * 演示通过SeimiAgent来获取京东联盟数据
 * @author github.com/zhegexiaohuozi [seimimaster@gmail.com]
 * @since 2016/8/24.
 */
@Crawler(name = "jdWalker",httpTimeOut = 30000)
public class JDWalker extends BaseSeimiCrawler {

    @Override
    public String[] startUrls() {
        return null;
    }

    @Override
    public List<Request> startRequests() {
        List<Request> requests = new LinkedList<>();
        Request request = Request.build("https://passport.jd.com/uc/login",JDWalker::start)
                .useSeimiAgent()
                .setSeimiAgentUseCookie(true)
                .setSeimiAgentRenderTime(6000)
                .setSeimiAgentContentType(SeimiAgentContentType.HTML)
                .setSeimiAgentScript("$(\"#loginname\").val(\"guigacpyx\");$(\"#nloginpwd\").val(\"xxxxx\");$(\".login-btn>a\").click();");
        requests.add(request);
        return requests;
    }
    @Override
    public void start(Response response) {
        JXDocument document = response.document();
        try {
            logger.info("login head name = {}", StringUtils.join(document.sel("//li[@id='ttbar-login']/a/text()"),""));
            Request request = Request.build("https://media.jd.com/gotoadv/goods",JDWalker::getProductList)
                    .useSeimiAgent()
                    .setSeimiAgentUseCookie(true)
                    .setSeimiAgentRenderTime(5000)
                    .setSeimiAgentContentType(SeimiAgentContentType.HTML);
            push(request);
        } catch (XpathSyntaxErrorException e) {
            logger.debug(e.getMessage(),e);
        }
    }
    public void getProductList(Response response){
        JXDocument jxDocument = response.document();
        try {
            List<JXNode> nodeList = jxDocument.selN("//tr/td/div[@class='dis_inline_k offset20 dis_ine_p_k']");
            for (JXNode jxNode:nodeList){
                logger.info(jxNode.toString());
            }
        }catch (Exception e){
            logger.debug(e.getMessage(),e);
        }
    }
}
