package com.jingxiang.business.tc.fsm;

import com.jingxiang.business.vo.Describable;

import java.io.Serializable;

/**
 * FSM基础Bean
 * Created by liuzhaoming on 2019/8/5.
 */
public class FsmBaseBean implements Serializable, Describable {
    /**
     * 检查配置信息是否正确；如果可以纠正，就纠正，不能则应该抛出异常
     * 此方法会修改本对象
     */
    public void checkAndCorrect() {
    }

    /**
     * 获取信息标识
     *
     * @return id
     */
    @Override
    public String id() {
        return toString();
    }
}
