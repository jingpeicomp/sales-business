package com.jingxiang.business.tc.order;

import com.jingxiang.business.tc.common.vo.order.OrderReceiverVo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * 收货人信息
 * Created by liuzhaoming on 2019/8/7.
 */
@Data
@Embeddable
public class OrderReceiver implements Serializable {
    /**
     * 收货人姓名
     */
    @Column(name = "RECEIVER_NAME", columnDefinition = "varchar(64) comment '收货人姓名'")
    private String name;

    /**
     * 收货人电话
     */
    @Column(name = "RECEIVER_MOBILE", columnDefinition = "varchar(20) comment '收货人电话'")
    private String mobile;

    /**
     * 收货人所在省份
     */
    @Column(name = "RECEIVER_PROVINCE", columnDefinition = "varchar(64) comment '收货人所在省份'")
    private String province;

    /**
     * 收货人所在城市
     */
    @Column(name = "RECEIVER_CITY", columnDefinition = "varchar(64) comment '收货人所在城市'")
    private String city;

    /**
     * 收货人所在区县
     */
    @Column(name = "RECEIVER_DISTRICT", columnDefinition = "varchar(64) comment '收货人所在区县'")
    private String district;

    /**
     * 收货人所在街道
     */
    @Column(name = "RECEIVER_STREET", columnDefinition = "varchar(64) comment '收货人所在街道'")
    private String street;

    /**
     * 收货人详细地址
     */
    @Column(name = "RECEIVER_ADDRESS", columnDefinition = "varchar(200) comment '收货人详细地址'")
    private String address;

    /**
     * 从订单收货信息VO中构造实体
     *
     * @param vo 订单收货信息value object
     * @return 订单收货信息实体
     */
    public static OrderReceiver from(OrderReceiverVo vo) {
        OrderReceiver receiver = new OrderReceiver();
        receiver.setName(vo.getName());
        receiver.setMobile(vo.getMobile());
        receiver.setProvince(vo.getProvince());
        receiver.setCity(vo.getCity());
        receiver.setDistrict(vo.getDistrict());
        receiver.setStreet(vo.getStreet());
        receiver.setAddress(vo.getAddress());

        return receiver;
    }

    /**
     * 返回订单收货信息值对象
     *
     * @return 订单收货信息值对象
     */
    public OrderReceiverVo toVo() {
        return OrderReceiverVo.builder()
                .name(name)
                .mobile(mobile)
                .province(province)
                .city(city)
                .district(district)
                .street(street)
                .address(address)
                .build();
    }
}
