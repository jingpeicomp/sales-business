package com.jingxiang.business.tc.fsm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * FSM事件
 * Created by liuzhaoming on 2019/8/4.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FsmEvent extends FsmBaseBean {

    /**
     * 不限制角色
     */
    public static String ROLE_MATCH_ALL = "*";

    /**
     * 事件名称（操作类型/命令名称）
     */
    private String name;

    /**
     * 可执行该操作的角色名称（类型）集合
     * - 为空时，表示不限定执行角色
     */
    private List<String> roleNames;

    /**
     * 执行该操作后迁移到的下一种状态
     */
    private FsmState toState;

    /**
     * 是否允许指定角色执行此事件
     *
     * @param roleName 角色名称
     * @return true-是，false-否
     */
    public boolean isRoleAllowed(String roleName) {
        return roleNames == null
                || roleNames.isEmpty()
                || roleNames.stream().anyMatch(name -> name.equals(ROLE_MATCH_ALL))
                || roleNames.stream().anyMatch(name -> name.equalsIgnoreCase(roleName));
    }

    /**
     * 是否允许执行此事件
     *
     * @param eventName 事件名称
     * @return true-是，false-否
     */
    public boolean isEventAllowed(String eventName) {
        return this.name.equalsIgnoreCase(eventName);
    }

    /**
     * 检查配置信息是否正确；如果可以纠正，就纠正，不能则应该抛出异常
     * 此方法会修改本对象
     */
    @Override
    public void checkAndCorrect() {
        if (StringUtils.isBlank(name)) {
            throw new IllegalStateException("FSM事件" + id() + "名称不能为空");
        }

        if (null == toState) {
            throw new IllegalStateException("FSM事件" + id() + "目的状态不能为空");
        }

        if (CollectionUtils.isNotEmpty(roleNames)) {
            roleNames = roleNames.stream().distinct().collect(Collectors.toList());
        }
    }

    /**
     * 获取信息标识
     *
     * @return id
     */
    @Override
    public String id() {
        return "FsmEvent:" + name + "--" + roleNames;
    }
}
