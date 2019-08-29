package com.jingxiang.business.user.uc.address;

import com.jingxiang.business.id.IdFactory;
import com.jingxiang.business.user.uc.common.consts.UserConsts;
import com.jingxiang.business.user.uc.common.vo.address.ShippingAddressVo;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 收货地址
 * Created by liuzhaoming on 2019/8/7.
 */
@Entity
@Table(name = "t_biz_uc_shipping_address")
@Data
@EntityListeners(AuditingEntityListener.class)
public class ShippingAddress implements Serializable {

    /**
     * 收货地址ID
     */
    @Id
    @Column(name = "ID", columnDefinition = "varchar(32) not null comment '收货地址ID'")
    private String id;

    /**
     * 用户账户ID
     */
    @Column(name = "USER_ID", nullable = false, updatable = false, columnDefinition = "varchar(32) not null comment '用户账户ID'")
    private String userId;

    /**
     * 是否默认地址
     */
    @Column(name = "DEF", columnDefinition = "bit(1) comment '是否是默认地址'")
    private boolean def;

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

    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME", updatable = false, columnDefinition = "datetime not null default CURRENT_TIMESTAMP comment '创建时间'")
    @CreatedDate
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @LastModifiedDate
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "UPDATE_TIME", columnDefinition = "datetime comment '修改时间'")
    private LocalDateTime updateTime;

    /**
     * 版本号
     */
    @Version
    @Column(name = "VERSION", columnDefinition = "bigint comment '版本号'")
    private Long version;

    /**
     * 将收货地址实体转化为值对象
     *
     * @return 收货实体值对象
     */
    public ShippingAddressVo toVo() {
        return ShippingAddressVo.builder()
                .id(id)
                .userId(userId)
                .def(def)
                .receiverName(receiverName)
                .receiverMobile(receiverMobile)
                .receiverProvince(receiverProvince)
                .receiverCity(receiverCity)
                .receiverDistrict(receiverDistrict)
                .receiverStreet(receiverStreet)
                .receiverAddress(receiverAddress)
                .build();
    }

    public void update(ShippingAddressVo vo){
        id = vo.getId();
        userId = vo.getUserId();
        def = vo.isDef();
        receiverName = vo.getReceiverName();
        receiverMobile = vo.getReceiverMobile();
        receiverProvince = vo.getReceiverProvince();
        receiverCity = vo.getReceiverCity();
        receiverDistrict = vo.getReceiverDistrict();
        receiverStreet = vo.getReceiverStreet();
        receiverAddress =vo.getReceiverAddress();
    }

    /**
     * 从VO构造收货地址
     *
     * @param vo VO
     * @return 收货地址
     */
    public static ShippingAddress fromVo(ShippingAddressVo vo) {
        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setId(vo.getId());
        shippingAddress.setUserId(vo.getUserId());
        shippingAddress.setDef(vo.isDef());
        shippingAddress.setReceiverName(vo.getReceiverName());
        shippingAddress.setReceiverMobile(vo.getReceiverMobile());
        shippingAddress.setReceiverProvince(vo.getReceiverProvince());
        shippingAddress.setReceiverCity(vo.getReceiverCity());
        shippingAddress.setReceiverDistrict(vo.getReceiverDistrict());
        shippingAddress.setReceiverStreet(vo.getReceiverStreet());
        shippingAddress.setReceiverAddress(vo.getReceiverAddress());

        if (StringUtils.isBlank(vo.getId())) {
            shippingAddress.setId(IdFactory.createUserId(UserConsts.ID_PREFIX_SHIPPING_ADDRESS));
        }

        return shippingAddress;
    }
}
