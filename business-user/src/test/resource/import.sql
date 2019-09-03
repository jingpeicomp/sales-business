-- 收货地址表
insert into `t_biz_uc_shipping_address` ( `id`, `USER_ID`, `def`, `receiver_Name`, `receiver_Mobile`, `receiver_Province`, `receiver_City`, `receiver_District`, `receiver_Street`, `receiver_Address`, `CREATE_TIME`, `update_Time`, `version`) values ( 'UA001', 'U0001', false, '张三', '12345678', '江苏', '南京', '雨花台区', '宁南街道', '软件大道新华汇', '2019-08-28 16:55:40', '2019-08-29 16:55:40', 0);
insert into `t_biz_uc_shipping_address` ( `id`, `USER_ID`, `def`, `receiver_Name`, `receiver_Mobile`, `receiver_Province`, `receiver_City`, `receiver_District`, `receiver_Street`, `receiver_Address`, `CREATE_TIME`, `update_Time`, `version`) values ( 'UA002', 'U0001', true, '李四', '000000000', '上海', '上海', '雨花台区', '宁南街道', '软件大道新华汇', '2019-08-29 16:55:40', '2019-08-29 17:55:40', 0);
insert into `t_biz_uc_shipping_address` ( `id`, `USER_ID`, `def`, `receiver_Name`, `receiver_Mobile`, `receiver_Province`, `receiver_City`, `receiver_District`, `receiver_Street`, `receiver_Address`, `CREATE_TIME`, `update_Time`, `version`) values ( 'UA003', 'U002', true, '王五', '999999999', '江苏', '扬州', '雨花台区', '宁南街道', '软件大道新华汇', '2018-08-29 16:55:40', '2018-08-29 17:55:40', 0);

-- 店铺
insert into `T_BIZ_UC_SHOP` ( `id`, `owner`, `group_Id`, `name`, `partner`,  `CREATE_TIME`, `update_Time`, `version`) values ( 'S001', 'U0001', 'group1', '张三小店', 'UP001',  '2019-08-28 16:55:40', '2019-08-29 16:55:40', 0);
insert into `T_BIZ_UC_SHOP` ( `id`, `owner`, `group_Id`, `name`, `partner`,  `CREATE_TIME`, `update_Time`, `version`) values ( 'S002', 'U0001', 'group2', '张三水果店', 'UP001',  '2019-08-28 16:55:40', '2019-08-29 16:55:40', 0);

-- 账户表
insert into `T_BIZ_UC_ACCOUNT` ( `id`, `user_Id`, `type`, `balance`, `total_Income`,  `total_Expend`, `sf_Balance`, `total_Sf_Income` , `total_Sf_Expend`,  `total_Bf_Expend`, `bf_Rate`, `sf_Rate`,  `CREATE_TIME`, `update_Time`, `version`) values ( 'A001', 'U001', 1, 0, 0, 0, 0, 0, 0, 0, 0.006, 0.01, '2019-08-28 16:55:40', '2019-08-29 16:55:40', 0);
insert into `T_BIZ_UC_ACCOUNT` ( `id`, `user_Id`, `type`, `balance`, `total_Income`,  `total_Expend`, `sf_Balance`, `total_Sf_Income` , `total_Sf_Expend`,  `total_Bf_Expend`, `bf_Rate`, `sf_Rate`,  `CREATE_TIME`, `update_Time`, `version`) values ( 'A002', 'U001', 2, 0, 0, 0, 0, 0, 0, 0, 0.006, 0.01, '2019-08-28 16:55:40', '2019-08-29 16:55:40', 0);
insert into `T_BIZ_UC_ACCOUNT` ( `id`, `user_Id`, `type`, `balance`, `total_Income`,  `total_Expend`, `sf_Balance`, `total_Sf_Income` , `total_Sf_Expend`,  `total_Bf_Expend`, `bf_Rate`, `sf_Rate`,  `CREATE_TIME`, `update_Time`, `version`) values ( 'A003', 'U001', 3, 0, 0, 0, 1000, 0, 0, 0, 0.006, 0.01, '2019-08-28 16:55:40', '2019-08-29 16:55:40', 0);
insert into `T_BIZ_UC_ACCOUNT` ( `id`, `user_Id`, `type`, `balance`, `total_Income`,  `total_Expend`, `sf_Balance`, `total_Sf_Income` , `total_Sf_Expend`,  `total_Bf_Expend`, `bf_Rate`, `sf_Rate`,  `CREATE_TIME`, `update_Time`, `version`) values ( 'A1001', 'U1001', 2, 500, 0, 0, 0, 0, 0, 0, 0.006, 0.01, '2019-08-28 16:55:40', '2019-08-29 16:55:40', 0);

-- 充值表
insert into `T_BIZ_UC_DEPOSIT` ( `id`, `user_Id`, `type`, `amount`, `bank_Fee`,  `pay_Amount`, `paid_Amount`, `pay_Status` ,  `complete_Status`, `CREATE_TIME`, `update_Time`, `finish_Time`, `auto_Close_Time`, `pay_Type`, `pay_Id`, `pay_Time`, `platform_Pay_Id`, `pre_Platform_Pay_Id`,  `version`) values ( 'D001', 'U001', 1, 100.99, 0, 100.99, 0, 1, 2, '2019-08-28 16:55:40', '2019-08-28 16:55:40',  null, '2019-08-28 20:55:40',  1, null, null, null, null, 0);

-- 提现表
insert into `T_BIZ_UC_WITHDRAWAL` ( `id`, `user_Id`, `type`, `amount`, `bank_Fee`,  `pay_Amount`, `paid_Amount`, `pay_Status` ,  `complete_Status`, `CREATE_TIME`, `update_Time`, `finish_Time`,  `pay_Type`, `pay_Id`, `pay_Time`, `platform_Pay_Id`, `pre_Platform_Pay_Id`,  `version`) values ( 'UW001', 'U001', 1, 100.99, 0, 100.99, 0, 1, 2, '2019-08-28 16:55:40', '2019-08-28 16:55:40',  null,  1, null, null, null, null, 0);
insert into `T_BIZ_UC_WITHDRAWAL` ( `id`, `user_Id`, `type`, `amount`, `bank_Fee`,  `pay_Amount`, `paid_Amount`, `pay_Status` ,  `complete_Status`, `CREATE_TIME`, `update_Time`, `finish_Time`,  `pay_Type`, `pay_Id`, `pay_Time`, `platform_Pay_Id`, `pre_Platform_Pay_Id`,  `version`) values ( 'UW002', 'U002', 1, 200.13, 0, 200.13, 0, 4, 3, '2019-08-29 16:55:40', '2019-08-29 16:55:40',  '2019-08-29 19:55:40',  1, null, '2019-08-29 19:55:40', null, null, 0);

-- 支付单表
insert into `T_BIZ_UC_PAYMENT` ( `id`, `shop_Id`, `source_Id`, `payer`, `payee`,  `pay_Type`, `pay_Amount`, `paid_Amount` ,  `source`, `CREATE_TIME`, `update_Time`, `pay_Time`,  `platform_Pay_Id`, `pre_Platform_Pay_Id`, `status`, `title`, `version`) values ( 'P1001', 'S002', 'T10001', 'U001','U002',1, 100.99, 0, 1,  '2019-08-28 16:55:40', '2019-08-28 16:55:40',  null,  null, null, 1, '测试订单T10001', 0);
