package com.jingxiang.business.user.uc.shop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 店铺数据仓库
 * Created by liuzhaoming on 2019/8/25.
 */
@Repository
public interface ShopRepository extends JpaRepository<Shop, String> {
}
