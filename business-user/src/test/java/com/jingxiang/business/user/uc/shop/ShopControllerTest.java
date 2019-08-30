package com.jingxiang.business.user.uc.shop;

import com.jingxiang.business.user.uc.common.vo.shop.ShopCreateRequest;
import com.jingxiang.business.user.uc.common.vo.shop.ShopUpdateRequest;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * 店铺Rest接口单元测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShopControllerTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void queryByGroupId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/business/user/shops/groups/{groupId}", "group2")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("S002"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.owner").value("U001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("张三水果店"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.partner").value("UP001"))
                .andReturn().getResponse().getErrorMessage();
    }

    @Test
    public void queryById() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/business/user/shops/{shopId}", "S001")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("S001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.owner").value("U001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("张三小店"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.partner").value("UP001"))
                .andReturn().getResponse().getErrorMessage();
    }

    @Test
    public void create() throws Exception {
        ShopCreateRequest request = ShopCreateRequest.builder()
                .groupId("group3")
                .name("新建小店")
                .owner("U9999")
                .partner("U66666")
                .build();
        mvc.perform(MockMvcRequestBuilders.post("/api/business/user/shops")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CommonUtils.toJson(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.owner").value("U9999"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("新建小店"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.partner").value("U66666"))
                .andReturn().getResponse().getErrorMessage();
    }

    @Test
    public void update() throws Exception {
        ShopUpdateRequest request = ShopUpdateRequest.builder()
                .name("新建小店")
                .partner("U66666")
                .build();
        mvc.perform(MockMvcRequestBuilders.put("/api/business/user/shops/{shopId}", "S001")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CommonUtils.toJson(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("S001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("新建小店"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.partner").value("U66666"))
                .andReturn().getResponse().getErrorMessage();
    }
}