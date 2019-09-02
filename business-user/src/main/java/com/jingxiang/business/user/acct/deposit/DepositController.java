package com.jingxiang.business.user.acct.deposit;

/**
 * 充值单Rest接口
 * Created by liuzhaoming on 2019/9/2.
 */

import com.jingxiang.business.exception.ResourceNotFindException;
import com.jingxiang.business.user.acct.common.consts.CompleteStatus;
import com.jingxiang.business.user.acct.common.vo.deposit.DepositCreateRequest;
import com.jingxiang.business.user.acct.common.vo.deposit.DepositQueryCondition;
import com.jingxiang.business.user.acct.common.vo.deposit.DepositVo;
import com.jingxiang.business.utils.AuthUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 充值单API接口
 * Created by liuzhaoming on 2019/8/31.
 */
@Api(value = "Deposit API", description = "充值单REST API接口")
@RestController
@RequestMapping("/api/business/acct/deposit")
@Validated
@Slf4j
public class DepositController {

    @Autowired
    private DepositService depositService;

    /**
     * 根据查询条件查询充值单
     *
     * @param condition 查询条件
     * @return 充值单列表
     */
    @ApiOperation(value = "根据查询条件查询充值单", notes = "根据查询条件查询充值单")
    @RequestMapping(method = RequestMethod.GET)
    public Page<DepositVo> query(DepositQueryCondition condition) {
        condition.setUserId(AuthUtils.getUserId());
        return depositService.query(condition.getUserId(), CompleteStatus.fromValue(condition.getCompleteStatus()),
                new PageRequest(condition.getPage(), condition.getSize()))
                .map(Deposit::toVo);
    }

    /**
     * 根据ID查询充值单
     *
     * @param id 充值单ID
     * @return 充值单详细信息
     */
    @ApiOperation(value = "根据ID查询充值单", notes = "根据ID查询充值单")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public DepositVo queryById(@PathVariable String id) {
        return depositService.queryById(id)
                .map(Deposit::toVo)
                .orElseThrow(() -> new ResourceNotFindException("找不到充值单"));
    }

    /**
     * 创建充值单
     *
     * @param request 充值单创建请求
     * @return 充值单
     */
    @ApiOperation(value = "创建充值单", notes = "创建充值单")
    @RequestMapping(method = RequestMethod.POST)
    public DepositVo create(@Validated @RequestBody DepositCreateRequest request) {
        request.setUserId(AuthUtils.getUserId());
        return depositService.create(request).toVo();
    }

    /**
     * 充值单支付
     *
     * @param id 支付单ID
     * @return 支付单
     */
    @ApiOperation(value = "充值单支付", notes = "充值单支付")
    @RequestMapping(path = "/{id}/pay", method = RequestMethod.PUT)
    public DepositVo pay(@PathVariable String id) {
        return depositService.pay(id, AuthUtils.getUserId()).toVo();
    }
}
