#!/bin/bash
#
#========================================================
#功能描述:    将flume收集上来到前一天到数据移动待待处理目录
#原始目录：    /data/flumedata/
#目标目录：    /data/weblog/preprocess/input
#========================================================

#设置java环境
export JAVA_HOME=/usr/local/java
export JRE_HOME=${JAVA_HOME}/jre
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib
export PATH=${JAVA_HOME}/bin:$PATH

#设置hadoop环境
export HADOOP_HOME=/usr/local/hadoop
export PATH=${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin:$PATH

#原始数据目录
log_flume_dir=/data/flumedata
#预处理目录
log_pre_process_dir=/data/weblog/preprocess/input

#获取前一天
#pre_day=`date -d'-1 day' +%Y-$m-%d`
pre_day=2013-09-18

echo "time of data: ${pre_day}"

files=`hadoop fs -ls ${log_flume_dir} | grep ${pre_day} | wc -l`

echo "count of folder : ${files}"

if [ ${files} -gt 0 ]; then
    hadoop fs -mv ${log_flume_dir}/${pre_day}  ${log_pre_process_dir}
    echo "move ${log_flume_dir}/${pre_day} to ${pre_log_process_dir} success"
fi