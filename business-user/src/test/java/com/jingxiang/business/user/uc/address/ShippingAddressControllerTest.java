package com.jingxiang.business.user.uc.address;

import com.jingxiang.business.user.uc.common.vo.address.ShippingAddressVo;
import com.jingxiang.business.utils.CommonUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;

/**
 * 收货地址Rest接口单元测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShippingAddressControllerTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void queryDefault() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/business/user/shippingaddress/default")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("UA002"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value("U0001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.receiverProvince").value("上海"))
                .andReturn().getResponse().getErrorMessage();
    }

    @Test
    public void query() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/business/user/shippingaddress")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value("UA002"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].userId").value("U0001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].receiverProvince").value("上海"))
                .andReturn().getResponse().getErrorMessage();
    }

    @Test
    public void create() throws Exception {
        ShippingAddressVo address = ShippingAddressVo.builder()
                .def(false)
                .receiverProvince("北京")
                .receiverAddress("DDD")
                .receiverCity("AA")
                .receiverMobile("1455554444444")
                .receiverName("王")
                .receiverDistrict("BBB")
                .build();
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/business/user/shippingaddress")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CommonUtils.toJson(address)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value("U0001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.receiverProvince").value("北京"))
                .andReturn();
        ShippingAddressVo returnVo = CommonUtils.fromJson(result.getResponse().getContentAsString(), ShippingAddressVo.class);
        mvc.perform(MockMvcRequestBuilders.delete("/api/business/user/shippingaddress/{id}", returnVo.getId())
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getErrorMessage();
    }

    @Test
    public void update() throws Exception {
        ShippingAddressVo address = ShippingAddressVo.builder()
                .def(false)
                .receiverProvince("北京")
                .receiverAddress("DDD")
                .receiverCity("AA")
                .receiverMobile("1455554444444")
                .receiverName("王")
                .receiverDistrict("BBB")
                .build();
        mvc.perform(MockMvcRequestBuilders.put("/api/business/user/shippingaddress/{id}", "UA002")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CommonUtils.toJson(address)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("UA002"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value("U0001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.receiverProvince").value("北京"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.def").value(true))
                .andReturn().getResponse().getErrorMessage();
    }
}