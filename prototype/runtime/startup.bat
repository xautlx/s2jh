@echo off
set PORT=8080
set CONTEXT=s2jh
set JETTY=prototype-jetty.jar
set WAR=prototype-simplified.war
set MERGE_WAR=prototype.war
set JAVA_OPT=-Xms256m -Xmx1024m -XX:MaxPermSize=128m -Dport=%PORT% -Dcontext=%CONTEXT%

echo ---------------------------------------------------------------
echo Preparing work files...
echo ---------------------------------------------------------------

if not exist h2 (
set JAVA_OPT=%JAVA_OPT% -Djdbc.initialize.database.enable=true
) else (
set JAVA_OPT=%JAVA_OPT% -Djdbc.initialize.database.enable=false
)

for %%i in (%WAR%) do (
  SET T1="%%~ti"
)
SET T2=""
if exist %MERGE_WAR% (
  for %%i in (%MERGE_WAR%) do (
    SET T2="%%~ti"
  )
) 

if %T1% GTR %T2% (
  echo "Updating war..."
  mkdir temp\WEB-INF\lib
  copy lib\* temp\WEB-INF\lib
  copy %JETTY% temp
  copy %WAR% temp 
  cd temp
  jar xvf %JETTY%
  del %JETTY%
  del META-INF\MANIFEST.MF
  jar uvf %WAR% *
  move %WAR% ..\%MERGE_WAR%
  cd ..
  rd /s/q temp
  if not exist work (
    rd /s/q work
  )
)

echo ---------------------------------------------------------------
echo [INFO] WAR=%MERGE_WAR%, JAVA_OPT=%JAVA_OPT%
echo [INFO] Please wait a moment for startup finish, when you see:
echo [INFO]   ...Started SelectChannelConnector@0.0.0.0:%PORT%...
echo [INFO] then use Firefox to visit the following URL:
echo [INFO] http://localhost:%PORT%/%CONTEXT%
echo ---------------------------------------------------------------
java %JAVA_OPT% -jar %MERGE_WAR%
