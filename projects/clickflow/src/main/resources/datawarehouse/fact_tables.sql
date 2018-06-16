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