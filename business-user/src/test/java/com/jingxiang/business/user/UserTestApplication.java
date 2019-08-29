package com.jingxiang.business.user;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

/**
 * 会员单元测试启动类
 * Created by liuzhaoming on 2019/8/29.
 */
@SpringBootApplication
@ActiveProfiles("test")
@ComponentScan("com.jingxiang.business")
@EnableJpaAuditing
@EnableAspectJAutoProxy
public class UserTestApplication {
}
