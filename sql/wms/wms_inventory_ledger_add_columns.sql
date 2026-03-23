-- ----------------------------
-- 为 wms_inventory_ledger 表添加缺失的字段
-- 执行时间：2026-03-18
-- ----------------------------

-- 添加物料编码字段
ALTER TABLE wms_inventory_ledger ADD COLUMN product_code VARCHAR(50) AFTER product_id;

-- 添加物料名称字段
ALTER TABLE wms_inventory_ledger ADD COLUMN product_name VARCHAR(100) AFTER product_code;

-- 添加关联单据号字段
ALTER TABLE wms_inventory_ledger ADD COLUMN reference_no VARCHAR(50) AFTER reference_id;
