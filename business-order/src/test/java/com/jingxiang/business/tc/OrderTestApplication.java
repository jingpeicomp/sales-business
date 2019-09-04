package com.jingxiang.business.tc;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

/**
 * 订单单元测试主入口
 * Created by liuzhaoming on 2019/9/3.
 */
@SpringBootApplication
@ActiveProfiles("test")
@ComponentScan("com.jingxiang.business")
@EnableJpaAuditing
@EnableAspectJAutoProxy
public class OrderTestApplication {
}
