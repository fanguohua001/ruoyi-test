-- ----------------------------
-- 供应商表
-- ----------------------------
DROP TABLE IF EXISTS `wms_supplier`;
CREATE TABLE `wms_supplier` (
  `supplier_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '供应商 ID',
  `supplier_code` varchar(50) NOT NULL COMMENT '供应商编码',
  `supplier_name` varchar(200) NOT NULL COMMENT '供应商名称',
  `contact` varchar(50) DEFAULT NULL COMMENT '联系人',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `address` varchar(500) DEFAULT NULL COMMENT '地址',
  `status` char(1) DEFAULT '0' COMMENT '状态（0 正常 1 停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`supplier_id`),
  UNIQUE KEY `uk_supplier_code` (`supplier_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='供应商表';

-- ----------------------------
-- 供应商测试数据
-- ----------------------------
INSERT INTO `wms_supplier` VALUES
(1, 'SUP001', '苏州电子科技有限公司', '张三', '13800138000', '江苏省苏州市工业园区', '0', 'admin', NOW(), '', NULL, '电子元器件供应商'),
(2, 'SUP002', '上海贸易有限公司', '李四', '13900139000', '上海市浦东新区', '0', 'admin', NOW(), '', NULL, '原材料供应商'),
(3, 'SUP003', '深圳实业有限公司', '王五', '13700137000', '广东省深圳市', '0', 'admin', NOW(), '', NULL, '包装材料供应商');

