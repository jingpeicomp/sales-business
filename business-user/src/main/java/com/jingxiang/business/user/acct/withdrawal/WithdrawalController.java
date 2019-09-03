package com.jingxiang.business.user.acct.withdrawal;

import com.jingxiang.business.exception.ResourceNotFindException;
import com.jingxiang.business.user.acct.common.consts.CompleteStatus;
import com.jingxiang.business.user.acct.common.vo.withdrawal.WithdrawalCreateRequest;
import com.jingxiang.business.user.acct.common.vo.withdrawal.WithdrawalQueryCondition;
import com.jingxiang.business.user.acct.common.vo.withdrawal.WithdrawalVo;
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
 * 提现单API接口
 * Created by liuzhaoming on 2019/9/2.
 */

@Api(value = "Deposit API", description = "充值单REST API接口")
@RestController
@RequestMapping("/api/business/acct/withdrawal")
@Validated
@Slf4j
public class WithdrawalController {

    @Autowired
    private WithdrawalService withdrawalService;

    /**
     * 用户根据查询条件分页查询提现单
     *
     * @param condition 查询条件
     * @return 提现单分页查询结果
     */
    @ApiOperation(value = "用户根据查询条件分页查询提现单", notes = "用户根据查询条件分页查询提现单")
    @RequestMapping(method = RequestMethod.GET)
    public Page<WithdrawalVo> query(WithdrawalQueryCondition condition) {
        condition.setUserId(AuthUtils.getUserId());
        if (null == condition.getCompleteStatus()) {
            return withdrawalService.query(condition.getUserId(), new PageRequest(condition.getPage(), condition.getSize()))
                    .map(Withdrawal::toVo);
        } else {
            return withdrawalService.query(condition.getUserId(), CompleteStatus.fromValue(condition.getCompleteStatus()),
                    new PageRequest(condition.getPage(), condition.getSize()))
                    .map(Withdrawal::toVo);
        }
    }

    /**
     * 用户根据ID查询提现单
     *
     * @param id 提现单ID
     * @return 提现单信息
     */
    @ApiOperation(value = "用户根据ID查询提现单", notes = "用户根据ID查询提现单")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public WithdrawalVo queryById(@PathVariable String id) {
        return withdrawalService.queryById(id, AuthUtils.getUserId())
                .map(Withdrawal::toVo)
                .orElseThrow(() -> new ResourceNotFindException("找不到对应的提现单"));
    }

    /**
     * 系统查询提现单信息，此接口只有管理员能够调用，主要用于Boss
     *
     * @param condition 查询条件
     * @return 提现单分页信息
     */
    @ApiOperation(value = "系统查询提现单信息", notes = "系统查询提现单信息，此接口只有管理员能够调用，主要用于Boss")
    @RequestMapping(path = "/system", method = RequestMethod.GET)
    public Page<WithdrawalVo> systemQuery(WithdrawalQueryCondition condition) {
        if (null == condition.getCompleteStatus()) {
            return withdrawalService.queryAsSystem(new PageRequest(condition.getPage(), condition.getSize()))
                    .map(Withdrawal::toVo);
        } else {
            return withdrawalService.queryAsSystem(CompleteStatus.fromValue(condition.getCompleteStatus()),
                    new PageRequest(condition.getPage(), condition.getSize()))
                    .map(Withdrawal::toVo);
        }
    }

    /**
     * 创建提现单
     *
     * @param request 提现单创建请求
     * @return 提现单详情
     */
    @ApiOperation(value = "创建提现单", notes = "创建提现单")
    @RequestMapping(method = RequestMethod.POST)
    public WithdrawalVo create(@Validated @RequestBody WithdrawalCreateRequest request) {
        request.setUserId(AuthUtils.getUserId());
        return withdrawalService.create(request).toVo();
    }

    /**
     * 系统确认提现单，此接口只有管理员能够调用，主要用于Boss
     *
     * @param id 提现单ID
     * @return 提现单详情
     */
    @ApiOperation(value = "系统确认提现单", notes = "系统确认提现单，此接口只有管理员能够调用，主要用于Boss")
    @RequestMapping(path = "/{id}/confirm/system", method = RequestMethod.PUT)
    public WithdrawalVo systemConfirm(@PathVariable String id) {
        return withdrawalService.confirm(id).toVo();
    }
}
