#!/bin/bash

PORT="80"
if [ "$1" != "" ]
then
PORT=$1
fi

CONTEXT="s2jh"
WAR="prototype-standalone.war"
JAVA_OPT="-Xms256m -Xmx512m -XX:MaxPermSize=128m -Dport=$PORT -Dcontext=$CONTEXT"

if [ -d "./h2" ]
then
JAVA_OPT="$JAVA_OPT -Djdbc.initialize.database.enable=false"
else
JAVA_OPT="$JAVA_OPT -Djdbc.initialize.database.enable=true"
fi

echo "---------------------------------------------------------------"
echo "[INFO] JAVA_OPT=$JAVA_OPT"
echo "[INFO] Please wait a moment for startup finish, when you see:"
echo "[INFO]   ...Started SelectChannelConnector@0.0.0.0:$PORT..."
echo "[INFO] then use Firefox to visit the following URL:"
echo "[INFO] http://localhost:$PORT/$CONTEXT"
echo "---------------------------------------------------------------"

sudo java $JAVA_OPT -jar $WAR
