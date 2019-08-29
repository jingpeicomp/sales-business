package com.jingxiang.business.user.uc.address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 收货地址数据仓库
 * Created by liuzhaoming on 2019/8/7.
 */
@Repository
public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, String> {

    /**
     * 查询用户默认收货地址
     *
     * @param userId 用户帐号ID
     * @return 用户默认收货地址
     */
    @Query("select t from ShippingAddress t where t.userId=?1 and t.def=true")
    Optional<ShippingAddress> findDefault(String userId);

    /**
     * 根据ID查询用户收货地址
     *
     * @param userId 用户帐号ID
     * @param id     收货地址ID
     * @return 用户默认收货地址
     */
    @Query("select t from ShippingAddress t where t.id=?2 and t.userId=?1")
    Optional<ShippingAddress> findByIdAndUserId(String userId, String id);

    /**
     * 查询用户的默认地址ID
     *
     * @param userId 用户ID
     * @return 默认收货地址ID
     */
    @Query("select t.id from ShippingAddress t where t.userId=?1 and t.def=true")
    Optional<String> findDefaultId(String userId);

    /**
     * 查询用户所有收货地址
     *
     * @param userId 用户ID
     * @return 收货地址列表
     */
    List<ShippingAddress> findByUserId(String userId);
}
