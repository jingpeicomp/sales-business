# 简单电商系统
一个简单的电商系统，包含商品、订单、会员及支付三个业务模块，基于领域驱动设计（DDD）。
 * business-common 公共模块
 * business-product 商品
 * business-order 订单， 内部用一个自定义的状态机（FSM）实现订单的状态迁移
 * business-user 账务和会员，一个会员对应多个账户
 * business-app 应用启动入口