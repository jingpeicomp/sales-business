package com.jingxiang.business.tc.fsm;

import com.jingxiang.business.vo.Describable;
import lombok.Data;

/**
 * FSM上下文
 * Created by liuzhaoming on 2019/8/5.
 */
@Data
public class FsmContext<T extends Describable> {
    /**
     * 起始状态
     */
    private FsmState fromState;

    /**
     * 目标对象，一般为订单
     */
    private T target;

    /**
     * 操作角色名称
     */
    private String roleName;

    @Override
    public String toString() {
        return "FsmContext[fromState:" + fromState + ", roleName:" + roleName + ", target:" + target.id() + "]";
    }
}
