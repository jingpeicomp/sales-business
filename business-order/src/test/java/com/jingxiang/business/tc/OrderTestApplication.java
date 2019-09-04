package com.jingxiang.business.tc;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

/**
 * 订单单元测试主入口
 * Created by liuzhaoming on 2019/9/3.
 */
@SpringBootApplication(scanBasePackages = "com.jingxiang.business")
@ActiveProfiles("test")
@EnableJpaAuditing
@EnableAspectJAutoProxy
@EnableJpaRepositories(basePackages = "com.jingxiang.business")
@EntityScan(basePackages = "com.jingxiang.business")
public class OrderTestApplication {
}
