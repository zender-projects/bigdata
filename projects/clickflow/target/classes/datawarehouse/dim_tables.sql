#维度表

#时间维度
create table if not exists bigdata_clickflow.dim_date (
    year string,
    month string,
    day string,
    hour string
)
row format delimited
fields terminated by '\t'
lines terminated by '\n'
stored as textfile;
