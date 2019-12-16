#!/bin/bash
baseDirForScriptSelf=$(cd "$(dirname "$0")"; pwd)
JAVA_HOME=/opt/web_app/jdk1.8
sh ${baseDirForScriptSelf}/run.sh start test 2g
