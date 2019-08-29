package com.jingxiang.business.user.uc.address;

import com.jingxiang.business.id.IdFactory;
import com.jingxiang.business.user.uc.common.consts.UserConsts;
import com.jingxiang.business.user.uc.common.vo.address.ShippingAddressVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @param userId 用户帐号ID
     * @return 默认收货地址
     */
    @Transactional(readOnly = true)
    public Optional<ShippingAddress> queryDefault(String userId) {
        return repository.findDefault(userId);
    }

    /**
     * 查询用户所有收货地址
     *
     * @param userId 用户ID
     * @return 收货地址列表
     */
    @Transactional(readOnly = true)
    public List<ShippingAddress> query(String userId) {
        return repository.findByUserId(userId);
    }

    /**
     * 保存收货地址，
     * 1. 如果新增收货地址， 用户没有收货地址，则该地址为默认收货地址（不管请求参数是什么）
     * 2. 如果修改收货地址，且数据库中该地址是默认地址，则该地址一定是默认地址（不允许修改为非默认）
     *
     * @param vo 收货地址
     * @return 保存后的收货地址
     */
    @Transactional(timeout = 100)
    public ShippingAddress save(ShippingAddressVo vo) {
        ShippingAddress shippingAddress;
        if (StringUtils.isBlank(vo.getId())) {
            if (!vo.isDef()) {
                List<ShippingAddress> addresses = repository.findByUserId(vo.getUserId());
                if (CollectionUtils.isEmpty(addresses)) {
                    //没有收货地址，该地址为默认收货地址
                    vo.setDef(true);
                }
            }
            vo.setId(IdFactory.createUserId(UserConsts.ID_PREFIX_SHIPPING_ADDRESS));
            shippingAddress = ShippingAddress.fromVo(vo);
        } else {
            Optional<ShippingAddress> addressIdOptional = repository.findDefault(vo.getUserId());
            if (!addressIdOptional.isPresent() || addressIdOptional.get().getId().equals(vo.getId())) {
                vo.setDef(true);
            }

            ShippingAddress dbAddress = repository.findOne(vo.getId());
            if (null == dbAddress) {
                shippingAddress = ShippingAddress.fromVo(vo);
            } else {
                shippingAddress = dbAddress;
                shippingAddress.update(vo);
            }
        }

        return repository.save(shippingAddress);
    }

    /**
     * 根据ID删除地址
     *
     * @param userId 用户ID
     * @param id     地址ID
     */
    public void delete(String userId, String id) {
        Optional<ShippingAddress> addressOptional = repository.findByIdAndUserId(userId, id);
        addressOptional.ifPresent(shippingAddress -> repository.delete(shippingAddress));
    }
}
