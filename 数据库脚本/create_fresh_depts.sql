-- =============================================
-- 生鲜电商用户分类部门创建脚本
-- =============================================
-- 说明：此脚本用于创建生鲜电商业务的用户分类部门
-- 执行顺序：必须在导入用户数据之前执行
-- =============================================

USE ry;

-- 先删除可能存在的旧数据（避免重复执行时出错）
DELETE FROM sys_dept WHERE dept_id IN (100, 101, 102, 200, 201, 202, 203, 300, 400);

-- 插入生鲜电商用户分类部门
-- 个人用户（父类）
INSERT INTO sys_dept (dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, del_flag, create_by, create_time)
VALUES (100, 103, '0,103', '个人用户', 1, '客服', '15888888888', 'customer@fresh.com', '0', '0', 'admin', NOW());

-- 个人用户 - 普通会员
INSERT INTO sys_dept (dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, del_flag, create_by, create_time)
VALUES (101, 100, '0,103,100', '普通会员', 1, '客服', '15888888888', 'member@fresh.com', '0', '0', 'admin', NOW());

-- 个人用户 - VIP会员
INSERT INTO sys_dept (dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, del_flag, create_by, create_time)
VALUES (102, 100, '0,103,100', 'VIP会员', 2, '客服', '15888888888', 'vip@fresh.com', '0', '0', 'admin', NOW());

-- 企业用户（父类）
INSERT INTO sys_dept (dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, del_flag, create_by, create_time)
VALUES (200, 103, '0,103', '企业用户', 2, '企业客服', '15888888889', 'business@fresh.com', '0', '0', 'admin', NOW());

-- 企业用户 - 餐饮企业
INSERT INTO sys_dept (dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, del_flag, create_by, create_time)
VALUES (201, 200, '0,103,200', '餐饮企业', 1, '餐饮经理', '15888888889', 'restaurant@fresh.com', '0', '0', 'admin', NOW());

-- 企业用户 - 酒店企业
INSERT INTO sys_dept (dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, del_flag, create_by, create_time)
VALUES (202, 200, '0,103,200', '酒店企业', 2, '酒店经理', '15888888889', 'hotel@fresh.com', '0', '0', 'admin', NOW());

-- 企业用户 - 食堂采购
INSERT INTO sys_dept (dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, del_flag, create_by, create_time)
VALUES (203, 200, '0,103,200', '食堂采购', 3, '食堂经理', '15888888889', 'canteen@fresh.com', '0', '0', 'admin', NOW());

-- 批发商
INSERT INTO sys_dept (dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, del_flag, create_by, create_time)
VALUES (300, 103, '0,103', '批发商', 3, '批发经理', '15888888890', 'wholesale@fresh.com', '0', '0', 'admin', NOW());

-- 零售商
INSERT INTO sys_dept (dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, del_flag, create_by, create_time)
VALUES (400, 103, '0,103', '零售商', 4, '零售经理', '15888888891', 'retail@fresh.com', '0', '0', 'admin', NOW());

COMMIT;

-- 验证插入结果
SELECT dept_id, parent_id, dept_name, leader FROM sys_dept WHERE dept_id IN (100, 101, 102, 200, 201, 202, 203, 300, 400) ORDER BY dept_id;

-- =============================================
-- 执行完成提示
-- =============================================
SELECT '生鲜电商用户分类部门创建完成！' AS message;
SELECT '接下来可以执行用户数据导入了。' AS next_step;














