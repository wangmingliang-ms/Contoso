package com.contoso.spring.boot;

import com.contoso.Constants;
import com.contoso.annotation.EnableSeimiCrawler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author: github.com/zhegexiaohuozi contmaster@gmail.com
 * @since 2018/5/7.
 */
@Configuration
@ConditionalOnProperty(name = {Constants.SEIMI_CRAWLER_BOOTSTRAP_ENABLED})
@EnableConfigurationProperties({CrawlerProperties.class})
@ComponentScan({"**/crawlers", "**/queues", "**/interceptors", "com.contoso.cont"})
@EnableSeimiCrawler
public class SeimiCrawlerAutoConfiguration {
}
