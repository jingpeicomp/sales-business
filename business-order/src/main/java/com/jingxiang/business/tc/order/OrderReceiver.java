package com.jingxiang.business.tc.order;

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
    private String receiverName;

    /**
     * 收货人电话
     */
    @Column(name = "RECEIVER_MOBILE", columnDefinition = "varchar(20) comment '收货人电话'")
    private String receiverMobile;

    /**
     * 收货人所在省份
     */
    @Column(name = "RECEIVER_PROVINCE", columnDefinition = "varchar(64) comment '收货人所在省份'")
    private String receiverProvince;

    /**
     * 收货人所在城市
     */
    @Column(name = "RECEIVER_CITY", columnDefinition = "varchar(64) comment '收货人所在城市'")
    private String receiverCity;

    /**
     * 收货人所在区县
     */
    @Column(name = "RECEIVER_DISTRICT", columnDefinition = "varchar(64) comment '收货人所在区县'")
    private String receiverDistrict;

    /**
     * 收货人所在街道
     */
    @Column(name = "RECEIVER_STREET", columnDefinition = "varchar(64) comment '收货人所在街道'")
    private String receiverStreet;

    /**
     * 收货人详细地址
     */
    @Column(name = "RECEIVER_ADDRESS", columnDefinition = "varchar(200) comment '收货人详细地址'")
    private String receiverAddress;
}
