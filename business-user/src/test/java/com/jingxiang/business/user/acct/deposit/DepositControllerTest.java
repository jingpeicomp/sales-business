package com.jingxiang.business.user.acct.deposit;

import com.jingxiang.business.user.acct.common.consts.DepositType;
import com.jingxiang.business.user.acct.common.consts.PayStatus;
import com.jingxiang.business.user.acct.common.vo.deposit.DepositCreateRequest;
import com.jingxiang.business.user.acct.common.vo.deposit.DepositVo;
import com.jingxiang.business.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
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

import java.math.BigDecimal;

/**
 * 充值单接口测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DepositControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private DepositRepository depositRepository;

    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void query() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/business/acct/deposit")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(0L))
                .andReturn().getResponse().getErrorMessage();

        mvc.perform(MockMvcRequestBuilders.get("/api/business/acct/deposit?completeStatus={status}", 2)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value("D001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].amount").value(BigDecimal.valueOf(100.99D)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].payStatus").value(PayStatus.UNPAID.getValue()))
                .andReturn().getResponse().getErrorMessage();
    }

    @Test
    public void queryById() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/business/acct/deposit/{id}", "D001")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("D001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(BigDecimal.valueOf(100.99D)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payStatus").value(PayStatus.UNPAID.getValue()))
                .andReturn().getResponse().getErrorMessage();
    }

    @Test
    public void create() throws Exception {
        DepositCreateRequest request = DepositCreateRequest.builder()
                .amount(BigDecimal.valueOf(155.76D))
                .depositType(DepositType.SERVICE_FEE.getValue())
                .build();
        String jsonStr = mvc.perform(MockMvcRequestBuilders.post("/api/business/acct/deposit")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CommonUtils.toJson(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(BigDecimal.valueOf(155.76D)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payStatus").value(PayStatus.UNPAID.getValue()))
                .andReturn().getResponse().getContentAsString();
        DepositVo deposit = CommonUtils.fromJson(jsonStr, DepositVo.class);
        if (null != deposit && StringUtils.isNotBlank(deposit.getId())) {
            depositRepository.delete(deposit.getId());
        }
    }

    //    @Test
    public void pay() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/business/acct/deposit/{id}/pay", "D001")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(BigDecimal.valueOf(100.99D)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payStatus").value(PayStatus.PAID.getValue()))
                .andReturn().getResponse().getErrorMessage();
    }
}