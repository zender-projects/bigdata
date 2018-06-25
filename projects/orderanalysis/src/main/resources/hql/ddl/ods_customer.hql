CREATE TABLE `ods_customer` (
  `CUST_ID` int COMMENT '客户ID',
  `MOBILE` varchar(15)  COMMENT '手机号',
  `NICKNAME` varchar(30)  COMMENT '客户昵称',
  `STATUS` int  COMMENT '客户状态：关注、激活、注册、下单',
  `CUST_SOURCE` int COMMENT '用户来源：微信、微博、IOS、Android',
  `QQ` int comment 'qq号码',
  `WEIBO` varchar(50)  COMMENT '微博账号',
  `EMAIL` varchar(50) COMMENT '邮箱',
  `HEAD_ICO` varchar(255)  COMMENT '头像',
  `POINTS` decimal(10,2)  COMMENT '积分',
  `REGISTER_TIME` string  COMMENT '注册时间',
  `FIRST_ORDER_TIME` string COMMENT '第1次成功订单（含虚拟订单）的下单时间',
  `LAST_ORDER_TIME` string COMMENT '最后1次的成功订单的下单时间',
  `LAST_CANCEL_TIME` string COMMENT '最后取消订单时间',
  `FIRST_BUY_TIME` string COMMENT '首次成功购物时间（非虚拟订单）',
  `LEVEL` int  COMMENT '客户级别',
  `SEX` char(3)  COMMENT '性别：男；女；NULL',
  `BIRTHDAY` varchar(20)  COMMENT '出生日期，可以是一个很准确、具体的日期，如1990-08-08；也可以是一个比较模糊但具象的',
  `AGE_DESC` varchar(30)  COMMENT '年龄描述，可以是一个具体的年龄，也可以是一个模糊但具像的，比如：\n            35岁，35岁左右',
  `CUST_NAME` varchar(15)  COMMENT '姓名',
  `CITY_ID` int comment '城市id',
  `ORDER_CNT` int  COMMENT '累计订单数',
  `AMOUNT_SUM` decimal(20,2)  COMMENT '累计金额',
  `PROMOTIONS_SUM` decimal(20,2)  COMMENT '累计补贴金额',
  `CANCEL_CNT` int COMMENT '累计取消订单数',
  `SOURCE_NO` int COMMENT '来源渠道号',
  `LAST_UPDATE_TIME` date COMMENT '最后更新时间')
  COMMENT '用户表'
partitioned by (dt string)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\t'
STORED AS TEXTFILE;


alter table ods_customer add partition (dt='20151210');
load data local inpath '/home/anjianbing/soft/data/t_customer.txt' overwrite into table
ods_customer partition(dt='20151210');
load data local inpath '/home/anjianbing/soft/data/t_customer_no.txt' into table
ods_customer partition(dt='20151210');