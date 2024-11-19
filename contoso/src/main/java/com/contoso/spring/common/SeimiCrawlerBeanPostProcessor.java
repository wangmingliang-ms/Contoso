package com.contoso.spring.common;

import com.contoso.annotation.Crawler;
import com.contoso.annotation.Interceptor;
import com.contoso.core.SeimiInterceptor;
import com.contoso.def.BaseSeimiCrawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author: github.com/zhegexiaohuozi contmaster@gmail.com
 * @since 2018/5/7.
 */
public class SeimiCrawlerBeanPostProcessor implements BeanPostProcessor {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class beanClass = bean.getClass();
        Crawler crawler = (Crawler) beanClass.getAnnotation(Crawler.class);
        if (crawler != null) {
            if (BaseSeimiCrawler.class.isAssignableFrom(beanClass)) {
                CrawlerCache.addCrawler(beanClass);
            } else {
                logger.error("Crawler={} not extends {@link def.com.contoso.BaseSeimiCrawler}", beanClass.getName());
            }
        }
        Interceptor interceptor = (Interceptor) beanClass.getAnnotation(Interceptor.class);
        if (interceptor != null) {
            if (SeimiInterceptor.class.isAssignableFrom(beanClass)) {
                CrawlerCache.addInterceptor((SeimiInterceptor) bean);
            } else {
                logger.error("find class = {}, has @Interceptor but not implement core.com.contoso.SeimiInterceptor", beanClass.getName());
            }
        }
        return bean;
    }
}
