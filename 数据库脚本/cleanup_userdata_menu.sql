-- =============================================
-- 彻底删除"用户数据"菜单
-- =============================================
-- 此脚本会删除生鲜数据菜单下的"用户数据"子菜单
-- =============================================

USE ry;

-- 删除"用户数据"菜单项（如果存在）
DELETE FROM sys_menu WHERE menu_id = 2007;
DELETE FROM sys_menu WHERE menu_name = '用户数据' AND parent_id = 2000;

-- 删除相关的角色权限关联
DELETE FROM sys_role_menu WHERE menu_id = 2007;

-- 确认删除结果
SELECT CONCAT('当前生鲜数据菜单数量: ', COUNT(*)) AS result 
FROM sys_menu 
WHERE parent_id = 2000;

SELECT menu_id, menu_name, order_num 
FROM sys_menu 
WHERE parent_id = 2000 
ORDER BY order_num;

COMMIT;

-- =============================================
-- 执行结果提示
-- =============================================
SELECT '✓ "用户数据"菜单已彻底删除' AS status;
SELECT '✓ 请重新登录系统以刷新权限缓存' AS next_step;












