-- ----------------------------
-- 客户表
-- ----------------------------
DROP TABLE IF EXISTS `wms_customer`;
CREATE TABLE `wms_customer` (
  `customer_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '客户 ID',
  `customer_code` varchar(50) NOT NULL COMMENT '客户编码',
  `customer_name` varchar(200) NOT NULL COMMENT '客户名称',
  `contact` varchar(50) DEFAULT NULL COMMENT '联系人',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `address` varchar(500) DEFAULT NULL COMMENT '地址',
  `status` char(1) DEFAULT '0' COMMENT '状态（0 正常 1 停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`customer_id`),
  UNIQUE KEY `uk_customer_code` (`customer_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='客户表';
