#!/bin/bash

PORT="80"
CONTEXT="s2jh"
JETTY="prototype-jetty.jar"
WAR="prototype-simplified.war"
JAVA_OPT="-Xms256m -Xmx512m -XX:MaxPermSize=128m -Dport=$PORT -Dcontext=$CONTEXT"

echo "---------------------------------------------------------------"
echo "Preparing work files..."
echo "---------------------------------------------------------------"

if [ -d "./h2" ]
then
JAVA_OPT="$JAVA_OPT -Djdbc.initialize.database.enable=false"
else
JAVA_OPT="$JAVA_OPT -Djdbc.initialize.database.enable=true"
fi

if [ -d "./work" ]
then
echo "work dir exist, do nothing"
else
mkdir -p  work/webapp/WEB-INF/lib
cp lib/* work/webapp/WEB-INF/lib
fi

cp $WAR  work/webapp
cd  work/webapp/
jar xvf $WAR
cd ../..
rm work/webapp/$WAR

echo "---------------------------------------------------------------"
echo "[INFO] JAVA_OPT=$JAVA_OPT"
echo "[INFO] Please wait a moment for startup finish, when you see:"
echo "[INFO]   ...Started SelectChannelConnector@0.0.0.0:$PORT..."
echo "[INFO] then use Firefox to visit the following URL:"
echo "[INFO] http://localhost:$PORT/$CONTEXT"
echo "---------------------------------------------------------------"

java $JAVA_OPT -jar $WAR
