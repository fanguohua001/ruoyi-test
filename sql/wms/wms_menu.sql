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

-- 商品管理
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, remark)
VALUES ('商品管理', @basicId, 1, 'product', 'wms/product/index', 1, 0, 'C', '0', '0', 'product', 'admin', NOW(), '商品管理菜单');

SET @productMenuId = LAST_INSERT_ID();
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('商品查询', @productMenuId, 1, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:product:list', '商品查询按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('商品新增', @productMenuId, 2, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:product:add', '商品新增按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('商品修改', @productMenuId, 3, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:product:edit', '商品修改按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('商品删除', @productMenuId, 4, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:product:remove', '商品删除按钮');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('商品导出', @productMenuId, 5, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:product:export', '商品导出按钮');

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
