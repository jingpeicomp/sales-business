package com.jingxiang.business.user.acct.pay;

import com.jingxiang.business.consts.PayType;
import com.jingxiang.business.user.acct.common.consts.PayStatus;
import com.jingxiang.business.user.acct.common.consts.PaymentSource;
import com.jingxiang.business.user.acct.common.vo.payment.PaymentCreateRequest;
import com.jingxiang.business.user.acct.common.vo.payment.PaymentVo;
import com.jingxiang.business.utils.AuthUtils;
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
 * 支付REST接口单元测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PayControllerTest {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private PaymentRepository paymentRepository;

    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void create() throws Exception {
        BigDecimal amount = BigDecimal.valueOf(199.43D);
        PaymentCreateRequest request = PaymentCreateRequest.builder()
                .payAmount(amount)
                .payer(AuthUtils.getUserId())
                .payee("U1000")
                .payType(PayType.WEIXIN)
                .shopId("S001")
                .source(PaymentSource.ORDER_PAY)
                .sourceId("T0001")
                .title("订单支付T0001")
                .description("")
                .build();
        String jsonStr = mvc.perform(MockMvcRequestBuilders.post("/api/business/acct/shops/{shopId}/payments", "S001")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CommonUtils.toJson(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payAmount").value(amount))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(PayStatus.UNPAID.getValue()))
                .andReturn().getResponse().getContentAsString();
        PaymentVo payment = CommonUtils.fromJson(jsonStr, PaymentVo.class);
        if (null != payment && StringUtils.isNotBlank(payment.getId())) {
            paymentRepository.delete(payment.getId());
        }
    }

    @Test
    public void pay() throws Exception {
        BigDecimal amount = BigDecimal.valueOf(100.99D);
        mvc.perform(MockMvcRequestBuilders.put("/api/business/acct/shops/{shopId}/payments/{id}/pay", "S002", "P1001")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payAmount").value(amount))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(PayStatus.PAID.getValue()))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void queryById() throws Exception {
        BigDecimal amount = BigDecimal.valueOf(100.99D);
        mvc.perform(MockMvcRequestBuilders.get("/api/business/acct/payments/{id}", "P1001")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payAmount").value(amount))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(PayStatus.UNPAID.getValue()))
                .andReturn().getResponse().getContentAsString();
    }
}