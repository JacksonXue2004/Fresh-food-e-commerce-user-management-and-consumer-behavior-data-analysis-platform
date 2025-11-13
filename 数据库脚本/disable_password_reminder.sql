-- 关闭初始密码提醒功能
-- Disable initial password reminder

USE ry;

-- 查看当前配置
SELECT config_id, config_name, config_key, config_value 
FROM sys_config 
WHERE config_key = 'sys.account.initPasswordModify';

-- 关闭初始密码提醒（改为0）
UPDATE sys_config 
SET config_value = '0' 
WHERE config_key = 'sys.account.initPasswordModify';

-- 验证修改结果
SELECT config_id, config_name, config_key, config_value 
FROM sys_config 
WHERE config_key = 'sys.account.initPasswordModify';

-- 提交事务
COMMIT;















