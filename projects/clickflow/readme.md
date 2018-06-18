#大数据试验项目-点击流分析

HDFS Path:
/bigdata/project/clickflow/dim/date/dim_date.txt
/bigdata/project/clickflow/origindata/access.log.fensi

#flume数据存储目录
/data/flumedata/

#待处理，未验证的数据目录
/data/weblog/preprocess/input(原始日志文件)

#经过清洗后，未验证的数据目录(valid:true/false)
/data/weblog/preprocess/output

#待处理，已验证的数据目录(valid:true)
/data/weblog/preprocess/valid_output

#宽表输出目录
data/weblog/preprocess/detail_output/

#pageview输入目录
/data/weblog/preprocess/valid_output
#pageview 输出目录
/data/weblog/preprocess/click_pv_out


#userview输入目录
/data/weblog/preprocess/click_pv_out
#userview输出目录
/data/weblog/preprocess/click_visit_out


#删除数据
hadoop fs -mkdir -p /data/flumedata/2013-09-18
hadoop fs -put /home/hadoop/data/clickflow/access.log.fensi /data/flumedata/2013-09-18
hadoop fs -rm -r /data/weblog/preprocess/input/2013-09-18;
hadoop fs -rm -r /data/weblog/preprocess/output/2013-09-18;
hadoop fs -rm -r /data/weblog/preprocess/valid_output/2013-09-18;
hadoop fs -rm -r /data/weblog/preprocess/detail_output/2013-09-18;
hadoop fs -rm -r /data/weblog/preprocess/click_pv_out/2013-09-18;
hadoop fs -rm -r /data/weblog/preprocess/click_visit_out/2013-09-18;

truncate table dw_avgpv_user_day;
truncate table dw_pvs_hour;
truncate table dw_pvs_referer_hour;
truncate table dw_pvs_refhost_topn_hour;
truncate table ods_click_pageviews;
truncate table ods_click_visit;
truncate table ods_weblog_detail;
truncate table ods_weblog_origin;

