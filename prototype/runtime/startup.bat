@echo off
set PORT=8080
set CONTEXT=s2jh
set JETTY=prototype-jetty.jar
set WAR=prototype-simplified.war
set JAVA_OPT=-Xms256m -Xmx1024m -XX:MaxPermSize=128m -Dport=%PORT% -Dcontext=%CONTEXT%

echo ---------------------------------------------------------------
echo Preparing work files...
echo ---------------------------------------------------------------

if not exist h2 (
set JAVA_OPT=%JAVA_OPT% -Djdbc.initialize.database.enable=true
) else (
set JAVA_OPT=%JAVA_OPT% -Djdbc.initialize.database.enable=false
)

if not exist work (
mkdir work\webapp\WEB-INF\lib
copy lib\* work\webapp\WEB-INF\lib
) 

copy %WAR%  work\webapp
cd  work\webapp\
jar xvf %WAR%
cd ..\..
rm work\webapp\%WAR%

echo ---------------------------------------------------------------
echo [INFO] JAVA_OPT=%JAVA_OPT%
echo [INFO] Please wait a moment for startup finish, when you see:
echo [INFO]   ...Started SelectChannelConnector@0.0.0.0:%PORT%...
echo [INFO] then use Firefox to visit the following URL:
echo [INFO] http://localhost:%PORT%/%CONTEXT%
echo ---------------------------------------------------------------
java %JAVA_OPT% -jar %WAR%
