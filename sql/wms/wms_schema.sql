-- ----------------------------
-- WMS 仓储管理模块数据库脚本
-- ----------------------------

-- ----------------------------
-- 1. 物料表
-- ----------------------------
DROP TABLE IF EXISTS `wms_product`;
CREATE TABLE `wms_product` (
  `product_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '物料 ID',
  `product_code` varchar(50) NOT NULL COMMENT '物料编码',
  `product_name` varchar(200) NOT NULL COMMENT '物料名称',
  `category` varchar(50) DEFAULT NULL COMMENT '分类',
  `specification` varchar(100) DEFAULT NULL COMMENT '规格型号',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `safety_stock` decimal(10,2) DEFAULT NULL COMMENT '安全库存',
  `max_stock` decimal(10,2) DEFAULT NULL COMMENT '最高库存',
  `shelf_life_days` int(11) DEFAULT NULL COMMENT '保质期天数',
  `status` char(1) DEFAULT '1' COMMENT '状态（0 停用 1 启用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`product_id`),
  UNIQUE KEY `uk_product_code` (`product_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='物料表';

-- ----------------------------
-- 2. 库位表
-- ----------------------------
DROP TABLE IF EXISTS `wms_location`;
CREATE TABLE `wms_location` (
  `location_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '库位 ID',
  `location_code` varchar(50) NOT NULL COMMENT '库位编码',
  `location_name` varchar(200) NOT NULL COMMENT '库位名称',
  `location_type` char(1) DEFAULT '1' COMMENT '库位类型（1 存储位 2 拣货位 3 暂存位）',
  `area_code` varchar(50) DEFAULT NULL COMMENT '区域编码',
  `row_no` varchar(20) DEFAULT NULL COMMENT '行号',
  `column_no` varchar(20) DEFAULT NULL COMMENT '列号',
  `level_no` varchar(20) DEFAULT NULL COMMENT '层号',
  `max_weight` decimal(10,2) DEFAULT NULL COMMENT '最大承重',
  `max_volume` decimal(10,2) DEFAULT NULL COMMENT '最大体积',
  `status` char(1) DEFAULT '0' COMMENT '状态（0 正常 1 停用 2 占用 3 锁定）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`location_id`),
  UNIQUE KEY `uk_location_code` (`location_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='库位表';

-- ----------------------------
-- 3. 入库单表
-- ----------------------------
DROP TABLE IF EXISTS `wms_inbound_order`;
CREATE TABLE `wms_inbound_order` (
  `inbound_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '入库单 ID',
  `inbound_no` varchar(50) NOT NULL COMMENT '入库单号',
  `inbound_type` char(1) DEFAULT '1' COMMENT '入库类型（1 采购入库 2 退货入库 3 调拨入库）',
  `source_type` char(1) DEFAULT NULL COMMENT '来源类型（1 采购订单 2 退货单 3 调拨单）',
  `source_no` varchar(50) DEFAULT NULL COMMENT '来源单号',
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '供应商 ID',
  `supplier_name` varchar(200) DEFAULT NULL COMMENT '供应商名称',
  `warehouse_id` bigint(20) DEFAULT NULL COMMENT '仓库 ID',
  `status` char(1) DEFAULT '0' COMMENT '状态（0 无 1 待收货 2 收货中 3 待质检 4 待上架 5 已入库）',
  `is_finished` tinyint(1) DEFAULT '0' COMMENT '是否完成（0 草稿 1 已完成）',
  `total_qty` decimal(10,2) DEFAULT '0' COMMENT '总数量',
  `received_qty` decimal(10,2) DEFAULT '0' COMMENT '已收货数量',
  `qualified_qty` decimal(10,2) DEFAULT '0' COMMENT '合格数量',
  `unqualified_qty` decimal(10,2) DEFAULT '0' COMMENT '不合格数量',
  `expected_date` date DEFAULT NULL COMMENT '预计到货日期',
  `actual_date` date DEFAULT NULL COMMENT '实际到货日期',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`inbound_id`),
  UNIQUE KEY `uk_inbound_no` (`inbound_no`),
  KEY `idx_supplier_id` (`supplier_id`),
  KEY `idx_warehouse_id` (`warehouse_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='入库单表';

-- ----------------------------
-- 4. 入库明细表
-- ----------------------------
DROP TABLE IF EXISTS `wms_inbound_item`;
CREATE TABLE `wms_inbound_item` (
  `item_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '明细 ID',
  `inbound_id` bigint(20) NOT NULL COMMENT '入库单 ID',
  `product_id` bigint(20) NOT NULL COMMENT '物料 ID',
  `product_code` varchar(50) DEFAULT NULL COMMENT '物料编码',
  `product_name` varchar(200) DEFAULT NULL COMMENT '物料名称',
  `expected_qty` decimal(10,2) DEFAULT '0' COMMENT '应收数量',
  `received_qty` decimal(10,2) DEFAULT '0' COMMENT '实收数量',
  `qualified_qty` decimal(10,2) DEFAULT '0' COMMENT '合格数量',
  `unqualified_qty` decimal(10,2) DEFAULT '0' COMMENT '不合格数量',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `batch_no` varchar(50) DEFAULT NULL COMMENT '批次号',
  `production_date` date DEFAULT NULL COMMENT '生产日期',
  `expiry_date` date DEFAULT NULL COMMENT '有效期至',
  `location_id` bigint(20) DEFAULT NULL COMMENT '上架库位 ID',
  `status` char(1) DEFAULT '0' COMMENT '状态（0 待收货 1 已收货 2 待质检 3 已质检 4 已上架）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`item_id`),
  KEY `idx_inbound_id` (`inbound_id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_location_id` (`location_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='入库明细表';

-- ----------------------------
-- 5. 出库单表
-- ----------------------------
DROP TABLE IF EXISTS `wms_outbound_order`;
CREATE TABLE `wms_outbound_order` (
  `outbound_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '出库单 ID',
  `outbound_no` varchar(50) NOT NULL COMMENT '出库单号',
  `outbound_type` char(1) DEFAULT '1' COMMENT '出库类型（1 销售出库 2 采购退货 3 调拨出库 4 其他出库）',
  `order_no` varchar(50) DEFAULT NULL COMMENT '订单编号',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户 ID',
  `customer_name` varchar(200) DEFAULT NULL COMMENT '客户名称',
  `warehouse_id` bigint(20) DEFAULT NULL COMMENT '仓库 ID',
  `status` char(1) DEFAULT '0' COMMENT '状态（0 无 1 待审核 2 待拣货 3 拣货中 4 待复核 5 待打包 6 待发货 7 已发货 8 已取消）',
  `is_finished` tinyint(1) DEFAULT '0' COMMENT '是否完成（0 草稿 1 已完成）',
  `priority` char(1) DEFAULT '1' COMMENT '优先级（1 普通 2 加急 3 特急）',
  `wave_no` varchar(50) DEFAULT NULL COMMENT '波次号',
  `total_qty` decimal(10,2) DEFAULT '0' COMMENT '总数量',
  `picked_qty` decimal(10,2) DEFAULT '0' COMMENT '已拣货数量',
  `shipped_qty` decimal(10,2) DEFAULT '0' COMMENT '已发货数量',
  `expected_date` date DEFAULT NULL COMMENT '预计发货日期',
  `actual_date` date DEFAULT NULL COMMENT '实际发货日期',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`outbound_id`),
  UNIQUE KEY `uk_outbound_no` (`outbound_no`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_warehouse_id` (`warehouse_id`),
  KEY `idx_status` (`status`),
  KEY `idx_wave_no` (`wave_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='出库单表';

-- ----------------------------
-- 6. 出库明细表
-- ----------------------------
DROP TABLE IF EXISTS `wms_outbound_item`;
CREATE TABLE `wms_outbound_item` (
  `item_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '明细 ID',
  `outbound_id` bigint(20) NOT NULL COMMENT '出库单 ID',
  `product_id` bigint(20) NOT NULL COMMENT '物料 ID',
  `product_code` varchar(50) DEFAULT NULL COMMENT '物料编码',
  `product_name` varchar(200) DEFAULT NULL COMMENT '物料名称',
  `order_qty` decimal(10,2) DEFAULT '0' COMMENT '订单数量',
  `picked_qty` decimal(10,2) DEFAULT '0' COMMENT '已拣货数量',
  `shipped_qty` decimal(10,2) DEFAULT '0' COMMENT '已发货数量',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `picking_strategy` char(1) DEFAULT '1' COMMENT '拣货策略（1 FIFO 2 LIFO 3 近效期优先）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`item_id`),
  KEY `idx_outbound_id` (`outbound_id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='出库明细表';

-- ----------------------------
-- 7. 库存表
-- ----------------------------
DROP TABLE IF EXISTS `wms_inventory`;
CREATE TABLE `wms_inventory` (
  `inventory_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '库存 ID',
  `warehouse_id` bigint(20) DEFAULT NULL COMMENT '仓库 ID',
  `location_id` bigint(20) DEFAULT NULL COMMENT '库位 ID',
  `product_id` bigint(20) NOT NULL COMMENT '物料 ID',
  `product_code` varchar(50) DEFAULT NULL COMMENT '物料编码',
  `product_name` varchar(200) DEFAULT NULL COMMENT '物料名称',
  `batch_no` varchar(50) DEFAULT NULL COMMENT '批次号',
  `production_date` date DEFAULT NULL COMMENT '生产日期',
  `expiry_date` date DEFAULT NULL COMMENT '有效期至',
  `serial_no` varchar(100) DEFAULT NULL COMMENT '序列号',
  `qty_on_hand` decimal(10,2) DEFAULT '0' COMMENT '现有数量',
  `qty_available` decimal(10,2) DEFAULT '0' COMMENT '可用数量',
  `qty_locked` decimal(10,2) DEFAULT '0' COMMENT '锁定数量',
  `qty_damaged` decimal(10,2) DEFAULT '0' COMMENT '损坏数量',
  `unit_cost` decimal(12,4) DEFAULT '0' COMMENT '单位成本',
  `total_cost` decimal(14,2) DEFAULT '0' COMMENT '总成本',
  `status` char(1) DEFAULT '1' COMMENT '状态（1 正常 2 锁定 3 冻结）',
  `last_stock_check_date` date DEFAULT NULL COMMENT '最后盘点日期',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`inventory_id`),
  KEY `idx_warehouse_id` (`warehouse_id`),
  KEY `idx_location_id` (`location_id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_batch_no` (`batch_no`),
  KEY `idx_expiry_date` (`expiry_date`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='库存表';

-- ----------------------------
-- 8. 库存台账表
-- ----------------------------
DROP TABLE IF EXISTS `wms_inventory_ledger`;
CREATE TABLE `wms_inventory_ledger` (
  `ledger_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '台账 ID',
  `transaction_no` varchar(50) DEFAULT NULL COMMENT '交易编号',
  `transaction_type` char(1) DEFAULT NULL COMMENT '交易类型（1 入库 2 出库 3 盘点调整 4 移库 5 冻结/解冻）',
  `warehouse_id` bigint(20) DEFAULT NULL COMMENT '仓库 ID',
  `location_id` bigint(20) DEFAULT NULL COMMENT '库位 ID',
  `product_id` bigint(20) NOT NULL COMMENT '物料 ID',
  `product_code` varchar(50) DEFAULT NULL COMMENT '物料编码',
  `product_name` varchar(200) DEFAULT NULL COMMENT '物料名称',
  `batch_no` varchar(50) DEFAULT NULL COMMENT '批次号',
  `qty_before` decimal(10,2) DEFAULT '0' COMMENT '变动前数量',
  `qty_change` decimal(10,2) DEFAULT '0' COMMENT '变动数量 (+入 - 出)',
  `qty_after` decimal(10,2) DEFAULT '0' COMMENT '变动后数量',
  `unit_cost` decimal(12,4) DEFAULT '0' COMMENT '单位成本',
  `reference_type` varchar(50) DEFAULT NULL COMMENT '关联单据类型',
  `reference_id` bigint(20) DEFAULT NULL COMMENT '关联单据 ID',
  `reference_no` varchar(50) DEFAULT NULL COMMENT '关联单据号',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`ledger_id`),
  KEY `idx_transaction_no` (`transaction_no`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_reference_type` (`reference_type`),
  KEY `idx_reference_id` (`reference_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='库存台账表';

-- ----------------------------
-- 9. 盘点单表
-- ----------------------------
DROP TABLE IF EXISTS `wms_stock_check`;
CREATE TABLE `wms_stock_check` (
  `check_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '盘点单 ID',
  `check_no` varchar(50) NOT NULL COMMENT '盘点单号',
  `check_type` char(1) DEFAULT '1' COMMENT '盘点类型（1 定期盘点 2 循环盘点 3 动盘 4 静盘）',
  `warehouse_id` bigint(20) DEFAULT NULL COMMENT '仓库 ID',
  `location_id` bigint(20) DEFAULT NULL COMMENT '库位 ID',
  `product_id` bigint(20) DEFAULT NULL COMMENT '物料 ID',
  `status` char(1) DEFAULT '0' COMMENT '状态（0 草稿 1 盘点中 2 待审核 3 已完成 4 已取消）',
  `plan_date` date DEFAULT NULL COMMENT '计划盘点日期',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `total_qty` int(11) DEFAULT '0' COMMENT '应盘数量',
  `checked_qty` int(11) DEFAULT '0' COMMENT '已盘数量',
  `diff_qty` int(11) DEFAULT '0' COMMENT '差异数量',
  `diff_amount` decimal(14,2) DEFAULT '0' COMMENT '差异金额',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`check_id`),
  UNIQUE KEY `uk_check_no` (`check_no`),
  KEY `idx_warehouse_id` (`warehouse_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='盘点单表';

-- ----------------------------
-- 10. 移库单表
-- ----------------------------
DROP TABLE IF EXISTS `wms_transfer`;
CREATE TABLE `wms_transfer` (
  `transfer_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '移库单 ID',
  `transfer_no` varchar(50) NOT NULL COMMENT '移库单号',
  `warehouse_id` bigint(20) DEFAULT NULL COMMENT '仓库 ID',
  `from_location_id` bigint(20) DEFAULT NULL COMMENT '源库位 ID',
  `to_location_id` bigint(20) DEFAULT NULL COMMENT '目标库位 ID',
  `status` char(1) DEFAULT '0' COMMENT '状态（0 草稿 1 待执行 2 已完成 3 已取消）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`transfer_id`),
  UNIQUE KEY `uk_transfer_no` (`transfer_no`),
  KEY `idx_warehouse_id` (`warehouse_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='移库单表';

-- ----------------------------
-- 11. 库存预警表
-- ----------------------------
DROP TABLE IF EXISTS `wms_alert`;
CREATE TABLE `wms_alert` (
  `alert_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '预警 ID',
  `product_id` bigint(20) DEFAULT NULL COMMENT '物料 ID',
  `product_code` varchar(50) DEFAULT NULL COMMENT '物料编码',
  `product_name` varchar(200) DEFAULT NULL COMMENT '物料名称',
  `warehouse_id` bigint(20) DEFAULT NULL COMMENT '仓库 ID',
  `alert_type` char(1) DEFAULT NULL COMMENT '预警类型（1 低于安全库存 2 高于最高库存 3 近效期 4 已过期 5 呆滞）',
  `alert_level` char(1) DEFAULT NULL COMMENT '预警级别（1 提示 2 警告 3 严重）',
  `qty_on_hand` decimal(10,2) DEFAULT '0' COMMENT '当前库存',
  `safety_stock` decimal(10,2) DEFAULT '0' COMMENT '安全库存',
  `expiry_date` date DEFAULT NULL COMMENT '有效期至',
  `days_until_expiry` int(11) DEFAULT '0' COMMENT '距离过期天数',
  `last_move_date` date DEFAULT NULL COMMENT '最后移动日期',
  `status` char(1) DEFAULT '0' COMMENT '状态（0 未处理 1 已处理 2 已忽略）',
  `handle_by` varchar(64) DEFAULT NULL COMMENT '处理人',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `handle_remark` varchar(500) DEFAULT NULL COMMENT '处理备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`alert_id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_warehouse_id` (`warehouse_id`),
  KEY `idx_alert_type` (`alert_type`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='库存预警表';
