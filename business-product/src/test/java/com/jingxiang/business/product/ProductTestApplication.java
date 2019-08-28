package com.jingxiang.business.product;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

/**
 * 商品模块单元测试主类
 * Created by liuzhaoming on 2019/8/28.
 */
@SpringBootApplication
@ActiveProfiles("test")
@ComponentScan("com.jingxiang.business")
@EnableJpaAuditing
@EnableAspectJAutoProxy
public class ProductTestApplication {
}
