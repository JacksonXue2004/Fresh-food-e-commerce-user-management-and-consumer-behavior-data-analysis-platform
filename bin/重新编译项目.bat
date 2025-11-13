@echo off
chcp 65001 >nul
echo.
echo [信息] 正在清理并重新编译项目...
echo.

cd /d %~dp0
cd ..

echo [信息] 当前目录: %CD%
echo.

echo [信息] 开始执行 Maven 编译...
call mvn clean package -DskipTests

if %ERRORLEVEL% EQU 0 (
    echo.
    echo [成功] 项目编译完成！
    echo [信息] JAR 文件位置: freshdata-admin\target\freshdata-admin.jar
    echo.
    echo 现在可以运行项目了：
    echo   1. 使用脚本运行: bin\运行项目.bat
    echo   2. 或直接运行: java -jar freshdata-admin\target\freshdata-admin.jar
) else (
    echo.
    echo [错误] 编译失败，请检查错误信息
)

echo.
pause


