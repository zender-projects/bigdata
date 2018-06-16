#!/bin/bash

#=====================================================
#功能描述：对原始日志数据进行预处理，过滤有效数据
#原始目录：/data/weblog/preprocess/input
#目标目录：/data/weblog/preprocess/output
#         /data/weblog/preprocess/valid_output
#=====================================================

#设置java环境
export JAVA_HOME=/usr/local/java
export JRE_HOME=${JAVA_HOME}/jre
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib
export PATH=${JAVA_HOME}/bin:${JRE_HOME}/bin$PATH

#设置hadoop环境
export HADOOP_HOME=/usr/local/hadoop
export PATH=${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin:$PATH

#输入目录
log_pre_input=/data/weblog/preprocess/input

#输出目录
log_pre_output=/data/weblog/preprocess/output
log_pre_valid_output=/data/weblog/preprocess/valid_output

#hadoop job class name
preprocess_class=bigdata.clickflow.mrjobs.WebLogPreProcessJob
prevalid_class=bigdata.clickflow.mrjobs.WebLogPreValidProcessJob

#获取前一天
#pre_day=`date -d'-1 day' +%Y-%m-%d`
pre_day=2013-09-18

jobpath=/home/hadoop/mrjobs
jarname=project-clickflow-1.0-SNAPSHOT.jar

#开始处理
echo "begin to pre process..."

files=`hadoop fs -ls $log_pre_input | grep $pre_day | wc -l`

echo "directory count: $files"

if [ $files -gt 0 ];then
    echo "running hadoop jar $jobpath/$jarname $preprocess_class $log_pre_input/$pre_day $log_pre_output/$$pre_day"
    hadoop jar $jobpath/$jarname $preprocess_class $log_pre_input/$pre_day $log_pre_output/$$pre_day
fi

echo "step1 process reuslt: $?"
if[ $? -eq 0 ];then
    echo "running hadoop jar $jobpath/$jarname $prevalid_class $log_pre_output/$pre_day $log_pre_valid_output/$pre_day"
    hadoop jar $jobpath/$jarname $prevalid_class $log_pre_output/$pre_day $log_pre_valid_output/$pre_day
fi

echo "step2 process result: $?"
echo "pre process complete!"