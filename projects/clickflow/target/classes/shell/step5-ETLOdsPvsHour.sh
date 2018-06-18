#!/bin/bash
#===========================================================
#按小时统计pv数
#数据来源：ods_weblog_detail
#目标表：dw_pvs_hour
#===========================================================

exec_hive=/usr/local/hive/bin/hive
if [ $# -eq 1 ];then
    day=${1}
else
    day=`date -d'-1 day' +%Y-%M-%d`
fi

origin_table="bigdata_clickflow.ods_weblog_detail"
target_table="bigdata_clickflow.dw_pvs_hour"

HQL="insert into table $target_table partition(datestr=$day)
    select
        a.month,
        a.day,
        a.hour,
        count(1) as pvs
    from ods_weblog_detail a
    where a.daystr = '2013-09-18'
    group by a.month, a.day, a.hour
    order by cast(a.hour as int);"

$exec_hive -e $HQL