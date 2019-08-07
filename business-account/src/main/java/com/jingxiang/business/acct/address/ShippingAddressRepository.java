package com.jingxiang.business.acct.address;

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
     * @param accountId 用户帐号ID
     * @return 用户默认收货地址
     */
    @Query("select t from ShippingAddress t where t.accountId=?1 and t.def=true")
    Optional<ShippingAddress> findDefault(String accountId);

    /**
     * 查询用户所有收货地址
     *
     * @param accountId 用户ID
     * @return 收货地址列表
     */
    List<ShippingAddress> findByAccountId(String accountId);
}