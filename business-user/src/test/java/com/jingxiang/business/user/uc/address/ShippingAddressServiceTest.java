package com.jingxiang.business.user.uc.address;

import com.jingxiang.business.user.uc.common.vo.address.ShippingAddressVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * 收货地址业务单元测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShippingAddressServiceTest {

    @Autowired
    private ShippingAddressService service;

    @Autowired
    private ShippingAddressRepository repository;

    @Test
    public void queryDefault() {
        ShippingAddress address = service.queryDefault("U0001").orElseThrow(IllegalStateException::new);
        assertThat(address.getId(), is("UA002"));
    }

    @Test
    public void queryDefaultNull() {
        ShippingAddress address = service.queryDefault("U005").orElse(null);
        assertThat(address, nullValue());
    }

    @Test
    public void query() {
        List<ShippingAddress> addressList = service.query("U0001");
        assertThat(addressList, hasSize(2));
        assertThat(addressList.stream().map(ShippingAddress::getId).collect(Collectors.toList()), hasItems("UA001", "UA002"));
    }

    @Test
    @Transactional
    public void saveInsert() {
        ShippingAddressVo address = new ShippingAddressVo();
        address.setUserId("U005");
        address.setDef(false);
        address.setReceiverName("Lily");
        address.setReceiverAddress("a");
        address.setReceiverCity("B");
        address.setReceiverDistrict("C");
        address.setReceiverMobile("22222");
        address.setReceiverProvince("DD");
        address.setReceiverStreet("DD222");
        service.save(address);
        List<ShippingAddress> addressList = repository.findByUserId("U005");
        assertThat(addressList, hasSize(1));
        assertThat(addressList.get(0).getReceiverName(), is("Lily"));
        assertThat(addressList.get(0).isDef(), is(true));
        assertThat(addressList.get(0).getReceiverStreet(), is("DD222"));
    }

    @Test
    public void saveUpdate() {
        ShippingAddressVo address = new ShippingAddressVo();
        address.setUserId("U0001");
        address.setId("UA001");
        address.setDef(false);
        address.setReceiverName("Lily");
        address.setReceiverAddress("A");
        address.setReceiverCity("B");
        address.setReceiverDistrict("C");
        address.setReceiverMobile("2222");
        address.setReceiverProvince("DD");
        address.setReceiverStreet("DD222");
        service.save(address);
        ShippingAddress dbAddress = repository.findOne("UA001");
        assertThat(dbAddress.getReceiverName(), is("Lily"));
        assertThat(dbAddress.isDef(), is(false));
        assertThat(dbAddress.getReceiverStreet(), is("DD222"));
    }

    @Test
    public void saveUpdateDefault() {
        ShippingAddressVo address = new ShippingAddressVo();
        address.setUserId("U0001");
        address.setId("UA002");
        address.setDef(false);
        address.setReceiverName("Lily");
        address.setReceiverAddress("a");
        address.setReceiverCity("D");
        address.setReceiverDistrict("CB");
        address.setReceiverMobile("22222");
        address.setReceiverProvince("DD");
        address.setReceiverStreet("DD222");
        service.save(address);
        ShippingAddress dbAddress = repository.findOne("UA002");
        assertThat(dbAddress.getReceiverName(), is("Lily"));
        assertThat(dbAddress.isDef(), is(true));
        assertThat(dbAddress.getReceiverStreet(), is("DD222"));
    }
}