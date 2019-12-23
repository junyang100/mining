#!/bin/bash

PROFILE=''
MAIN_CLASS=com.mine.ConfigCenterAdminApplication
#脚本所在目录
cd `dirname $0`
#BIN目路
BIN_DIR=`pwd`
cd ..
#部署目路
DEPLOY_DIR=`pwd`
#获取到当前目录的名称
SERVER_NAME=`basename $DEPLOY_DIR`
#配置文件目录
CONF_DIR=$DEPLOY_DIR/conf
#项目依赖库
LIB_DIR=$DEPLOY_DIR/lib
#应用进程
PIDS=`ps -ef | grep java | grep "$CONF_DIR" |awk '{print $2}'`
#设置日志文件的输出目录
LOGS_DIR=$DEPLOY_DIR/logs                                 
if [ ! -d $LOGS_DIR ]; then                               
    mkdir $LOGS_DIR
fi

#日志
STDOUT_FILE=$LOGS_DIR/stdout.log
#JAVA 环境配置
JAVA_OPTS=" -Djava.net.preferIPv4Stack=true -Dlog.home=$LOGS_DIR -Djava.security.auth.login.config=$CONF_DIR/kafka_client_jaas.conf"
JAVA_MEM_OPTS=" -server -Xms$3 -Xmx$3 -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=256m  -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:InitiatingHeapOccupancyPercent=50 -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCDateStamps -verbose:gc -XX:+PrintGCDetails -Xloggc:$LOGS_DIR/gc.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=dump.hprof "

#退出标志
RETVAL="0"

function start() {
	if [ -n "$PIDS" ]; then                                                         
		echo "ERROR: The $SERVER_NAME already started!"
		echo "PID: $PIDS"
		exit $RETVAL
	fi
	
	echo -e "Starting the $SERVER_NAME with JAVA_HOME [$JAVA_HOME] ...\c"

	#启动tomcat，如果指定的JAVA_HOME不存在，则使用环境变量中的JAVA_HOME
	if [ ! -d $JAVA_HOME ]; then
        nohup java $JAVA_OPTS $JAVA_MEM_OPTS -cp $CONF_DIR:$LIB_DIR/* $MAIN_CLASS $PROFILE > $STDOUT_FILE 2>&1 &
    else
        nohup $JAVA_HOME/bin/java $JAVA_OPTS $JAVA_MEM_OPTS -cp $CONF_DIR:$LIB_DIR/* $MAIN_CLASS $PROFILE > $STDOUT_FILE 2>&1 &
    fi
	
	COUNT=0
	while [ $COUNT -lt 1 ]; do                 
		echo -e ".\c"                         
		sleep 1                            
		COUNT=`ps -f | grep java | grep "$DEPLOY_DIR" | awk '{print $2}' | wc -l`
		if [ $COUNT -gt 0 ]; then
			break
		fi
	done
  
	echo "OK!"                                      
	PIDS=`ps -f | grep java | grep "$DEPLOY_DIR" | awk '{print $2}'`
	echo "PID: $PIDS"
	echo "STDOUT: $STDOUT_FILE"
	
}

function stop() {
	if [ -z "$PIDS" ]; then
    		echo "ERROR: The $SERVER_NAME does not started!"
    	exit 1
	fi

	echo -e "Stopping the $SERVER_NAME ...\c"
	for PID in $PIDS ; do
    		kill $PID > /dev/null 2>&1
	done

	COUNT=0
	while [ $COUNT -lt 1 ]; do
    		echo -e ".\c"
    		sleep 1
    		COUNT=1
    		for PID in $PIDS ; do
        	PID_EXIST=`ps -f -p $PID | grep java`
        	if [ -n "$PID_EXIST" ]; then
            		COUNT=0
            		break
        	fi
    		done
	done

	echo "OK!"
	echo "PID: $PIDS"
	PIDS=""
}


function usage() {
	echo "Usage: $0 {start|stop|restart|status}"
	RETVAL="2"
}

#退出标志
RETVAL="0"

if [ ! -n "$2" ] ;then
	PROFILE=''
else
	PROFILE='--spring.profiles.active='$2
fi

case $1 in
        start)
                start
                ;;
        stop)
                stop
                ;;
        restart)
                stop
                start
                ;;
        *)
                ;;
esac
exit $RETVAL
