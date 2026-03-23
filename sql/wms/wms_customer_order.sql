-- ----------------------------
-- 客户订单表结构
-- ----------------------------

-- ----------------------------
-- 1. 客户订单主表
-- ----------------------------
DROP TABLE IF EXISTS `wms_customer_order`;
CREATE TABLE `wms_customer_order` (
  `order_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单 ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单编号',
  `customer_code` varchar(50) NOT NULL COMMENT '客户编码',
  `customer_name` varchar(200) DEFAULT NULL COMMENT '客户名称',
  `order_date` date DEFAULT NULL COMMENT '订单日期',
  `required_date` date DEFAULT NULL COMMENT '要求到货日期',
  `total_qty` decimal(10,2) DEFAULT '0' COMMENT '订单数量合计',
  `total_amount` decimal(14,2) DEFAULT '0' COMMENT '金额合计',
  `order_status` char(1) DEFAULT '0' COMMENT '订单状态（0 草稿 1 完成）',
  `ship_status` char(1) DEFAULT '0' COMMENT '发货状态（0 未发货 1 部分发货 2 已发货）',
  `remark` varchar(500) DEFAULT NULL COMMENT '说明',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_customer_code` (`customer_code`),
  KEY `idx_order_date` (`order_date`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='客户订单表';

-- ----------------------------
-- 2. 客户订单明细表
-- ----------------------------
DROP TABLE IF EXISTS `wms_customer_order_item`;
CREATE TABLE `wms_customer_order_item` (
  `item_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '明细 ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单 ID',
  `product_code` varchar(50) NOT NULL COMMENT '物料编码',
  `product_name` varchar(200) DEFAULT NULL COMMENT '物料名称',
  `unit_price` decimal(14,2) DEFAULT '0' COMMENT '不含税单价',
  `quantity` decimal(10,2) DEFAULT '0' COMMENT '数量',
  `amount` decimal(14,2) DEFAULT '0' COMMENT '不含税金额',
  `tax_rate` decimal(5,2) DEFAULT '0' COMMENT '税率（%）',
  `unit_price_tax` decimal(14,2) DEFAULT '0' COMMENT '含税单价',
  `amount_tax` decimal(14,2) DEFAULT '0' COMMENT '含税金额',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`item_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_product_code` (`product_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='客户订单明细表';
