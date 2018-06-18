
#按小时统计PV数量
#按天统计PV数量
#按终端统计PV数量
#按流量来源统计PV数量
#按流量来源统计PV的topn

#统计每日新访客
思路：创建一个去重的访客累计表，然后将每日访客与历史访客进行对比（用left semi join）

#关键路径转化率分析
思路：定义业务流程中各个环境的url标示，统计每个url下的pv数



#按天统计各个小时的pv数量
select
    a.month,
    a.day,
    a.hour,
    count(1) as pvs
from ods_weblog_detail a
where a.daystr = '2013-09-18'
group by a.month, a.day, a.hour
order by cast(a.hour as int);


#统计每天的PV总量
select sum(pvs), month, day from dw_pvs_hour group by month, day;


#统计各个终端的PV数量
insert into table display_pv_terminal partition(datestr='2013-09-18')
select
     'Mozilla' as terminal,
     count(1),
     a.month,
     a.day,
     a.hour
 from ods_weblog_detail a
 where a.http_user_agent like '%Mozilla%'
 group by a.month, a.day, a.hour

 union all

 select
     'Sogou' as terminal,
     count(1),
     a.month,
     a.day,
     a.hour
 from ods_weblog_detail a
 where a.http_user_agent like '%Sogouwebspider%'
 group by a.month, a.day, a.hour;


#统计每个流量来源的pv（按小时）
insert into table display_pv_channel partition(datestr='2013-09-18')
select
    a.ref_host,
    a.month,
    a.day,
    a.hour,
    count(1) as referer_pv_cnt
from ods_weblog_detail a
group by a.ref_host, a.month, a.day, a.hour;


#降序排序各个渠道的pv数
select
    referer_host,
    count(1) as ref_pv_cnts
from display_pv_channel
group by referer_host
having referer_host is not null
order by ref_pv_cnts desc;


#将每日新用户加入到历史表

insert into table dw_user_dsct_history partition(datestr='2013-09-18')
select temp.day as day, temp.today_addr as new_ip from
(
select
    today.day as day ,
    today.remote_addr as today_addr,
    old.ip as old_addr
from
    (
        select
            distinct remote_addr as remote_addr,
            '2013-09-18' as day
        from ods_weblog_detail
        where datestr = '2013-09-18'
    )
today
left outer join
     dw_user_dsct_history old
on today.remote_addr = old.ip
) temp
#过滤掉已经存储在的访客
where temp.old_addr is null;


#转化率统计
step1：/items
step2: /category
step3: /order
step4: /index

create table route_nums as
select 'step1' as step, count(distinct remote_addr) as numbers from ods_click_pageviews where request like '/items%'
select 'step2' as step, count(distinct remote_addr) as numbers from ods_click_pageviews where request like '/category%'
select 'step3' as step, count(distinct remote_addr) as numbers from ods_click_pageviews where request like '/order%'
select 'step4' as step, count(distinct remote_addr) as numbers from ods_click_pageviews where request like '/index%'


