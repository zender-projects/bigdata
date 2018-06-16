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



#pageview输入目录
/data/weblog/preprocess/valid_output
#pageview 输出目录
/data/weblog/preprocess/click_pv_out