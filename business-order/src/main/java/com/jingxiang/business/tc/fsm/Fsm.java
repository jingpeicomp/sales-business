package com.jingxiang.business.tc.fsm;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * FSM类
 * Created by liuzhaoming on 2019/8/4.
 */
@Slf4j
public class Fsm {

    /**
     * FSM配置参数
     */
    private final FsmProperties fsmProperties;

    public Fsm(FsmProperties fsmProperties) {
        this.fsmProperties = fsmProperties;
    }

    /**
     * FSM状态迁移
     *
     * @param context   FSM上下文
     * @param eventName 事件
     * @return FSM状态迁移结果
     */
    public FsmTransitionResult sendEvent(FsmContext context, String eventName) {
        log.info("FSM {} start to send event:{}, with context:{}", fsmProperties.getName(), eventName, context);
        FsmTransition transition = Optional.ofNullable(fsmProperties.findAvailableTransition(context.getFromState()))
                .orElseThrow(() -> new IllegalArgumentException("FSM" + fsmProperties.getName() +
                        "错误的状态" + context.getFromState()));
        FsmEvent event = Optional.ofNullable(transition.findAvailableEventsByRoleAndEvent(context.getRoleName(), eventName))
                .orElseThrow(() -> new IllegalArgumentException("FSM" + fsmProperties.getName() +
                        "不支持当前操作" + context.getRoleName() + eventName));

        return FsmTransitionResult.builder().fromState(context.getFromState())
                .roleName(context.getRoleName())
                .eventName(context.getRoleName())
                .toState(event.getToState())
                .build();
    }
}
