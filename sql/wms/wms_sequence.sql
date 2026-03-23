-- ----------------------------
-- 序号管理表
-- ----------------------------
DROP TABLE IF EXISTS `wms_sequence`;
CREATE TABLE `wms_sequence` (
  `sequence_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '序号 ID',
  `biz_type` varchar(50) NOT NULL COMMENT '业务类型（inbound 入库单 outbound 出库单 check 盘点单 transfer 移库单）',
  `prefix` varchar(20) DEFAULT '' COMMENT '序号前缀',
  `current_seq` bigint(20) NOT NULL DEFAULT '0' COMMENT '当前序号',
  `seq_length` int(11) DEFAULT '6' COMMENT '序号长度（不足补 0）',
  `update_date` date DEFAULT NULL COMMENT '更新日期',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`sequence_id`),
  UNIQUE KEY `uk_biz_type` (`biz_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='序号管理表';

-- ----------------------------
-- 初始化单据序号数据
-- ----------------------------
INSERT INTO wms_sequence (biz_type, prefix, current_seq, seq_length, remark, create_time) VALUES
('inbound', 'IN', 1, 6, '入库单序号', NOW()),
('outbound', 'OUT', 1, 6, '出库单序号', NOW()),
('check', 'CHK', 1, 6, '盘点单序号', NOW()),
('transfer', 'TRF', 1, 6, '移库单序号', NOW()),
('customer_order', 'CO', 1, 6, '客户订单序号', NOW()),
('purchase_order', 'PO', 1, 6, '采购订单序号', NOW());
