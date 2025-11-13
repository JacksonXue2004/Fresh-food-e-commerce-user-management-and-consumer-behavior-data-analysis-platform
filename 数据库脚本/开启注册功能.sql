-- ========================================
-- 开启用户注册功能
-- ========================================

-- 检查配置是否存在，如果不存在则插入
INSERT INTO sys_config (config_name, config_key, config_value, config_type, create_by, create_time, update_by, update_time, remark)
SELECT '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'true', 'Y', 'admin', sysdate(), 'admin', sysdate(), '是否开启注册用户功能（true开启，false关闭）'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_config WHERE config_key = 'sys.account.registerUser'
);

-- 如果已存在，则更新为开启状态
UPDATE sys_config 
SET config_value = 'true', 
    update_time = sysdate(),
    remark = '是否开启注册用户功能（true开启，false关闭）- 已开启'
WHERE config_key = 'sys.account.registerUser';

-- 验证配置
SELECT config_id, config_name, config_key, config_value, remark 
FROM sys_config 
WHERE config_key = 'sys.account.registerUser';

-- ========================================
-- 执行完成后，重启项目即可看到注册按钮
-- ========================================











