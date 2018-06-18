#!/bin/bash

#============================================================
#功能描述：计算pv/uv
#输入路径：pv：/data/weblog/preprocess/valid_output
#         uv：/data/weblog/preprocess/click_pv_out
#输出路径：pv：/data/weblog/preprocess/click_pv_out
#         uv：/data/weblog/preprocess/click_visit_out
#============================================================

#设置java环境
export JAVA_HOME=/usr/local/java
export JRE_HOME=${JAVA_HOME}/jre
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib
export PATH=${JAVA_HOME}/bin:${JRE_HOME}/bin:$PATH

#设置hadoop环境
export HADOOP_HOME=/usr/local/hadoop
export PATH=${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin:$PATH

#pv输入/输出目录
pv_in_dir=/data/weblog/preprocess/valid_output
pv_out_dir=/data/weblog/preprocess/click_pv_out

#uv输入/输出目录
uv_in_dir=/data/weblog/preprocess/click_pv_out
uv_out_dir=/data/weblog/preprocess/click_visit_out

pv_class=bigdata.clickflow.mrjobs.ClickFlowPvJob
uv_class=bigdata.clickflow.mrjobs.ClickFlowUvJob

jobpath=/home/hadoop/mrjobs
jarname=project-clickflow-1.0-SNAPSHOT.jar

#获取前一天时间
#pre_day=`date -d'-1 day' +%Y-%m-%d`
pre_day=2013-09-18
echo "begin to process pv..."
files=`hadoop fs -ls $pv_in_dir | grep $pre_day | wc -l`
if [ $files -gt 0 ];then
    echo "running... hadoop jar $jobpath/$jarname $pv_class $pv_in_dir/$pre_day $pv_out_dir/$pre_day"
    hadoop jar $jobpath/$jarname $pv_in_dir/$pre_day $pv_out_dir/$pre_day
fi

echo "pv process result: $?"
echo "begin to process uv..."
if [ $? -eq 0 ];then
    echo "running... hadoop jar $jobpath/$jarname $uv_class $uv_in_dir/$pre_day $uv_out_dir/$pre_day"
    hadoop jar $jobpath/$jarname  $uv_in_dir/$pre_day $uv_out_dir/$pre_day"
fi
echo "uv process result: $?"
echo "pv & uv process complete!"

