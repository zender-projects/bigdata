#事实表

#weblog贴源表
drop table if exists ods_weblog_origin;
create table ods_weblog_origin(
    valid string,
    remote_addr string,
    remote_user string,
    time_local string,
    request string,
    status string,
    body_bytes_sent string,
    http_referer string,
    http_user_agent string
)
partitioned by (datestr string)
row format delimited
fields terminated by '\001'
lines terminated by '\n'
stored as textfile;

#pageview-PV表
drop table if exists ods_click_pageviews;
create table ods_click_pageviews(
    session string,
    remote_addr string,
    remote_user string,
    time_local string,
    request string,
    visit_step string,
    page_staylong string,
    http_referer string,
    http_user_agent string,
    body_bytes_sent string,
    status string
)
partitioned by (datestr string)
row format delimited
fields terminated by '\001'
lines terminated by '\n'
stored as textfile;


#userview-UV表
drop table if exists ods_click_visit;
create table ods_click_visit(
    session string,
    remote_addr string,
    in_time string,
    out_time string,
    in_page string,
    out_page string,
    referal string,
    page_visists int
)
partitioned by (datestr string)
row format delimited
fields terminated by '\001'
lines terminated by '\n'
stored as textfile;


#etl明细宽表
drop table if exists ods_weblog_detail;
create table ods_weblog_detail(
    valid string,
    remote_addr string,
    remote_user string,
    time_local string,
    daystr string,
    timestr string,
    month string,
    day string,
    hour string,
    request string,
    status string,
    body_bytes_sent string,
    http_referer string,
    ref_host string,
    ref_path string,
    ref_query string,
    ref_query_id string,
    http_user_agent string
)
partitioned by (datestr string)
row format delimited
fields terminated by '\001'
lines terminated by '\n'
stored as textfile;



#每小时pv统计表
drop table if exists dw_pvs_hour;
create table dw_pvs_hour(
    month string,
    day string,
    hour string,
    pvs bigint
)
partitioned by(datestr string)
row format delimited
fields terminated by '\001'
lines terminated by '\n'
stored as textfile;

#每日用户平均pv
drop table if exists dw_avgpv_user_day;
create table dw_avgpv_user_day(
    day string,
    avgpv string
);


#来源维度pv统计(小时粒度)
drop table if exists dw_pvs_referer_hour;
create table dw_pvs_referer_hour(
    referer_url string,
    referer_host string,
    month string,
    day string,
    hour string,
    pv_referer_cnt bigint
)
partitioned by(datestr string)
row format delimited
fields terminated by '\001'
lines terminated by '\n'
stored as textfile;

#每小时来源pv的topn
drop table if exists dw_pvs_refhost_topn_hour;
create table dw_pvs_refhost_topn_hour(
    hour string,
    toporder string,
    ref_host string,
    ref_host_cnt string
)
partitioned by(datestr string)
row format delimited
fields terminated by '\001'
lines terminated by '\n'
stored as textfile;



#不同终端的pv统计
drop table if exists display_pv_terminal;
create table display_pv_terminal(
    terminal_type string,
    pvs bigint,
    month string,
    day string,
    hour string
)
partitioned by (datestr string)
row format delimited
fields terminated by '\001'
lines terminated by '\n'
stored as textfile;

#不通渠道pv统计
drop table if exists display_pv_channel;
create table display_pv_channel(
    referer_host string,
    month string,
    day string,
    hour string,
    referer_pv_cnt bigint
)
partitioned by(datestr string)
row format delimited
fields terminated by '\001'
lines terminated by '\n'
stored as textfile;



#用户访问历史表
drop table if exists dw_user_dsct_history;
create table dw_user_dsct_history(
    day string,
    ip string
)
partitioned by (datestr string)
row format delimited
fields terminated by '\001'
lines terminated by '\n'
stored as textfile;

