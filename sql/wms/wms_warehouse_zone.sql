-- ----------------------------
-- 仓库表
-- ----------------------------
DROP TABLE IF EXISTS `wms_warehouse`;
CREATE TABLE `wms_warehouse` (
  `warehouse_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '仓库 ID',
  `warehouse_code` varchar(50) NOT NULL COMMENT '仓库编码',
  `warehouse_name` varchar(200) NOT NULL COMMENT '仓库名称',
  `warehouse_type` char(1) DEFAULT '1' COMMENT '仓库类型（1 原料仓 2 成品仓 3 辅料仓 4 备件仓）',
  `address` varchar(500) DEFAULT NULL COMMENT '地址',
  `person_in_charge` varchar(50) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `area` decimal(10,2) DEFAULT NULL COMMENT '面积（平方米）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0 正常 1 停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`warehouse_id`),
  UNIQUE KEY `uk_warehouse_code` (`warehouse_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='仓库表';

-- ----------------------------
-- 库区表
-- ----------------------------
DROP TABLE IF EXISTS `wms_warehouse_zone`;
CREATE TABLE `wms_warehouse_zone` (
  `zone_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '库区 ID',
  `zone_code` varchar(50) NOT NULL COMMENT '库区编码',
  `zone_name` varchar(200) NOT NULL COMMENT '库区名称',
  `warehouse_id` bigint(20) NOT NULL COMMENT '仓库 ID',
  `zone_type` char(1) DEFAULT '1' COMMENT '库区类型（1 存储区 2 拣货区 3 收货区 4 发货区 5 退货区 6 质检区）',
  `order_num` int(4) DEFAULT '0' COMMENT '顺序号',
  `status` char(1) DEFAULT '0' COMMENT '状态（0 正常 1 停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`zone_id`),
  UNIQUE KEY `uk_zone_code` (`zone_code`),
  KEY `idx_warehouse_id` (`warehouse_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='库区表';

-- ----------------------------
-- 修改库位表，添加仓库 ID 和库区 ID 关联
-- ----------------------------
ALTER TABLE `wms_location`
ADD COLUMN `warehouse_id` bigint(20) DEFAULT NULL COMMENT '仓库 ID' AFTER `location_name`,
ADD COLUMN `zone_id` bigint(20) DEFAULT NULL COMMENT '库区 ID' AFTER `warehouse_id`,
ADD KEY `idx_warehouse_id` (`warehouse_id`),
ADD KEY `idx_zone_id` (`zone_id`);
