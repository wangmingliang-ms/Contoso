package com.contoso.spring.common;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author 汪浩淼  contmaster@gmail.com
 * @since 2018/5/29.
 */
public class StandaloneCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return !CrawlerCache.isSpringBoot();
    }
}
