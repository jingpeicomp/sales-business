package com.jingxiang.business.user.uc.shop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 店铺数据仓库
 * Created by liuzhaoming on 2019/8/25.
 */
@Repository
public interface ShopRepository extends JpaRepository<Shop, String> {
    /**
     * 根据群ID查询关联的店铺
     *
     * @param groupId 群ID
     * @return 群关联的店铺
     */
    Optional<Shop> findByGroupId(String groupId);

    /**
     * 查询店主
     *
     * @param id 店铺ID
     * @return 店主用户ID
     */
    @Query("select t.owner from Shop t where t.id=?1")
    String findOwnerById(String id);

    /**
     * 查询店铺合伙人
     *
     * @param id 店铺ID
     * @return 店铺合伙人ID
     */
    @Query("select t.partner from Shop t where t.id=?1")
    String findPartnerById(String id);
}
