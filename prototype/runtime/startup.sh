#!/bin/bash

PORT="80"
if [ "$1"!="" ]
then
PORT=$1
fi

CONTEXT="s2jh"
JETTY="prototype-jetty.jar"
WAR="prototype-simplified.war"
JAVA_OPT="-Xms256m -Xmx512m -XX:MaxPermSize=128m -Dport=$PORT -Dcontext=$CONTEXT"

MERGE_WAR="prototype.war"

echo "Preparing work files..."

if [ -d "./h2" ]
then
JAVA_OPT="$JAVA_OPT -Djdbc.initialize.database.enable=false"
else
JAVA_OPT="$JAVA_OPT -Djdbc.initialize.database.enable=true"
fi

T1=`date +%s -r $WAR`
T2=0
if [ -f "$MERGE_WAR" ]; then
T2=`date +%s -r $MERGE_WAR`
fi

if [ $T1 -gt $T2 ]
then
echo "Updating war..."
mkdir -p temp/WEB-INF/lib
cp lib/* temp/WEB-INF/lib
cp $JETTY temp
cp $WAR temp
cd temp
jar xf $JETTY
rm $JETTY
rm -f META-INF/MANIFEST.MF
jar uf $WAR *
mv $WAR ../$MERGE_WAR
cd ..
sudo rm -fr temp
sudo rm -fr work
fi

echo "---------------------------------------------------------------"
echo "[INFO] JAVA_OPT=$JAVA_OPT"
echo "[INFO] Please wait a moment for startup finish, when you see:"
echo "[INFO]   ...Started SelectChannelConnector@0.0.0.0:$PORT..."
echo "[INFO] then use Firefox to visit the following URL:"
echo "[INFO] http://localhost:$PORT/$CONTEXT"
echo "---------------------------------------------------------------"

sudo java $JAVA_OPT -jar $MERGE_WAR
