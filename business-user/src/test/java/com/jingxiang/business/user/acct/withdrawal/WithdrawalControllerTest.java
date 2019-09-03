package com.jingxiang.business.user.acct.withdrawal;

import com.jingxiang.business.user.acct.common.consts.CompleteStatus;
import com.jingxiang.business.user.acct.common.consts.PayStatus;
import com.jingxiang.business.user.acct.common.consts.WithdrawalType;
import com.jingxiang.business.user.acct.common.vo.withdrawal.WithdrawalCreateRequest;
import com.jingxiang.business.user.acct.common.vo.withdrawal.WithdrawalVo;
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
 * 提现接口单元测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WithdrawalControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void query() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/business/acct/withdrawal?completeStatus={status}", 3)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(0L))
                .andReturn().getResponse().getErrorMessage();

        mvc.perform(MockMvcRequestBuilders.get("/api/business/acct/withdrawal")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value("UW001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].amount").value(BigDecimal.valueOf(100.99D)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].payStatus").value(PayStatus.UNPAID.getValue()))
                .andReturn().getResponse().getErrorMessage();
    }

    @Test
    public void queryById() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/business/acct/withdrawal/{id}", "UW001")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("UW001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(BigDecimal.valueOf(100.99D)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payStatus").value(PayStatus.UNPAID.getValue()))
                .andReturn().getResponse().getErrorMessage();
    }

    @Test
    public void systemQuery() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/business/acct/withdrawal/system")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value("UW002"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].amount").value(BigDecimal.valueOf(200.13D)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].payStatus").value(PayStatus.PAID.getValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].completeStatus").value(CompleteStatus.DONE.getValue()))
                .andReturn().getResponse().getErrorMessage();
    }

    @Test
    public void create() throws Exception {
        WithdrawalCreateRequest request = WithdrawalCreateRequest.builder()
                .amount(BigDecimal.valueOf(155.76D))
                .withdrawalType(WithdrawalType.SERVICE_FEE.getValue())
                .build();
        String jsonStr = mvc.perform(MockMvcRequestBuilders.post("/api/business/acct/withdrawal")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CommonUtils.toJson(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(BigDecimal.valueOf(155.76D)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payStatus").value(PayStatus.UNPAID.getValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value(WithdrawalType.SERVICE_FEE.getValue()))
                .andReturn().getResponse().getContentAsString();
        WithdrawalVo withdrawal = CommonUtils.fromJson(jsonStr, WithdrawalVo.class);
        if (null != withdrawal && StringUtils.isNotBlank(withdrawal.getId())) {
            withdrawalRepository.delete(withdrawal.getId());
        }
    }

    @Test
    public void systemConfirm() throws Exception {
        WithdrawalCreateRequest request = WithdrawalCreateRequest.builder()
                .amount(BigDecimal.valueOf(155.76D))
                .withdrawalType(WithdrawalType.SERVICE_FEE.getValue())
                .build();
        String jsonStr = mvc.perform(MockMvcRequestBuilders.post("/api/business/acct/withdrawal")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CommonUtils.toJson(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(BigDecimal.valueOf(155.76D)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payStatus").value(PayStatus.UNPAID.getValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value(WithdrawalType.SERVICE_FEE.getValue()))
                .andReturn().getResponse().getContentAsString();
        WithdrawalVo withdrawal = CommonUtils.fromJson(jsonStr, WithdrawalVo.class);

        mvc.perform(MockMvcRequestBuilders.put("/api/business/acct/withdrawal/{id}/confirm/system", withdrawal.getId())
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(BigDecimal.valueOf(155.76D)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payStatus").value(PayStatus.PAID.getValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.completeStatus").value(CompleteStatus.DONE.getValue()))
                .andReturn().getResponse().getErrorMessage();

        if (StringUtils.isNotBlank(withdrawal.getId())) {
            withdrawalRepository.delete(withdrawal.getId());
        }
    }
}