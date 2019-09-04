-- 订单表
insert into `t_biz_tc_order` ( `update_time`, `id`, `receiver_street`, `pay_id`, `ship_time`, `version`, `pay_status`, `total_item_price`, `shop_id`, `auto_close_time`, `total_pay_price`, `finish_time`, `create_time`, `receiver_province`, `buyer`, `ship_status`, `receiver_city`, `trade_no`, `receiver_address`, `pay_time`, `complete_status`, `confirm_time`, `order_type`, `total_paid_price`, `receiver_district`, `auto_confirm_seconds`, `total_price`, `receiver_name`, `auto_confirm_time`, `pay_type`, `receiver_mobile`, `ship_price`, `pre_pay_id`) values ( null, 'T0001', '宁南街道', 'P00001', null, '0', '1', '111.44', 'S001', '2019-09-05 09:18:41', '111.44', null, '2019-09-04 09:19:56', '江苏', 'U001', '1', '南京', null, 'jjjjjj', null, '2', null, '1', '0.00', '雨花台区', '30000', '111.44', '张三', null, '1', '1234567890', '0.00', null);

insert into `t_biz_tc_order_product` ( `id`, `sku_name`, `total_buy_price`, `buyer`, `sku_num`, `sku_id`, `order_id`, `buy_price`, `pic`, `sku_sale_price`, `sku_unit`, `shop_id`) values ( 'TP0001', '手机', '100.00', 'U001', '1.00', 'S1009', 'T0001', '100.00', null, '100.00', '个', 'S001');
insert into `t_biz_tc_order_product` ( `id`, `sku_name`, `total_buy_price`, `buyer`, `sku_num`, `sku_id`, `order_id`, `buy_price`, `pic`, `sku_sale_price`, `sku_unit`, `shop_id`) values ( 'TP0002', '电脑', '11.44', 'U001', '1.00', 'S1004', 'T0001', '11.44', null, '11.44', '个', 'S001');

-- SKU表
insert into `t_biz_pc_sku` ( `id`, `description`, `unit`, `update_time`, `version`, `pic`, `stock`, `modifier`, `sale_price`, `publisher`, `images`, `adder`, `publish_time`, `create_time`, `name`, `shop_id`) values ( 'K0001', 'dafafdasfdasfsaf', '个', '2019-08-28 16:57:48', '0', 'http://3.jpg', '100', '', '28.99', '', 'http://1.jpg;http://2.jpg', '', null, '2019-08-28 16:55:40', 'iphoneX', 'S001');
insert into `t_biz_pc_sku` ( `id`, `description`, `unit`, `update_time`, `version`, `pic`, `stock`, `modifier`, `sale_price`, `publisher`, `images`, `adder`, `publish_time`, `create_time`, `name`, `shop_id`) values ( 'K0002', '测试商品2', '个', '2019-08-28 16:57:48', '0', 'http://3.jpg', '100', '', '35.10', '', 'http://1.jpg;http://2.jpg', '', null, '2019-08-28 16:55:40', '华为mateX', 'S001');

-- 店铺
insert into `T_BIZ_UC_SHOP` ( `id`, `owner`, `group_Id`, `name`, `partner`,  `CREATE_TIME`, `update_Time`, `version`) values ( 'S001', 'U0001', 'group1', '张三小店', 'UP001',  '2019-08-28 16:55:40', '2019-08-29 16:55:40', 0);

--支付单
insert into `T_BIZ_UC_PAYMENT` ( `id`, `shop_Id`, `source_Id`, `payer`, `payee`,  `pay_Type`, `pay_Amount`, `paid_Amount` ,  `source`, `CREATE_TIME`, `update_Time`, `pay_Time`,  `platform_Pay_Id`, `pre_Platform_Pay_Id`, `status`, `title`, `version`) values ( 'P00001', 'S001', 'T0001', 'U001','U002',1, 111.44, 0, 1,  '2019-08-28 16:55:40', '2019-08-28 16:55:40',  null,  null, null, 1, '你懂的T0001', 0);
