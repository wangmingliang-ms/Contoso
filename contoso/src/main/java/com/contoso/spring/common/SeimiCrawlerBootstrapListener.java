package com.contoso.spring.common;

import com.contoso.Constants;
import com.contoso.config.SeimiConfig;
import com.contoso.core.SeimiProcessor;
import com.contoso.def.BaseSeimiCrawler;
import com.contoso.exception.SeimiInitExcepiton;
import com.contoso.spring.boot.CrawlerProperties;
import com.contoso.struct.CrawlerModel;
import com.contoso.utils.StrFormatUtil;
import com.contoso.utils.SysEnvUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: github.com/zhegexiaohuozi contmaster@gmail.com
 * @since 2018/5/7.
 */
public class SeimiCrawlerBootstrapListener implements ApplicationListener<ContextRefreshedEvent> {

    private ExecutorService workersPool;
    private boolean isSpringBoot = false;

    public SeimiCrawlerBootstrapListener(){
        super();
    }

    public SeimiCrawlerBootstrapListener(boolean isSpringBoot) {
        super();
        this.isSpringBoot = isSpringBoot;
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        if (isSpringBoot){
            CrawlerProperties crawlerProperties = context.getBean(CrawlerProperties.class);
            if (!crawlerProperties.isEnabled()){
                logger.warn("{} is not enabled",Constants.SEIMI_CRAWLER_BOOTSTRAP_ENABLED);
                return;
            }
        }

        if (context != null) {
            if (CollectionUtils.isEmpty(CrawlerCache.getCrawlers())) {
                logger.info("Not find any crawler,may be you need to check.");
                return;
            }
            int customThreadNum = SysEnvUtil.customThreadNum();
            int poolSize = customThreadNum * CrawlerCache.getCrawlers().size();
            if (customThreadNum <= 0){
                poolSize = Constants.BASE_THREAD_NUM * Runtime.getRuntime().availableProcessors() * CrawlerCache.getCrawlers().size();
                customThreadNum = Constants.BASE_THREAD_NUM * Runtime.getRuntime().availableProcessors();
            }
            workersPool = Executors.newFixedThreadPool(poolSize);
            for (Class<? extends BaseSeimiCrawler> a : CrawlerCache.getCrawlers()) {
                CrawlerModel crawlerModel = new CrawlerModel(a, context);
                if (CrawlerCache.isExist(crawlerModel.getCrawlerName())) {
                    logger.error("Crawler:{} is repeated,please check", crawlerModel.getCrawlerName());
                    throw new SeimiInitExcepiton(StrFormatUtil.info("Crawler:{} is repeated,please check", crawlerModel.getCrawlerName()));
                }
                CrawlerCache.putCrawlerModel(crawlerModel.getCrawlerName(), crawlerModel);
            }

            for (Map.Entry<String, CrawlerModel> crawlerEntry : CrawlerCache.getCrawlerModelContext().entrySet()) {
                for (int i = 0; i < customThreadNum; i++) {
                    workersPool.execute(new SeimiProcessor(CrawlerCache.getInterceptors(), crawlerEntry.getValue()));
                }
            }

            if (isSpringBoot){
                CrawlerProperties crawlerProperties = context.getBean(CrawlerProperties.class);
                String crawlerNames = crawlerProperties.getNames();
                if (StringUtils.isBlank(crawlerNames)){
                    logger.info("Spring boot start [{}] as worker.",StringUtils.join(CrawlerCache.getCrawlerModelContext().keySet(),","));
                }else {
                    String[] crawlers = crawlerNames.split(",");
                    for (String cn:crawlers){
                        CrawlerModel crawlerModel = CrawlerCache.getCrawlerModel(cn);
                        if (crawlerModel == null){
                            logger.warn("Crawler name = {} is not existent.",cn);
                            continue;
                        }
                        crawlerModel.startRequest();
                    }
                }
                //统一通用配置信息至 seimiConfig
                SeimiConfig config = new SeimiConfig();
                config.setBloomFilterExpectedInsertions(crawlerProperties.getBloomFilterExpectedInsertions());
                config.setBloomFilterFalseProbability(crawlerProperties.getBloomFilterFalseProbability());
                config.setSeimiAgentHost(crawlerProperties.getSeimiAgentHost());
                config.setSeimiAgentPort(crawlerProperties.getSeimiAgentPort());
                CrawlerCache.setConfig(config);
            }
        }
    }
}
