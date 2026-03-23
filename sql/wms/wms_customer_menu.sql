-- ----------------------------
-- 客户管理菜单权限 SQL
-- ----------------------------

-- 在基础资料下添加客户管理菜单
-- 首先获取基础资料菜单的 ID（假设已存在）
-- 如果仓储管理菜单已存在，需要先查询出对应的 parent_id
-- 这里使用动态 SQL，实际执行时需要先获取基础资料菜单 ID

-- 获取基础资料菜单 ID
SET @basicId = (SELECT menu_id FROM sys_menu WHERE menu_name = '基础资料' AND parent_id IN (SELECT menu_id FROM sys_menu WHERE menu_name = '仓储管理') LIMIT 1);

-- 客户管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, remark)
VALUES ('客户管理', IFNULL(@basicId, 0), 5, 'customer', 'wms/customer/index', 1, 0, 'C', '0', '0', 'user', 'admin', NOW(), '客户管理菜单');

SET @customerMenuId = LAST_INSERT_ID();

-- 客户管理按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('客户查询', @customerMenuId, 1, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:customer:list', '客户查询按钮');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('客户新增', @customerMenuId, 2, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:customer:add', '客户新增按钮');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('客户修改', @customerMenuId, 3, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:customer:edit', '客户修改按钮');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('客户删除', @customerMenuId, 4, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:customer:remove', '客户删除按钮');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, icon, create_by, create_time, perms, remark)
VALUES ('客户导出', @customerMenuId, 5, '#', '', 1, 0, 'F', '0', '0', '#', 'admin', NOW(), 'wms:customer:export', '客户导出按钮');
