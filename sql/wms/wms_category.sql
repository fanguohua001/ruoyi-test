-- ----------------------------
-- 物料分类表
-- ----------------------------
DROP TABLE IF EXISTS `wms_category`;
CREATE TABLE `wms_category` (
  `category_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类 ID',
  `category_code` varchar(50) NOT NULL COMMENT '分类编码',
  `category_name` varchar(100) NOT NULL COMMENT '分类名称',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父分类 ID',
  `order_num` int(4) DEFAULT '0' COMMENT '排序',
  `status` char(1) DEFAULT '0' COMMENT '状态（0 正常 1 停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`category_id`),
  UNIQUE KEY `uk_category_code` (`category_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='物料分类表';

-- ----------------------------
-- 初始化分类数据
-- ----------------------------
INSERT INTO `wms_category` (`category_id`, `category_code`, `category_name`, `parent_id`, `order_num`, `status`, `remark`) VALUES
(1, 'RAW', '原材料', 0, 1, '1', '原材料分类'),
(2, 'SEMI', '半成品', 0, 2, '1', '半成品分类'),
(3, 'FINISHED', '成品', 0, 3, '1', '成品分类'),
(4, 'PART', '零部件', 0, 4, '1', '零部件分类'),
(5, 'OTHER', '其他', 0, 99, '1', '其他物料分类');
