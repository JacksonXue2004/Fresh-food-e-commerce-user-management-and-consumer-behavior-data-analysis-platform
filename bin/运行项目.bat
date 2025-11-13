@echo off
echo.
echo [��Ϣ] ʹ��Jar��������Web���̡�
echo.

cd %~dp0
cd ../freshdata-admin/target

set JAVA_OPTS=-Xms256m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m

java %JAVA_OPTS% -jar freshdata-admin.jar

cd %~dp0
pause