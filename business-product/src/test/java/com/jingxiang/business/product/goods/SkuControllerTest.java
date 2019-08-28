package com.jingxiang.business.product.goods;

import com.jingxiang.business.product.common.vo.SkuCreateRequest;
import com.jingxiang.business.utils.CommonUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import java.util.Arrays;

/**
 * SKU rest接口单元测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SkuControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private SkuService skuService;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }


    @Test
    public void createSku() throws Exception {
        SkuCreateRequest request = new SkuCreateRequest();
        request.setName("iphoneX");
        request.setDescription("Iphone good good good");
//        request.setSalePrice(100.31d);
//        request.setUnit("个");
        request.setImages(Arrays.asList("http://1.jpg", "http://2.jpg"));
        String message = mvc.perform(MockMvcRequestBuilders.post("/api/business/product/{shopId}/skus", "S001")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CommonUtils.toJson(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("iphoneX"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shopId").value("S001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pic").value("http://1.jpg"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salePrice").value(100.31d)).andReturn().getResponse().getErrorMessage();
        System.out.println(message);
    }

    @Test
    public void updateSku() {
    }

    @Test
    public void query() {
    }

    @Test
    public void publish() {
    }
}