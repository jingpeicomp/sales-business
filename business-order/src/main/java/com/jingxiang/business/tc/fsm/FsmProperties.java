package com.jingxiang.business.tc.fsm;

import com.jingxiang.business.utils.CommonUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.MapUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 有限状态机配置类
 * Created by liuzhaoming on 2019/8/4.
 */
@EqualsAndHashCode(callSuper = false)
public class FsmProperties extends FsmBaseBean {
    /**
     * 有限状态机规则名称
     */
    @Getter
    @Setter
    private String name;

    /**
     * 有限状态机状态迁移表
     */
    @Getter
    @Setter
    private List<FsmTransition> transitions;

    /**
     * 有限状态机状态-迁移映射
     */
    private Map<FsmState, FsmTransition> transitionMap;

    /**
     * 加载FSM配置
     *
     * @param filePath 配置文件路径
     * @return FSM配置
     */
    public static FsmProperties load(String filePath) {
        FsmProperties fsmConfig = Optional.ofNullable(CommonUtils.fromJsonFile(filePath, FsmProperties.class))
                .orElseThrow(() -> new IllegalArgumentException("无法加载FSM配置文件：" + filePath));
        fsmConfig.checkAndCorrect();
        return fsmConfig;
    }

    /**
     * 检查配置信息是否正确；如果可以纠正，就纠正，不能则应该抛出异常
     * 此方法会修改本对象
     */
    @Override
    public void checkAndCorrect() {
        if (MapUtils.isNotEmpty(transitionMap)) {
            Map<FsmState, FsmTransition> stateTransitionMap = new HashMap<>();
            transitions.forEach(fsmTransition -> {
                FsmTransition transition = stateTransitionMap.putIfAbsent(fsmTransition.getFromState(), fsmTransition);
                if (Objects.isNull(transition)) {
                    fsmTransition.checkAndCorrect();
                }
            });
            transitionMap = Collections.unmodifiableMap(stateTransitionMap);
        }
    }

    /**
     * 获取信息标识
     *
     * @return id
     */
    @Override
    public String id() {
        return "Fsm:" + name;
    }

    /**
     * 获取指定状态的可执行的状态迁移定义
     *
     * @param fromState 当前状态
     * @return 可执行的状态迁移定义
     */
    public FsmTransition findAvailableTransition(FsmState fromState) {
        return transitionMap.get(fromState);
    }

    /**
     * 获取指定状态的下一步可执行的操作集合
     *
     * @param fromState 当前状态
     * @return 下一步可执行的操作集合
     */
    public List<FsmEvent> findAvailableEvents(FsmState fromState) {
        FsmTransition transition = findAvailableTransition(fromState);
        if (Objects.isNull(transition)) {
            return Collections.emptyList();
        }

        return transition.getEvents();
    }

    /**
     * 获取指定角色在指定状态的下一步可执行的操作集合
     *
     * @param fromState 当前状态
     * @param roleName  角色名称
     * @return 下一步可执行的操作集合
     */
    public List<FsmEvent> findAvailableEventsByRole(FsmState fromState, String roleName) {
        return Optional.ofNullable(findAvailableTransition(fromState))
                .map(fsmTransition -> fsmTransition.findAvailableEventsByRole(roleName))
                .orElse(Collections.emptyList());
    }

    /**
     * 获取指定角色在指定状态的下一步可执行的操作事件名称集合
     *
     * @param fromState 当前状态
     * @param roleName  角色名称
     * @return 下一步可执行的操作事件名称集合
     */
    public List<String> findAvailableEventNamesByRole(FsmState fromState, String roleName) {
        return findAvailableEventsByRole(fromState, roleName).stream()
                .map(FsmEvent::getName)
                .collect(Collectors.toList());
    }
}
