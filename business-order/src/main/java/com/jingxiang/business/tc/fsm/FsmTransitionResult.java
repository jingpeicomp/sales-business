package com.jingxiang.business.tc.fsm;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * FSM迁移结果
 * Created by liuzhaoming on 2019/8/5.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class FsmTransitionResult extends FsmBaseBean {
    /**
     * 事件名称（操作类型）
     */
    private String eventName;

    /**
     * 执行该操作的角色名称（类型）
     */
    private String roleName;

    /**
     * 迁移前状态
     */
    private FsmState fromState;

    /**
     * 迁移后状态
     */
    private FsmState toState;
}
