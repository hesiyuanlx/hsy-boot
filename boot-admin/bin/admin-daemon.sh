#!/bin/bash

USG="Usage: $0  start|stop [-conf confDir] [-logs logDir] [-superd]"
if [ $# -lt 1 ] ; then
  exit 2
fi

#source /etc/profile

#The home directory for rcd process.
mypwd=$(cd `dirname "${BASH_SOURCE[0]}"` && pwd)/`basename "${BASH_SOURCE[0]}"`

mypwd=`dirname $mypwd`

RADAR_HOME=$mypwd/..

# The java implementation to use. Required
#export JAVA_HOME=

#The maximum amount of heap to use, in MB. Default is 4096.
APP_HEAP_SIZE=2048

# Where log files are stored.  $RADAR_LOG_DIR/logs by default.
ARR=($@)
NUMBER=$#
for ((i=1 ; i <= NUMBER ; i++))
do
  if [ "${ARR[$i]}" = "-conf" ] ; then
    CONFIG_DIR=${ARR[$((i+1))]}
  elif [ "${ARR[$i]}" = "-logs" ] ; then
    RADAR_LOG_DIR=${ARR[$((i+1))]}
  elif [ "${ARR[$i]}" = "-superd" ] ; then
    SUPERD_FLAG=y
  fi
done

if [ "x$CONFIG_DIR" = "x" ] ; then
  CONFIG_DIR=$RADAR_HOME/conf
fi

if [ "x$RADAR_LOG_DIR" = "x" ] ; then
  RADAR_LOG_DIR=$RADAR_HOME/logs
fi

CLASSPATH=.:$JAVA_HOME/lib/tools.jar:$CONFIG_DIR
LIBPATH=$RADAR_HOME/lib

for f in `find $LIBPATH -name '*.jar'|sort -r`
do
  CLASSPATH=$CLASSPATH:$f
done

# ******************************************************************
# ** Set java main class                                         **
# ** Set JVM heap PARAMS                                         **
# ** Set java runtime options                                    **
# ******************************************************************
CLASS_NAME=cn.hsy.boot.admin.AdminBootStrap
OPT="-Xmx${APP_HEAP_SIZE}m -Xms${APP_HEAP_SIZE}m -XX:+UseG1GC -XX:MaxGCPauseMillis=150 -XX:+UnlockDiagnosticVMOptions -XX:+AlwaysPreTouch  -verbose:gc -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Djava.util.concurrent.ForkJoinPool.common.parallelism=16 -Xloggc:${RADAR_LOG_DIR}/boot-admin.gc -cp $CLASSPATH"

# ******************************************************************
# ** Set java runtime options                                      **
# ** start worker and mkdir logs dir .  **
# ******************************************************************

if [ ! -d $RADAR_LOG_DIR ] ; then
  mkdir -p $RADAR_LOG_DIR
fi

# ***************
# ** Run...    **
# ***************

PID=$RADAR_LOG_DIR/boot-admin.pid
ERR_LOG=$RADAR_LOG_DIR/boot-admin.err

function start()
{
   ENV="-Dlog.path=$RADAR_LOG_DIR -Djava.util.Arrays.useLegacyMergeSort=true -Duser.dir=$RADAR_HOME -Dlog-dir=$RADAR_LOG_DIR -Ddubbo.application.logger=slf4j "
   if [ "$SUPERD_FLAG" = "y" ];then
     exec setuidgid work java $ENV $OPT $CLASS_NAME >/dev/null 2>&1
   else
     exec java $ENV $OPT $CLASS_NAME
   fi
   echo $! > $PID
}

function stop()
{
  if [ -f $PID ]; then
    if kill -0 `cat $PID` > /dev/null 2>&1; then
      echo "stop boot admin process ..."
      kill `cat $PID`
    fi
  else
      echo "It doesn't find boot admin pid file:$PID"
  fi
}
if [ "$1" = "start" ] ; then
  start
elif [ "$1" = "stop" ] ; then
  stop
elif [ "$1" = "restart" ]; then
  stop && sleep 3 && start
else
  echo "It doesn't support this command:$0"
fi