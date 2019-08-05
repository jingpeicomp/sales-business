package com.jingxiang.business.tc.fsm;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * FSM状态迁移
 * Created by liuzhaoming on 2019/8/4.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FsmTransition extends FsmBaseBean {
    /**
     * 当前状态
     */
    private FsmState fromState;

    /**
     * 下一步可执行的操作集合
     */
    private List<FsmEvent> events;


    /**
     * 获取指定角色在指定状态的下一步可执行的操作集合
     *
     * @param roleName 角色名称
     * @return 下一步可执行的操作集合
     */
    public List<FsmEvent> findAvailableEventsByRole(String roleName) {
        return events.stream()
                .filter(fsmEvent -> fsmEvent.isRoleAllowed(roleName))
                .collect(Collectors.toList());
    }

    /**
     * 获取指定角色在指定状态的下一步可执行的操作事件
     *
     * @param roleName  角色名称
     * @param eventName 事件名称
     * @return 下一步可执行的操作事件, null-无
     */
    public FsmEvent findAvailableEventsByRoleAndEvent(String roleName, String eventName) {
        return findAvailableEventsByRole(roleName).stream()
                .filter(fsmEvent -> fsmEvent.isEventAllowed(eventName))
                .findFirst()
                .orElse(null);
    }


    @Override
    public String id() {
        return "FsmTransition[fromState:" + fromState + "]";
    }

    @Override
    public void checkAndCorrect() {
        if (Objects.isNull(events) || events.isEmpty()) {
            throw new IllegalStateException("FSM" + id() + "事件为空");
        }

        events = events.stream()
                .peek(FsmEvent::checkAndCorrect)
                .distinct()
                .collect(Collectors.toList());
    }
}
