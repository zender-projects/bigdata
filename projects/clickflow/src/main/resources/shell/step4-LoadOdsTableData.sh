#!/bin/bash

#============================================================
#功能描述：加载数据到hive表
#============================================================

#设置java环境
export JAVA_HOME=/usr/local/java
export JRE_HOME=${JAVA_HOME}/jre
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib
export PATH=${JAVA_HOME}/bin:${JRE_HOME}/bin:$PATH

#设置hadoop环境
export HADOOP_HOME=/usr/local/hadoop
export PATH=${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin:$PATH

pre_day=2013-09-18

log_pre_output=/data/weblog/preprocess/output
click_pv_output=/data/weblog/preprocess/click_pv_out
click_uv_output=/data/weblog/preprocess/click_visit_out

ods_weblog_origin="bigdata_clickflow.ods_weblog_origin"
ods_click_pageviews="bigdata_clickflow.ods_click_pageviews"
ods_click_visit="bigdata_clickflow.ods_click_visit"

#原始数据
HQL_ORIGIN="load data inpath '$log_pre_output/$pre_day' overwirte into table $ods_weblog_origin partition(datestr=$pre_day)"
echo $HQL_ORIGIN
/usr/local/hive/bin/hive -e "$HQL_ORIGIN"


#PV数据
HQL_PV="load data inpath '$click_pv_output/$pre_day' overwrite into table $ods_click_pageviews partition(datestr=$pre_day)"
echo $HQL_PV
/usr/local/hive/bin/hive -e "$HQL_PV"


#UV数据
HQL_UV="load data inpath '$click_uv_output/$pre_day' overwrite into table $ods_click_visit partition(datestr=$pre_day)"
echo $HQL_UV
/usr/local/hive/bin/hive -e "$HQL_UV"