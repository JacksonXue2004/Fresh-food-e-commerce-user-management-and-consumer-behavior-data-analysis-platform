-- =============================================
-- 生鲜电商完整数据导入脚本
-- =============================================
-- 包含：清理旧数据 + 创建部门 + 导入用户
-- 执行时间：约3-5秒
-- =============================================

SET NAMES utf8mb4;
SET CHARACTER_SET_CLIENT = utf8mb4;
SET CHARACTER_SET_CONNECTION = utf8mb4;
SET CHARACTER_SET_RESULTS = utf8mb4;

USE ry;

-- ====== 第1步：清理旧数据 ======
-- 删除测试用户（保留admin和ry用户）
DELETE FROM sys_user WHERE user_id > 2;

-- 删除旧的用户分类部门
DELETE FROM sys_dept WHERE dept_id IN (100, 101, 102, 200, 201, 202, 203, 300, 400);

-- 重置用户表自增ID
ALTER TABLE sys_user AUTO_INCREMENT = 3;

SELECT '✓ 步骤1完成：旧数据已清理' AS status;

-- ====== 第2步：创建用户分类部门 ======
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

SELECT '✓ 步骤2完成：用户分类部门已创建' AS status;

COMMIT;

-- ====== 第3步：导入用户数据 ======
-- 请在第3步单独执行 fresh_users.sql
-- 或者继续在此脚本后面追加用户数据

SELECT '✓ 前置步骤完成！' AS status;
SELECT '接下来请执行 fresh_users.sql 导入用户数据' AS next_step;
SELECT '或者使用命令: mysql -u root -p ry < fresh_users.sql' AS command;

