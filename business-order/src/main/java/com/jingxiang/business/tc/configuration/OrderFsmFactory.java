package com.jingxiang.business.tc.configuration;

import com.jingxiang.business.tc.fsm.Fsm;
import com.jingxiang.business.tc.fsm.FsmProperties;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 订单有限状态机工厂类
 * Created by liuzhaoming on 2019/8/5.
 */
public class OrderFsmFactory {
    /**
     * 订单类型
     */
    private static final Map<String, Fsm> orderTypeNameAndFsm = new ConcurrentHashMap<>();

    private OrderFsmFactory() {
    }

    /**
     * 初始化工厂类，放在spring context加载后执行，通过auto config启动
     *
     * @param orderTypeAndFiles 订单类型和对应的json文件
     */
    static void init(List<String[]> orderTypeAndFiles) {
        orderTypeAndFiles.forEach(orderTypeAndFile -> {
            FsmProperties fsmProperties = FsmProperties.load(orderTypeAndFile[1]);
            orderTypeNameAndFsm.put(orderTypeAndFile[0], new Fsm(fsmProperties));
        });

    }

    /**
     * 获取对应订单类型的有限状态机
     *
     * @param orderTypeName 订单类型名称
     * @return 有限状态机
     */
    public static Fsm getFsm(String orderTypeName) {
        return orderTypeNameAndFsm.get(orderTypeName);
    }
}
