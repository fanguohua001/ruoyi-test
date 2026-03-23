-- ----------------------------
-- 添加客户订单菜单权限
-- ----------------------------

-- 获取仓储管理菜单 ID
SET @parentId = (SELECT menu_id FROM sys_menu WHERE menu_name = '仓储管理' AND parent_id = 0 LIMIT 1);

-- 检查是否已存在客户订单菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, remark)
SELECT '客户订单', @parentId, 8, 'customerOrder', 'wms/customerOrder/index', 1, 0, 'C', '0', '0', 'form', 'admin', NOW(), '客户订单菜单'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_name = '客户订单' AND parent_id = @parentId);

-- 获取客户订单菜单 ID
SET @customerOrderMenuId = (SELECT menu_id FROM sys_menu WHERE menu_name = '客户订单' AND parent_id = @parentId LIMIT 1);

-- 添加权限按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
SELECT '订单查询', @customerOrderMenuId, 1, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:customerOrder:list', '订单查询按钮'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'wms:customerOrder:list');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
SELECT '订单新增', @customerOrderMenuId, 2, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:customerOrder:add', '订单新增按钮'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'wms:customerOrder:add');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
SELECT '订单修改', @customerOrderMenuId, 3, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:customerOrder:edit', '订单修改按钮'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'wms:customerOrder:edit');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
SELECT '订单删除', @customerOrderMenuId, 4, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:customerOrder:remove', '订单删除按钮'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'wms:customerOrder:remove');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
SELECT '订单完成', @customerOrderMenuId, 5, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:customerOrder:finish', '订单完成按钮'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'wms:customerOrder:finish');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
SELECT '订单导出', @customerOrderMenuId, 6, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:customerOrder:export', '订单导出按钮'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'wms:customerOrder:export');
