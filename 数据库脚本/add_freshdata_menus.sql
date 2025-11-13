-- =============================================
-- 添加生鲜数据分析菜单和权限
-- =============================================
-- 执行方式：在数据库中运行此脚本
-- =============================================

USE ry;

-- 删除旧的生鲜数据菜单（如果存在）
DELETE FROM sys_menu WHERE menu_id >= 2000 AND menu_id < 3000;

-- ====== 添加生鲜数据分析主菜单 ======
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, url, target, menu_type, visible, perms, icon, create_by, create_time, remark)
VALUES (2000, '生鲜数据', 0, 2, '#', '', 'M', '0', '', 'fa fa-bar-chart-o', 'admin', NOW(), '生鲜电商数据分析菜单');

-- ====== 添加数据分析子菜单 ======
-- 数据概览
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, url, target, menu_type, visible, perms, icon, create_by, create_time, remark)
VALUES (2001, '数据概览', 2000, 1, '/freshdata/overview', 'menuItem', 'C', '0', 'freshdata:analysis:overview', 'fa fa-dashboard', 'admin', NOW(), '数据概览页面');

-- 用户行为分析
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, url, target, menu_type, visible, perms, icon, create_by, create_time, remark)
VALUES (2002, '用户行为', 2000, 2, '/freshdata/behavior', 'menuItem', 'C', '0', 'freshdata:analysis:login', 'fa fa-line-chart', 'admin', NOW(), '用户行为分析');

-- 消费行为分析
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, url, target, menu_type, visible, perms, icon, create_by, create_time, remark)
VALUES (2003, '消费分析', 2000, 3, '/freshdata/consumption', 'menuItem', 'C', '0', 'freshdata:analysis:consumption', 'fa fa-shopping-cart', 'admin', NOW(), '消费行为分析');

-- 用户画像分析
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, url, target, menu_type, visible, perms, icon, create_by, create_time, remark)
VALUES (2004, '用户画像', 2000, 4, '/freshdata/profile', 'menuItem', 'C', '0', 'freshdata:analysis:profile', 'fa fa-user', 'admin', NOW(), '用户画像分析');

-- 社交行为分析
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, url, target, menu_type, visible, perms, icon, create_by, create_time, remark)
VALUES (2005, '社交分析', 2000, 5, '/freshdata/social', 'menuItem', 'C', '0', 'freshdata:analysis:social', 'fa fa-share-alt', 'admin', NOW(), '社交行为分析');

-- 内容偏好分析
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, url, target, menu_type, visible, perms, icon, create_by, create_time, remark)
VALUES (2006, '内容偏好', 2000, 6, '/freshdata/content', 'menuItem', 'C', '0', 'freshdata:analysis:content', 'fa fa-heart', 'admin', NOW(), '内容偏好分析');

-- ====== 给admin角色分配权限 ======
-- 清除旧的权限关联（如果存在）
DELETE FROM sys_role_menu WHERE menu_id >= 2000 AND menu_id < 3000;

-- 给角色ID=1（超级管理员）分配所有生鲜数据菜单权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2000);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2001);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2002);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2003);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2004);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2005);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2006);

COMMIT;

SELECT '✓ 生鲜数据分析菜单已添加' AS status;
SELECT '✓ 包含6个分析菜单（数据概览、用户行为、消费分析、用户画像、社交分析、内容偏好）' AS info;
SELECT '✓ 权限已分配给admin角色' AS status;
SELECT '请重新登录系统以刷新权限' AS next_step;


