-- ----------------------------
-- 供应商管理菜单权限 SQL
-- ----------------------------

-- 在基础资料下添加供应商管理菜单
-- 首先获取基础资料菜单的 ID（假设已存在）
-- 如果仓储管理菜单已存在，需要先查询出对应的 parent_id
-- 这里使用动态 SQL，实际执行时需要先获取基础资料菜单 ID

-- 获取基础资料菜单 ID
SET @basicId = (SELECT menu_id FROM sys_menu WHERE menu_name = '基础资料' AND parent_id IN (SELECT menu_id FROM sys_menu WHERE menu_name = '仓储管理') LIMIT 1);

-- 供应商管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, remark)
VALUES ('供应商管理', IFNULL(@basicId, 0), 4, 'supplier', 'wms/supplier/index', 1, 0, 'C', '0', '0', 'user', 'admin', NOW(), '供应商管理菜单');

SET @supplierMenuId = LAST_INSERT_ID();

-- 供应商管理按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('供应商查询', @supplierMenuId, 1, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:supplier:list', '供应商查询按钮');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('供应商新增', @supplierMenuId, 2, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:supplier:add', '供应商新增按钮');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('供应商修改', @supplierMenuId, 3, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:supplier:edit', '供应商修改按钮');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('供应商删除', @supplierMenuId, 4, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:supplier:remove', '供应商删除按钮');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('供应商导出', @supplierMenuId, 5, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:supplier:export', '供应商导出按钮');
