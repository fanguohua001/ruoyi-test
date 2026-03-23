-- ----------------------------
-- WMS 库存履历菜单权限 SQL
-- ----------------------------

-- 获取仓储管理菜单 ID (假设 parent_id=0 表示顶级菜单)
SET @wmsParentId = (SELECT menu_id FROM sys_menu WHERE menu_name = '仓储管理' AND parent_id = 0 LIMIT 1);

-- 检查是否已存在库存履历菜单，不存在则插入
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, remark)
SELECT '库存履历', @wmsParentId, 4, 'inventoryLedger', 'wms/inventoryLedger/index', 1, 0, 'C', '0', '0', 'log', 'admin', NOW(), '库存履历菜单'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_name = '库存履历' AND component = 'wms/inventoryLedger/index');

-- 获取库存履历菜单 ID
SET @inventoryLedgerMenuId = (SELECT menu_id FROM sys_menu WHERE menu_name = '库存履历' AND component = 'wms/inventoryLedger/index' LIMIT 1);

-- 插入权限按钮（如果不存在）
-- 履历查询按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
SELECT '履历查询', @inventoryLedgerMenuId, 1, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:inventoryLedger:list', '履历查询按钮'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'wms:inventoryLedger:list');

-- 履历导出按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
SELECT '履历导出', @inventoryLedgerMenuId, 2, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:inventoryLedger:export', '履历导出按钮'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'wms:inventoryLedger:export');

-- 显示插入结果
SELECT menu_id, menu_name, parent_id, perms FROM sys_menu WHERE menu_name LIKE '%履历%' OR perms LIKE 'wms:inventoryLedger%';
