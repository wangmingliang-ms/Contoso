package com.contoso.controller;

import com.contoso.spring.common.CrawlerCache;
import com.contoso.struct.CrawlerModel;
import com.contoso.struct.Request;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author github.com/zhegexiaohuozi seimimaster@gmail.com
 */
@RestController
@RequestMapping(value = "/cont")
public class IndexController {

    @RequestMapping(value = "/info/{cname}")
    public String crawler(@PathVariable String cname) {
        CrawlerModel model = CrawlerCache.getCrawlerModel(cname);
        if (model == null) {
            return "not find " + cname;
        }
        return model.queueInfo();
    }

    @RequestMapping(value = "send_req")
    public String sendRequest(Request request){
        CrawlerCache.consumeRequest(request);
        return "consume suc";
    }
}
