package com.jingxiang.business.user.acct.account;

import com.jingxiang.business.consts.PayType;
import com.jingxiang.business.user.acct.account.bill.AccountBill;
import com.jingxiang.business.user.acct.common.consts.*;
import com.jingxiang.business.user.acct.common.vo.payment.PaymentVo;
import com.jingxiang.business.utils.CommonUtils;
import com.jingxiang.business.utils.MathUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

/**
 * 账户服务单元测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void orderPaid() {
        BigDecimal orderAmount = BigDecimal.valueOf(100.59d);
        buyerOrderPaidTest(orderAmount);

        BigDecimal sfFee = sellerOrderPaidTest(orderAmount);

        partnerOrderPaidTest(sfFee);
    }

    private void partnerOrderPaidTest(BigDecimal sfFee) {
        List<AccountBill> partnerBills = accountService.queryBill("UP001", AccountType.PARTNER)
                .stream()
                .filter(bill -> bill.getRequestId().equals("T001"))
                .collect(Collectors.toList());
        assertThat(partnerBills, hasSize(1));
        assertThat(partnerBills.get(0).getPaymentId(), is("P001"));
        assertThat(partnerBills.get(0).getFundDirection(), is(FundDirection.DEBIT));
        assertThat(partnerBills.get(0).getAmount(), is(sfFee));
        assertThat(partnerBills.get(0).getTarget(), is(AccountOperationTarget.SERVICE_FEE));
        assertThat(partnerBills.get(0).getOperation(), is(AccountOperation.PAY));
        assertThat(partnerBills.get(0).getAccountType(), is(AccountType.PARTNER));
    }

    private void buyerOrderPaidTest(BigDecimal orderAmount) {
        PaymentVo payment = PaymentVo.builder()
                .id("P001")
                .payAmount(orderAmount)
                .paidAmount(orderAmount)
                .payee("U001")
                .payer("U100")
                .payTime(CommonUtils.parseLocalDateTime("2019-01-01 00:00:00"))
                .payType(PayType.WEIXIN)
                .platformPayId("platformId1")
                .prePlatformPayId("prePlatformPayId1")
                .shopId("S001")
                .source(PaymentSource.ORDER_PAY)
                .sourceId("T001")
                .status(PaymentStatus.PAID)
                .title("U100订单金额100.59")
                .build();
        accountService.orderPaid(payment);
        List<AccountBill> buyerBills = accountService.queryBill("U100", AccountType.USER)
                .stream()
                .filter(bill -> bill.getRequestId().equals("T001"))
                .collect(Collectors.toList());
        assertThat(buyerBills, hasSize(1));
        assertThat(buyerBills.get(0).getPaymentId(), is("P001"));
        assertThat(buyerBills.get(0).getFundDirection(), is(FundDirection.CREDIT));
        assertThat(buyerBills.get(0).getAmount(), is(orderAmount));
        assertThat(buyerBills.get(0).getTarget(), is(AccountOperationTarget.BALANCE));
        assertThat(buyerBills.get(0).getOperation(), is(AccountOperation.PAY));
        assertThat(buyerBills.get(0).getAccountType(), is(AccountType.USER));
    }

    private BigDecimal sellerOrderPaidTest(BigDecimal orderAmount) {
        List<AccountBill> sellerBills = accountService.queryBill("U001", AccountType.SELLER)
                .stream()
                .filter(bill -> bill.getRequestId().equals("T001"))
                .collect(Collectors.toList());
        assertThat(sellerBills, hasSize(2));

        AccountBill balanceBill = sellerBills.stream()
                .filter(bill -> bill.getTarget().equals(AccountOperationTarget.BALANCE))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
        assertThat(balanceBill.getPaymentId(), is("P001"));
        assertThat(balanceBill.getFundDirection(), is(FundDirection.DEBIT));
        BigDecimal bankFee = MathUtils.multiply(orderAmount, AcctConsts.WXPAY_FEE_RATE);
        assertThat(balanceBill.getAmount(), is(orderAmount.subtract(bankFee)));
        assertThat(balanceBill.getTarget(), is(AccountOperationTarget.BALANCE));
        assertThat(balanceBill.getOperation(), is(AccountOperation.PAY));
        assertThat(balanceBill.getAccountType(), is(AccountType.SELLER));

        AccountBill sfBalanceBill = sellerBills.stream()
                .filter(bill -> bill.getTarget().equals(AccountOperationTarget.SERVICE_FEE))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
        assertThat(sfBalanceBill.getPaymentId(), is("P001"));
        assertThat(sfBalanceBill.getFundDirection(), is(FundDirection.CREDIT));
        BigDecimal sellIn = orderAmount.subtract(bankFee);
        BigDecimal sfFee = MathUtils.multiply(sellIn, AcctConsts.SERVICE_FEE_RATE);
        assertThat(sfBalanceBill.getAmount(), is(sfFee));
        assertThat(sfBalanceBill.getTarget(), is(AccountOperationTarget.SERVICE_FEE));
        assertThat(sfBalanceBill.getOperation(), is(AccountOperation.PAY));
        assertThat(sfBalanceBill.getAccountType(), is(AccountType.SELLER));
        return sfFee;
    }

    @Test
    public void depositPaid() {
    }

    @Test
    public void withdrawalCreated() {
    }
}