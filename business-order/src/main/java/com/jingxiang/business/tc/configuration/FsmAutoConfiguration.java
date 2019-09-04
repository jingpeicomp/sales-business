package com.jingxiang.business.tc.configuration;

import com.jingxiang.business.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * FSM自动配置类
 * Created by liuzhaoming on 2019/8/5.
 */
@Component
//@Configuration
@Slf4j
public class FsmAutoConfiguration {

    @Value("${jingxiang.business.order.fsmPropertyFile}")
    private String orderFsmPropertyFileString;

    @Bean
    @ConditionalOnProperty(name = "jingxiang.business.order.fsmPropertyFile")
    public OrderFsmFactory initOrderFsmFactory() {
        List<String> orderTypeAndFiles = CommonUtils.splitStr(orderFsmPropertyFileString);
        List<String[]> formattedOrderTypeAndFiles = orderTypeAndFiles.stream()
                .map(orderTypeAndFileStr -> {
                    List<String> orderTypeAndFile = CommonUtils.splitStr(orderTypeAndFileStr, ":");
                    if (orderTypeAndFile.size() == 2) {
                        return orderTypeAndFile.toArray(new String[0]);
                    } else {
                        log.error("Bad order fsm config item {}", orderTypeAndFile);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        OrderFsmFactory.init(formattedOrderTypeAndFiles);
        return OrderFsmFactory.INSTANCE;
    }
}
