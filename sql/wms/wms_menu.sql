-- ----------------------------
-- WMS 仓储管理模块菜单权限 SQL
-- ----------------------------

-- 插入顶级菜单：仓储管理
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, remark)
VALUES ('仓储管理', 0, 5, 'wms', NULL, 1, 0, 'M', '0', '0', 'warehouse', 'admin', NOW(), '仓储管理模块');

-- 获取刚插入的菜单 ID
SET @parentId = LAST_INSERT_ID();

-- ==================== 基础资料 ====================
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, remark)
VALUES ('基础资料', @parentId, 1, 'basic', NULL, 1, 0, 'M', '0', '0', 'product', 'admin', NOW(), '基础资料管理');

SET @basicId = LAST_INSERT_ID();

-- 物料管理
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, remark)
VALUES ('物料管理', @basicId, 1, 'product', 'wms/product/index', 1, 0, 'C', '0', '0', 'product', 'admin', NOW(), '物料管理菜单');

SET @productMenuId = LAST_INSERT_ID();
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('物料查询', @productMenuId, 1, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:product:list', '物料查询按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('物料新增', @productMenuId, 2, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:product:add', '物料新增按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('物料修改', @productMenuId, 3, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:product:edit', '物料修改按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('物料删除', @productMenuId, 4, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:product:remove', '物料删除按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('物料导出', @productMenuId, 5, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:product:export', '物料导出按钮');

-- 物料分类管理
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, remark)
VALUES ('物料分类', @basicId, 3, 'category', 'wms/category/index', 1, 0, 'C', '0', '0', 'list', 'admin', NOW(), '物料分类菜单');

SET @categoryMenuId = LAST_INSERT_ID();
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('分类查询', @categoryMenuId, 1, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:category:list', '分类查询按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('分类新增', @categoryMenuId, 2, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:category:add', '分类新增按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('分类修改', @categoryMenuId, 3, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:category:edit', '分类修改按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('分类删除', @categoryMenuId, 4, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:category:remove', '分类删除按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('分类导出', @categoryMenuId, 5, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:category:export', '分类导出按钮');

-- 库位管理
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, remark)
VALUES ('库位管理', @basicId, 2, 'location', 'wms/location/index', 1, 0, 'C', '0', '0', 'location', 'admin', NOW(), '库位管理菜单');

SET @locationMenuId = LAST_INSERT_ID();
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('库位查询', @locationMenuId, 1, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:location:list', '库位查询按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('库位新增', @locationMenuId, 2, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:location:add', '库位新增按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('库位修改', @locationMenuId, 3, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:location:edit', '库位修改按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('库位删除', @locationMenuId, 4, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:location:remove', '库位删除按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('库位导出', @locationMenuId, 5, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:location:export', '库位导出按钮');

-- ==================== 入库管理 ====================
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, remark)
VALUES ('入库管理', @parentId, 2, 'inbound', 'wms/inbound/index', 1, 0, 'C', '0', '0', 'inbound', 'admin', NOW(), '入库管理菜单');

SET @inboundMenuId = LAST_INSERT_ID();
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('入库查询', @inboundMenuId, 1, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:inbound:list', '入库查询按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('入库新增', @inboundMenuId, 2, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:inbound:add', '入库新增按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('入库修改', @inboundMenuId, 3, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:inbound:edit', '入库修改按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('入库删除', @inboundMenuId, 4, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:inbound:remove', '入库删除按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('入库收货', @inboundMenuId, 5, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:inbound:receive', '入库收货按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('入库质检', @inboundMenuId, 6, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:inbound:quality', '入库质检按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('入库上架', @inboundMenuId, 7, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:inbound:putaway', '入库上架按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('入库完成', @inboundMenuId, 8, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:inbound:finish', '入库完成按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('入库导出', @inboundMenuId, 9, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:inbound:export', '入库导出按钮');

-- ==================== 出库管理 ====================
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, remark)
VALUES ('出库管理', @parentId, 3, 'outbound', 'wms/outbound/index', 1, 0, 'C', '0', '0', 'outbound', 'admin', NOW(), '出库管理菜单');

SET @outboundMenuId = LAST_INSERT_ID();
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('出库查询', @outboundMenuId, 1, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:outbound:list', '出库查询按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('出库新增', @outboundMenuId, 2, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:outbound:add', '出库新增按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('出库修改', @outboundMenuId, 3, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:outbound:edit', '出库修改按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('出库删除', @outboundMenuId, 4, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:outbound:remove', '出库删除按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('出库拣货', @outboundMenuId, 5, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:outbound:picking', '出库拣货按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('出库复核', @outboundMenuId, 6, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:outbound:review', '出库复核按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('出库打包', @outboundMenuId, 7, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:outbound:pack', '出库打包按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('出库发货', @outboundMenuId, 8, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:outbound:ship', '出库发货按钮');

-- ==================== 库存管理 ====================
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, remark)
VALUES ('库存管理', @parentId, 4, 'inventory', 'wms/inventory/index', 1, 0, 'C', '0', '0', 'inventory', 'admin', NOW(), '库存管理菜单');

SET @inventoryMenuId = LAST_INSERT_ID();
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('库存查询', @inventoryMenuId, 1, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:inventory:list', '库存查询按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('库存导出', @inventoryMenuId, 2, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:inventory:export', '库存导出按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('库存冻结', @inventoryMenuId, 3, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:inventory:freeze', '库存冻结按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('库存解冻', @inventoryMenuId, 4, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:inventory:unfreeze', '库存解冻按钮');

-- ==================== 库存预警 ====================
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, remark)
VALUES ('库存预警', @parentId, 5, 'alert', 'wms/alert/index', 1, 0, 'C', '0', '0', 'warning', 'admin', NOW(), '库存预警菜单');

SET @alertMenuId = LAST_INSERT_ID();
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('预警查询', @alertMenuId, 1, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:alert:list', '预警查询按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('预警处理', @alertMenuId, 2, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:alert:handle', '预警处理按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('预警生成', @alertMenuId, 3, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:alert:generate', '预警生成按钮');

-- ==================== 盘点管理 ====================
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, remark)
VALUES ('盘点管理', @parentId, 6, 'stockCheck', 'wms/stockCheck/index', 1, 0, 'C', '0', '0', 'list', 'admin', NOW(), '盘点管理菜单');

SET @stockCheckMenuId = LAST_INSERT_ID();
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('盘点查询', @stockCheckMenuId, 1, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:stockCheck:list', '盘点查询按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('盘点新增', @stockCheckMenuId, 2, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:stockCheck:add', '盘点新增按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('盘点修改', @stockCheckMenuId, 3, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:stockCheck:edit', '盘点修改按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('盘点删除', @stockCheckMenuId, 4, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:stockCheck:remove', '盘点删除按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('盘点导出', @stockCheckMenuId, 5, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:stockCheck:export', '盘点导出按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('开始盘点', @stockCheckMenuId, 6, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:stockCheck:start', '开始盘点按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('完成盘点', @stockCheckMenuId, 7, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:stockCheck:finish', '完成盘点按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('审核盘点', @stockCheckMenuId, 8, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:stockCheck:approve', '审核盘点按钮');

-- ==================== 移库管理 ====================
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, remark)
VALUES ('移库管理', @parentId, 7, 'transfer', 'wms/transfer/index', 1, 0, 'C', '0', '0', 'bounce', 'admin', NOW(), '移库管理菜单');

SET @transferMenuId = LAST_INSERT_ID();
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('移库查询', @transferMenuId, 1, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:transfer:list', '移库查询按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('移库新增', @transferMenuId, 2, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:transfer:add', '移库新增按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('移库修改', @transferMenuId, 3, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:transfer:edit', '移库修改按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('移库删除', @transferMenuId, 4, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:transfer:remove', '移库删除按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('移库导出', @transferMenuId, 5, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:transfer:export', '移库导出按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('执行移库', @transferMenuId, 6, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:transfer:execute', '执行移库按钮');

-- ==================== 客户订单管理 ====================
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, remark)
VALUES ('客户订单', @parentId, 8, 'customerOrder', 'wms/customerOrder/index', 1, 0, 'C', '0', '0', 'form', 'admin', NOW(), '客户订单菜单');

SET @customerOrderMenuId = LAST_INSERT_ID();
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('订单查询', @customerOrderMenuId, 1, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:customerOrder:list', '订单查询按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('订单新增', @customerOrderMenuId, 2, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:customerOrder:add', '订单新增按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('订单修改', @customerOrderMenuId, 3, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:customerOrder:edit', '订单修改按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('订单删除', @customerOrderMenuId, 4, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:customerOrder:remove', '订单删除按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('订单完成', @customerOrderMenuId, 5, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:customerOrder:finish', '订单完成按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('订单导出', @customerOrderMenuId, 6, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:customerOrder:export', '订单导出按钮');
