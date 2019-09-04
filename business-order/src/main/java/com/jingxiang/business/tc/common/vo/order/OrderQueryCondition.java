package com.jingxiang.business.tc.common.vo.order;

import com.jingxiang.business.base.QueryCondition;
import com.jingxiang.business.tc.order.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Example;

/**
 * 订单查询参数
 * Created by liuzhaoming on 2019/8/21.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderQueryCondition implements QueryCondition<Order> {

    /**
     * 订单状态
     *
     * @see com.jingxiang.business.tc.common.consts.OrderStatus
     */
    private int orderStatus;

    /**
     * 店铺ID
     */
    private String shopId;

    /**
     * 买家ID
     */
    private String buyer;

    /**
     * 页数，从0开始
     */
    @Builder.Default
    private int page = 0;

    /**
     * 每页条数,
     */
    @Builder.Default
    private int size = 10;

    @Override
    public Example<Order> generate() {
        return null;
    }
}
