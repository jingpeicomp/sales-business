package com.jingxiang.business.user.uc.address;

import com.jingxiang.business.id.IdFactory;
import com.jingxiang.business.user.uc.common.vo.address.UserConsts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 收货地址业务类
 * Created by liuzhaoming on 2019/8/7.
 */
@Service
@Slf4j
public class ShippingAddressService {

    @Autowired
    private ShippingAddressRepository repository;

    /**
     * 查询用户默认收货地址
     *
     * @param accountId 用户帐号ID
     * @return 默认收货地址
     */
    public Optional<ShippingAddress> queryDefault(String accountId) {
        return repository.findDefault(accountId);
    }

    /**
     * 查询用户所有收货地址
     *
     * @param accountId 用户ID
     * @return 收货地址列表
     */
    public List<ShippingAddress> query(String accountId) {
        return repository.findByAccountId(accountId);
    }

    /**
     * 保存收货地址，
     * 1. 如果新增收货地址， 用户没有收货地址，则该地址为默认收货地址（不管请求参数是什么）
     * 2. 如果修改收货地址，且数据库中该地址是默认地址，则该地址一定是默认地址（不允许修改为非默认）
     *
     * @param shippingAddress 收货地址
     * @return 保存后的收货地址
     */
    public ShippingAddress save(ShippingAddress shippingAddress) {
        if (StringUtils.isBlank(shippingAddress.getId())) {
            if (!shippingAddress.isDef()) {
                List<ShippingAddress> addresses = repository.findByAccountId(shippingAddress.getAccountId());
                if (CollectionUtils.isEmpty(addresses)) {
                    //没有收货地址，该地址为默认收货地址
                    shippingAddress.setDef(true);
                }
            }
            shippingAddress.setId(IdFactory.createUserId(UserConsts.ID_PREFIX_SHIPPING_ADDRESS));
        } else {
            Optional<ShippingAddress> addressOptional = repository.findDefault(shippingAddress.getAccountId());
            if (!addressOptional.isPresent() || addressOptional.get().getId().equals(shippingAddress.getId())) {
                shippingAddress.setDef(true);
            }
        }

        return repository.save(shippingAddress);
    }
}
