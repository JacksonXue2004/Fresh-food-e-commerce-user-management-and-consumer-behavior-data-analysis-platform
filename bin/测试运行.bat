@echo off
echo.
echo [信息] 测试运行现有的 JAR 文件
echo.

cd %~dp0
cd ../freshdata-admin/target

if not exist freshdata-admin.jar (
    echo [错误] 找不到 freshdata-admin.jar 文件
    echo 请先使用 Maven 编译项目: mvn clean package -DskipTests
    pause
    exit /b 1
)

echo [信息] 找到 JAR 文件，正在启动...
echo.

set JAVA_OPTS=-Xms256m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m

java %JAVA_OPTS% -jar freshdata-admin.jar

cd %~dp0
pause


