package com.jingxiang.business.user.uc.shop;

import com.jingxiang.business.id.IdFactory;
import com.jingxiang.business.user.uc.common.consts.UserConsts;
import com.jingxiang.business.user.uc.common.vo.shop.ShopCreateRequest;
import com.jingxiang.business.user.uc.common.vo.shop.ShopUpdateRequest;
import com.jingxiang.business.user.uc.common.vo.shop.ShopVo;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 店铺
 * Created by liuzhaoming on 2019/8/25.
 */
@Data
@Entity
@Table(name = "T_BIZ_UC_SHOP")
@EntityListeners(AuditingEntityListener.class)
public class Shop implements Serializable {
    /**
     * 店铺编号
     */
    @Id
    @Column(name = "ID", columnDefinition = "varchar(32) not null comment '店铺编号'")
    private String id;

    /**
     * 店铺所有者
     */
    @Column(name = "OWNER", nullable = false, updatable = false, columnDefinition = "varchar(32) not null comment '店铺所有者'")
    private String owner;

    /**
     * 店铺关联的群ID
     */
    @Column(name = "GROUP_ID", nullable = false, updatable = false, columnDefinition = "varchar(32) not null comment '店铺关联的群ID'")
    private String groupId;

    /**
     * 店铺名称
     */
    @Column(name = "NAME", nullable = false, columnDefinition = "varchar(64) not null comment '店铺名称'")
    private String name;

    /**
     * 合伙人
     */
    @Column(name = "PARTNER", updatable = false, columnDefinition = "varchar(32) comment '合伙人'")
    private String partner;

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

    @Version
    @Column(name = "VERSION", columnDefinition = "bigint comment '版本号'")
    private Long version;

    /**
     * 生成店铺值对象
     *
     * @return 值对象
     */
    public ShopVo toVo() {
        return ShopVo.builder()
                .id(id)
                .createTime(createTime)
                .groupId(groupId)
                .name(name)
                .owner(owner)
                .partner(partner)
                .updateTime(updateTime)
                .build();
    }

    /**
     * 更新店铺信息
     *
     * @param request 店铺更新请求
     */
    public void update(ShopUpdateRequest request) {
        name = request.getName();
        partner = request.getPartner();
    }

    /**
     * 创建新的店铺
     *
     * @param request 店铺创建请求
     * @return 新创建的店铺
     */
    public static Shop from(ShopCreateRequest request) {
        Shop shop = new Shop();
        shop.setId(IdFactory.createUserId(UserConsts.ID_PREFIX_SHOP));
        shop.setGroupId(request.getGroupId());
        shop.setName(request.getName());
        shop.setOwner(request.getOwner());
        shop.setPartner(request.getPartner());
        return shop;
    }
}
