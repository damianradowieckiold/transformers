package com.radowiecki.transformers.config;

import com.radowiecki.transformers.cron.CronConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAspectJAutoProxy
@Import({HibernateConfiguration.class, CronConfiguration.class})
public class MainConfiguration {
}