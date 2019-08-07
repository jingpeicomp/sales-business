package com.jingxiang.business.tc.fsm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

/**
 * FSM状态
 * Created by liuzhaoming on 2019/8/4.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FsmState extends FsmBaseBean {
    /**
     * 总体状态
     */
    private String state;

    /**
     * 子状态
     */
    private String[] subStates;

    /**
     * 是否为简单状态（即子状态集合为空）
     *
     * @return true-简单状态，false-复杂状态
     */
    public boolean isSimple() {
        return ArrayUtils.isEmpty(subStates);
    }

    /**
     * 是否为复杂状态（即子状态集合不为空）
     *
     * @return true-复杂状态，false-简单状态
     */
    public boolean isComplex() {
        return !isSimple();
    }

    /**
     * 状态描述
     * - 简单状态：总体状态
     * - 复杂状态：总体状态(子状态1,子状态2,子状态3)
     *
     * @return 状态描述
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder(state != null ? state : "");

        if (isComplex()) {
            b.append("[\"").append(String.join("\", \"", subStates)).append("\"]");
        }

        return b.toString();
    }

    /**
     * 如果是简单状态，则只比较总体状态；否则，如果是复杂状态，则只比较子状态集合（假设每种子状态是按固定顺序存放的）
     *
     * @param o 对象
     * @return 比较结果
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof FsmState)) {
            return false;
        }

        FsmState fsmState = (FsmState) o;

        // 如果是简单状态，则只比较总体状态
        if (isSimple()) {
            return state.equals(fsmState.state);
        }

        // 否则，如果是复杂状态，则只比较子状态集合
        if (fsmState.isSimple()) {
            return false;
        }
        if (subStates.length != fsmState.subStates.length) {
            return false;
        }
        // 假设每种子状态是按固定顺序存放的
        for (int i = 0; i < subStates.length; i++) {
            if (!subStates[i].equals(fsmState.subStates[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        if (isSimple()) {
            result = 31 * result + state.hashCode();
        } else {
            result = 31 * result + Arrays.hashCode(subStates);
        }
        return result;
    }
}
