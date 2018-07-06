#!/bin/bash

#set java environment
export JAVA_HOME=/usr/local/java
export JRE_HOME=$JAVA_HOME/jre
export CLASSPATH=.:$JAVA_HOME/lib:$JRE_HOME/lib
export PATH=$HADOOP_HOME/bin:$PATH

#set hadoop environment
export HADOOP_HOME=/usr/local/hadoop
export PATH=$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$PATH


#日志存放目录
log_src_dir=/home/hadoop/data/logs

#日志文件上传到hdfs到的路径
hdfs_root_dir=/data/clickLog/20180607/

#待上传目录
log_toupload_dir=/home/hadoop/log/toupload/

#打印环境变量信息
echo "envs: hadoop_home: $HADOOP_HOME"

#读取日志文件所在目录，判断是否有需要上传的文件
echo "log_src_dir:"$log_src_dir
ls $log_src_dir | while read fileName
do
    if [["$fileName" == access.log.*]];then
        date = `date +%Y%m%d%H%M%S`
        #将文件移动到待上传路径，并重命名
        echo "moving $log_src_dir $fileName to $log_toupload_dir"index_click_log_$fileName$date
        mv $log_src_dir$fileName $log_toupload_dir"index_click_log_$fileName"$date
        #将待上传的文件的path写入一个列表文件
        echo $log_toupload_dir"index_click_log_$fileName"$date >> $log_toupload_dir"willDoing."$date
    fi
done


#找到列表文件willDoing
ls $log_toupload_dir | grep will | grep -v  "_COPY_" | grep -v "_DONE_" | while read line
do
    echo "toupload is in file:"$line
    #将待上传文件列表改名为willDoing_COPY_
    mv $log_toupload_dir$line $log_toupload_dir$line"_COPY_"
    #读取willDoing_COPY_中的内容（待上传的文件）
    cat $log_toupload_dir$line"_COPY_" | while read line
    do
        echo "putting... $line to hdfs path .... $hdfs_root_dir"
        hadoop fs -put $line $hdfs_root_dir
    done
    #修改willDone_COPY_ 为 willDone_DONE_
    mv $log_toupload_dir$line"_COPY_" $log_toupload_dir$line"_DONE_"
done






