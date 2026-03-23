-- ----------------------------
-- 仓库库区管理菜单权限 SQL
-- ----------------------------

-- 获取基础资料菜单 ID
SET @basicId = (SELECT menu_id FROM sys_menu WHERE menu_name = '基础资料' AND parent_id IN (SELECT menu_id FROM sys_menu WHERE menu_name = '仓储管理') LIMIT 1);

-- 如果基础资料菜单不存在，则使用仓储管理菜单 ID
IF @basicId IS NULL THEN
    SET @basicId = (SELECT menu_id FROM sys_menu WHERE menu_name = '仓储管理' LIMIT 1);
END IF;

-- ==================== 仓库管理 ====================
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, remark)
VALUES ('仓库管理', IFNULL(@basicId, 0), 1, 'warehouse', 'wms/warehouse/index', 1, 0, 'C', '0', '0', 'home', 'admin', NOW(), '仓库管理菜单');

SET @warehouseMenuId = LAST_INSERT_ID();

-- 仓库管理按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('仓库查询', @warehouseMenuId, 1, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:warehouse:list', '仓库查询按钮');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('仓库新增', @warehouseMenuId, 2, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:warehouse:add', '仓库新增按钮');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('仓库修改', @warehouseMenuId, 3, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:warehouse:edit', '仓库修改按钮');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('仓库删除', @warehouseMenuId, 4, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:warehouse:remove', '仓库删除按钮');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('仓库导出', @warehouseMenuId, 5, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:warehouse:export', '仓库导出按钮');

-- ==================== 库区管理 ====================
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, remark)
VALUES ('库区管理', IFNULL(@basicId, 0), 2, 'zone', 'wms/zone/index', 1, 0, 'C', '0', '0', 'area-chart', 'admin', NOW(), '库区管理菜单');

SET @zoneMenuId = LAST_INSERT_ID();

-- 库区管理按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('库区查询', @zoneMenuId, 1, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:zone:list', '库区查询按钮');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('库区新增', @zoneMenuId, 2, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:zone:add', '库区新增按钮');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('库区修改', @zoneMenuId, 3, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:zone:edit', '库区修改按钮');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('库区删除', @zoneMenuId, 4, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:zone:remove', '库区删除按钮');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('库区导出', @zoneMenuId, 5, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:zone:export', '库区导出按钮');
