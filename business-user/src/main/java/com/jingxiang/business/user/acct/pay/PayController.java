package com.jingxiang.business.user.acct.pay;

import com.jingxiang.business.exception.ResourceNotFindException;
import com.jingxiang.business.user.acct.common.vo.payment.PaymentCreateRequest;
import com.jingxiang.business.user.acct.common.vo.payment.PaymentOperateRequest;
import com.jingxiang.business.user.acct.common.vo.payment.PaymentVo;
import com.jingxiang.business.utils.AuthUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 支付API接口
 * Created by liuzhaoming on 2019/8/31.
 */
@Api(value = "Pay API", description = "支付REST API接口")
@RestController
@RequestMapping("/api/business/acct")
@Validated
@Slf4j
public class PayController {
    @Autowired
    private PayService payService;

    /**
     * 创建支付单
     *
     * @param shopId  店铺ID
     * @param request 支付单创建请求
     * @return 支付单详情
     */
    @ApiOperation(value = "创建支付单", notes = "创建支付单")
    @RequestMapping(path = "/shops/{shopId}/payments", method = RequestMethod.POST)
    public PaymentVo create(@PathVariable String shopId, @Valid @RequestBody PaymentCreateRequest request) {
        request.setShopId(shopId);
        request.setPayer(AuthUtils.getUserId());
        return payService.create(request);
    }

    /**
     * 支付单支付
     *
     * @param shopId 店铺ID
     * @param id     支付单ID
     * @return 支付结果
     */
    @ApiOperation(value = "支付单支付", notes = "支付单支付")
    @RequestMapping(path = "/shops/{shopId}/payments/{id}/pay", method = RequestMethod.PUT)
    public PaymentVo pay(@PathVariable String shopId, @PathVariable String id) {
        PaymentOperateRequest request = PaymentOperateRequest.builder().paymentId(id).shopId(shopId).build();
        return payService.pay(request);
    }

    /**
     * 根据ID查询支付单
     *
     * @param id 支付单ID
     * @return 支付单详细信息
     */
    @ApiOperation(value = "根据ID查询支付单", notes = "根据ID查询支付单")
    @RequestMapping(path = "/payments/{id}", method = RequestMethod.GET)
    public PaymentVo queryById(@PathVariable String id) {
        return payService.queryById(id)
                .orElseThrow(() -> new ResourceNotFindException("找不到对应的支付单"));
    }
}
