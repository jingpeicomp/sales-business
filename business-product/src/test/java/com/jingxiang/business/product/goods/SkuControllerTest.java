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

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }


    @Test
    public void createSku() throws Exception {
        SkuCreateRequest request = new SkuCreateRequest();
        request.setName("iphoneX");
        request.setDescription("Iphone good good good");
        request.setSalePrice(100.31d);
        request.setUnit("个");
        request.setImages(Arrays.asList("http://1.jpg", "http://2.jpg"));
        mvc.perform(MockMvcRequestBuilders.post("/api/business/product/{shopId}/skus", "S001")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CommonUtils.toJson(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("iphoneX"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shopId").value("S001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pic").value("http://1.jpg"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salePrice").value(100.31d))
                .andReturn().getResponse().getErrorMessage();
    }

    @Test
    public void updateSku() throws Exception {
        SkuCreateRequest request = new SkuCreateRequest();
        request.setName("iphone8");
        request.setSalePrice(55.45d);
        request.setDescription("Huawei good good good");
        request.setImages(Arrays.asList("http://2.jpg", "http://3.jpg"));
        mvc.perform(MockMvcRequestBuilders.put("/api/business/product/{shopId}/skus/{skuId}", "S001", "K0001")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CommonUtils.toJson(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("iphone8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shopId").value("S001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pic").value("http://2.jpg"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salePrice").value(55.45d))
                .andExpect(MockMvcResultMatchers.jsonPath("$.unit").value("个"))
                .andReturn().getResponse().getErrorMessage();
    }

    @Test
    public void query() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/business/product/{shopId}/skus", "S001")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("iphoneX"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].shopId").value("S001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].pic").value("http://3.jpg"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].salePrice").value(28.99d))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].unit").value("个"))
                .andReturn().getResponse().getErrorMessage();
    }

    @Test
    public void publish() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/api/business/product/{shopId}/skus/{skuId}/publish", "S001", "K0001")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("iphoneX"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shopId").value("S001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pic").value("http://3.jpg"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salePrice").value(28.99d))
                .andExpect(MockMvcResultMatchers.jsonPath("$.unit").value("个"))
                .andReturn().getResponse().getErrorMessage();
    }
}