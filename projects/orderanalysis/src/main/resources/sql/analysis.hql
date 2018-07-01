

#按天查询每个城市的 成功订单数  失败订单数 总订单数  总用户数
select
    city_id,
    sum(case when order_status=5 then 1 else 0 end) as cnt_ord_succ_d,
    sum(case when order_status=3 then 1 else 0 end) as cnt_ord_cacel_d,
    sum(1) as cnt_ord_d,
    count(distinct cust_id) as cnt_ord_user
from ods_order
where dt='20151010'
group by city_id;


#查询7日，15日，30日的商品复购率
1.查询当天完成订单的 订单id，城市id，客户id，商品id :(t1)
2.去掉日期再查一次1 (t2)
3.1和2的结果做inner join，获取订单日期，城市id，顾客id，商品id，时间差

select
    t3.atdate as cdate,
    t3.city_id,
    t3.goods_id,
    count(distinct case when days=0 then t3.cust_id end) as cnt_buy_cust_d,
    count(distinct case when days>0 and days <= 7 then t3.cust_id end) as cnt_buy_cust_7_d,
    count(distinct case when days>0 and days <= 15 then t3.cust_id end) as cnt_buy_cust_15_d,
    count(distinct case when days>0 and days <= 30 then t3.cust_id end) as cnt_buy_cust_30_d
(select
    t1.atdate,
    t1.city_id,
    t1.goods_id,
    datediff(t2.atdate,t1.atdate) as days
from
(select
    o.order_date as atdate,
    o.city_id,
    o.cust_id,
    og.goods_id
from ods_order o
 inner join ods_order_goods og

 on o.order_id = og.order_id
and o.order_status = 5
and og.source_id = 1
and o.dt='20151010') t1 inner join
(select
    o.order_date as atdate,
    o.city_id,
    o.cust_id,
    og.goods_id
from ods_order o
 inner join ods_order_goods og
 on o.order_id = og.order_id
and o.order_status = 5
and og.source_id = 1) t2
on t1.cust_id = t2.cust_id and t1.goods_id=t2.goods_id
) t3


#月平均日客户数
select
  sum(case when o.completion_date >='20150901' and o.completion_date <= '20150930' then 1 else 0 end) as cnt_ord_09_m,
  sum(case when o.completion_date >='20151001' and o.completion_date <= '20151030' then 1 else 0 end) as cnt_ord_10_m,
  count(distinct case when o.completion_date >='20150901' and o.completion_date <= '20150930' then cust_id end) as cnt_cust_09_m,
  count(distinct case when o.completion_date >='20151001' and o.completion_date <= '20151030' then cust_id end) as cnt_cust_10_m
from ods_order o
where o.completion_date >= '20150901'
  and o.completion_date <= '20151031'
  and o.city_id = 2
  and o.order_type <> 6
  and o.payable_amount > 100
  and o.order_status = 5;


#统计每天的新用户信息
select
    count(1)
from ods_customer c
where dt='20151210'
  and from_unixtime(unix_timestamp(register_time, 'yyyy/MM/dd HH:mm'), 'yyyyMMdd')='20140702';


#统计5月6月不同渠道的新用户数
select
    sum(case when from_unixtime(unix_timestamp(c.register_time,'yyyy/MM/dd HH:mm'), yyyyMMdd) >='20150501' and from_unixtime(unix_timestamp(c.register_time,'yyyy/MM/dd HH:mm'), yyyyMMdd) <= '20150531' then 1 else 0 end) as cnt_new_cust_05_m,
    sum(case when from_unixtime(unix_timestamp(c.register_time,'yyyy/MM/dd HH:mm'), yyyyMMdd) >='20150601' and from_unixtime(unix_timestamp(c.register_time,'yyyy/MM/dd HH:mm'), yyyyMMdd) <= '20150630' then 1 else 0 end) as cnt_new_cust_06_m
from ods_customer c
where c.dt='20151210'
group by c.source_no;


select
    sum(case when t.register_time >= '20140501' and t.register_time <= '20150531' then 1 else 0 end) as cnt_05_m,
    sum(case when t.register_time >= '20140601' and t.register_time <= '20150530' then 1 else 0 end) as cnt_06_m,
    sum(case when t.register_time >= '20140701' and t.register_time <= '20150731' then 1 else 0 end) as cnt_07_m,
    sum(case when t.register_time >= '20140801' and t.register_time <= '20150830' then 1 else 0 end) as cnt_08_m,
    sum(case when t.register_time >= '20140830' then 1 else 0 end) as cnt_other_m,
    t.source_no
from (
   select
       from_unixtime(unix_timestamp(c.register_time,'yyyy/MM/dd HH:mm'), 'yyyyMMdd') as register_time,
       c.source_no
   from ods_customer c where dt='20151210') t
group by t.source_no;


#查询每个渠道的订单总数的top10用户

select
 t1.source_no,
 t1.mobile,
 t1.order_cnt
(select
    c.source_no,
    c.mobile,
    c.order_cnt,
    rownum() over(partition by c.source_no, c.cust_id order by c.order_cnt) as rn
from ods_customer c
where c.dt='20151210'
 and c.source_no is not null
 and c.order_cnt is not null) t1
where t1.rn < 11
