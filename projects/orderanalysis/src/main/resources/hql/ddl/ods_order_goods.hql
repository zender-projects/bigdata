CREATE TABLE `ods_order_goods` (
  `ORDER_DATE` string  COMMENT '下单日期',
  `order_id` int  COMMENT '订单ID',
  `goods_id` int  COMMENT '商品ID',
  `goods_name` varchar(30)  COMMENT '商品名称',
  `source_id` int COMMENT '商品来源',
  `RETURN_PRICE` decimal(10,2)  COMMENT '给门店实际返款价',
  `sell_price` decimal(10,2)  COMMENT '实际售价',
  `sell_num` decimal(10,2)  COMMENT '销售数量',
  `give_num` decimal(10,2)  COMMENT '赠送数量',
  `sell_val` decimal(10,4)  COMMENT '销售额＝实际售价＊销售数量－优惠金额',
  `promotions` decimal(10,4)  COMMENT '优惠券使用金额',
  `PROFITS` decimal(10,2)  COMMENT '利润＝销售数量＊（（线上售价＋给门店的铺货价）－（公司成本价＋给门店的返款价））',
  `DEALER_PROFITS` decimal(10,2)  COMMENT '门店毛利',
  `base_goods_id` int  COMMENT '套餐商品ID：一般为组合商品',
  `base_goods_name` varchar(30)  COMMENT '套餐名称',
  `NORMAL_RETURN_PRICE` decimal(10,2)  COMMENT '正常返款价',
  `DEALER_PRICE` decimal(10,2)  COMMENT '配送门店商品售价',
  `base_sell_price` decimal(10,2)  COMMENT '套餐实际售价',
  `base_sell_num` decimal(10,2)  COMMENT '套餐销量',
  `BASE_GIVE_NUM` decimal(10,2)  COMMENT '套餐赠送量',
  `coupon_id` int  COMMENT '优惠券ID',
  `prom_type` int  COMMENT '优惠类型：-1，价格异常； 0，正常销售； 1，赠券； 2，特价； 3，买赠； 4，打折； 5，抽奖； 6，秒杀；  7，免运费；8，满赠； 9，满减； 10，满返；11，搭配减价； 12，商品组合促销；13，团购；14，场景；',
  `GOODS_DETAIL` string COMMENT '商品详情：JSON格式，对应hs_order_goods.mix_info',
  `SHOW_DEALER_ID` int  COMMENT '商品展示门店ID，',
  `GOODS_STATUS` int COMMENT '商品状态：0，正常；1，被取消商品；2，订单生成后增加到订单中的商品；',
  `STATUS_TIME` string  COMMENT '商品状态改变时间',
  `LAST_UPDATE_TIME` string  COMMENT '最后更新时间',
  `MKG_ID` int COMMENT '活动ID',
  `PRESSIE_HAVE_PRICE` int  COMMENT '赠品给门店是否有铺货价： 0，赠品对门店无铺货价；1，赠品对门店有铺货价；',
  `PRESSIE_BUY_GOODSID` varchar(100)  COMMENT '获得赠品所购买的商品ID，如果有多个，以逗号分隔'
)COMMENT '商品订单表'
 PARTITIONED BY(dt STRING)
 ROW FORMAT DELIMITED
 FIELDS TERMINATED BY '\t'
 STORED AS TEXTFILE;


alter table ods_order_goods add partition (dt='20151010');
alter table ods_order_goods add partition (dt='20151011');
alter table ods_order_goods add partition (dt='20151012');
alter table ods_order_goods add partition (dt='20151013');
load data local inpath '/home/anjianbing/soft/data/t_order_goods20151010.txt' overwrite into table
ods_order_goods partition(dt='20151010');
load data local inpath '/home/anjianbing/soft/data/t_order_goods20151011.txt' overwrite into table
ods_order_goods partition(dt='20151011');
load data local inpath '/home/anjianbing/soft/data/t_order_goods20151012.txt' overwrite into table
ods_order_goods partition(dt='20151012');
load data local inpath '/home/anjianbing/soft/data/t_order_goods20151013.txt' overwrite into table
ods_order_goods partition(dt='20151013');