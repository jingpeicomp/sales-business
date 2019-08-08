package com.jingxiang.business.tc.common.vo.order;

import com.jingxiang.business.product.base.vo.SkuVo;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 下单请求
 * Created by liuzhaoming on 2019/8/6.
 */
@Data
public class OrderCreateRequest implements Serializable {
    /**
     * 店铺ID
     */
    private String shopId;

    /**
     * 买家，无需前端传入，由订单模块从后台上下文中自动获取
     */
    private String buyer;

    /**
     * 订单收货信息
     */
    private OrderReceiverVo receiver;

    /**
     * 商品ID
     */
    private List<OrderProductParam> products;

    /**
     * 检查商品数据是否正确
     *
     * @param voList SKU值对象列表
     * @return 执行结果
     */
    public boolean checkOrderProduct(List<SkuVo> voList) {
        if (CollectionUtils.isEmpty(voList) || voList.size() != products.size()) {
            throw new IllegalArgumentException("请求中的部分商品数据不存在或不合法");
        }

        return true;
    }

    /**
     * 更新请求中的SKU信息
     *
     * @param voList SKU值对象列表
     */
    public void updateProductInfo(List<SkuVo> voList) {
        Map<String, SkuVo> voById = voList.stream().collect(Collectors.toMap(SkuVo::getId, skuVo -> skuVo));
        products.forEach(param -> param.setSkuVo(voById.get(param.getSkuId())));
    }
}
