package com.jingxiang.business.tc.base.configuration;

import com.jingxiang.business.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * FSM自动配置类
 * Created by liuzhaoming on 2019/8/5.
 */
@Configuration
@ConditionalOnProperty(name = "jingxiang.business.order.fsmPropertyFile")
@Slf4j
public class FsmAutoConfiguration {

    @Value("${jingxiang.business.order.fsmPropertyFile}")
    private String orderFsmPropertyFileString;

    @PostConstruct
    public void initOrderFsmFactory() {
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
    }
}
