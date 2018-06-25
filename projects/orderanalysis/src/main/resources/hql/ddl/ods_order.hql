create table ods.ods_order
(
   ORDER_ID             int comment '订单ID',
   ORDER_NO             varchar(30)  comment '订单编号（唯一字段），前缀字符表示订单来源：a，Andriod；b，微博；c，WEB；e，饿了么；i，Iphone；m，Mobile；x，微信； z，中粮我买网；l，其它。 接着3位数字代表订单城市编号；接着字符z与后面的真正订单编号分隔。这套机制从2014年12月开始实施。',
   DEALER_ID            int comment '门店ID',
   CUST_ID              int comment '客户ID',
   ORDER_DATE           string  comment '下单日期',
   ORDER_TIME           string  comment '下单时间',
   PAYABLE_AMOUNT       decimal(10,2) comment '应付商品总金额',
   REAL_AMOUNT          decimal(10,2) comment '实付商品总金额：应付商品总金额 - 促销优惠金额',
   PAYABLE_FREIGHT      decimal(10,2) comment '应付运费',
   REAL_FREIGHT         decimal(10,2) comment '实付运费',
   EXPECT_SERVICE_FEE   decimal(10,2) comment '应付服务费，如果　EXPECT_SERVICE_FEE > SERVICE_FEE ，则要给门店补贴服务费差 (EXPECT_SERVICE_FEE - SERVICE_FEE)',
   SERVICE_FEE          decimal(10,2) comment '实付服务费',
   PROM_VALUE           decimal(10,2) comment '促销优惠金额，主要是优惠券抵扣金额',
   FULLCUT              decimal(10,2) comment '满减金额，如满59元减5元',
   SETTLEMENT_AMOUNT    decimal(10,2) comment '结算金额，客户最终或实际支付的金额。对应hs_order表order_amount',
   INITIAL_PAY_AMOUNT   decimal(10,2) comment '初始支付金额：对应hs_order表user_pay_amount。记录订单创建时的order_amount，如果客服改单后order_amount可能会发生变化，但user_pay_amount不变',
   PAY_TIME             string comment '付款时间',
   THIRD_PAY_SEQUENCE   varchar(50) comment '第三方支付流水号：空值NULL表示现金支付、 前缀A-表示支付宝支付、T-表示财付通支付；',
   ORDER_STATUS         tinyint comment '订单状态： 1，生成订单；  2，确认订单；3，取消订单；4，作废订单；5，完成订单； 6，无法配送。',
   COUPON_ID            int comment '优惠券ID',
   PROM_CODE_ID         int comment '优惠码ID',
   SPECIFY_TIME         string comment '客户要求送达时间：对应hs_order表accept_time 预订单为客户设定时段的结束时间。',
   DELIVERY_TIME        string comment '发货时间',
   COMPLETION_DATE      string comment '订单完成日期',
   COMPLETION_TIME      string comment '订单完成时间',
   PREG_DEALER_TYPE     int comment '分配订单给门店的依据： 1，关键字分配； 2，按坐标分配店铺；3，人工分配；  4，客户扫描店铺二维码。',
   CONSIGNEE            varchar(20)  comment '收货人',
   SEX                  char(3) comment '性别：男；女；NULL',
   AGE_DESC             varchar(30) comment '年龄描述，可以是一个具体的年龄，也可以是一个模糊但具像的，比如：35岁，35岁左右',
   TELPHONE             varchar(20) comment '电话号码',
   MOBILE               varchar(20) comment '手机号',
   PROVINCE_ID          int comment '省级ID，对应T_DISTRICT表的第1层',
   CITY_ID              int  comment '城市ID，对应T_DISTRICT表的第2层',
   DISTRICT_ID          int comment '行政区ID，对应T_DISTRICT表的第3层（区县）',
   ADDRESS              varchar(100)  comment '街道地址',
   LONGITUDE            decimal(20,8) comment '经度',
   LATITUDE             decimal(20,8) comment '纬度',
   POSTCODE             varchar(6) comment '邮编',
   CUST_NOTE            varchar(255) comment '客户附言，对应hs_order表postscript',
   SERVICE_NOTE         string comment '客服备注',
   DEALER_ACCEPT_TIME   string comment '商户确认接受（即确定可以送货）订单的时间',
   SENDER_MOBILE        varchar(15) comment '配送员手机号',
   REFUND_TIME          string comment '客户申请退货(退款)时间',
   REFUND_STATUS        int comment '退款状态：1，未处理； 2，处理中； 3，处理失败； 4，完成',
   REFUND_RESULT        int comment '退款结果： 1，未退款； 2，部分退款；3，全部退款',
   RECEIVED_GOODS       int comment '是否收到货',
   OPEN_APP_ID          int comment '开放应用ID：0，android市场；1，360；',
   ORDER_SOURCE         int comment '订单来源： -1，其它；1，Android； 2，IOS； 3，微信；4，微博；5，饿了么； 6，PC机； 7，移动端；8，本来生活； 9，中粮我买网；',
   PAY_CHANNEL          int comment '支付渠道：-1，其它； 1，支付宝； 2，微信；3，微博； 4，现金；5，POS机；6，APP微信；7，公众号微信；8，百度钱包；9，QQ钱包； 10，京东钱包；',
   DELIVER_MODE         int  comment '配送模式： 1，门店配送；  2，爱鲜蜂配送； 3，客户自提；4，厂商配送； 5，第三方物流配送； 6，无需配送； 7，门店招募的配送员配送；',
   ORDER_TYPE           int  comment '订单类型（业务类型）： 1，及时达业务； 2，店内业务（地推活动）； 3，线上自提业务；4，自营业务（次日达）； 5，厂商直供业务（大闸蟹）； 6，虚拟订单（售券、秒券）； 7，2c业务；',
   ORDER_FLAG           int  comment '订单标志（按位运算）：1，预订订单；2，收藏店铺订单； 4，客户不可取消订单；',
   EXPECT_TIME          string comment '延迟临界时间点，超过此时间点可以视为延迟送达',
   DELAY_TAG            int comment '延迟标记： 0，送达及时；1，送达延迟。',
   DELAY_BLAME_DEALER   int comment '延迟责任门店ID',
   DISTANCE             decimal(10,4) comment '配送距离(千米)',
   PROD_SUBSIDY         decimal(10,2) comment '商品补贴',
   PROFITS              decimal(10,2) comment '毛利',
   DEALER_PROFITS       decimal(10,2) comment '门店毛利',
   RETURN_AMOUNT        decimal(10,2) comment '给门店返款金额',
   PROM_TYPE            tinyint comment '优惠类型：-1，价格异常； 1，赠券；2，特价； 3，买赠；4，打折；5，抽奖；6，秒杀； 7，免运费；8，满赠；9，满减； 10，满返； 11，搭配减价； 12，商品组合促销； 13，团购；  14，场景；',
   CHEAT_TAG            tinyint  comment '疑似作弊标记',
   LAST_UPDATE_TIME     string comment '最后更新时间',
   PAY_POUNDAGE         decimal(10,2) comment '支付手续费，对应hs_order.pay_fee'
)COMMENT '订单表'
partitioned by (dt string)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\t'
STORED AS TEXTFILE;


alter table ods_order add partition (dt='20151010');
alter table ods_order add partition (dt='20151011');
load data local inpath '/home/anjianbing/soft/data/t_order_data20151010.txt' overwrite into table
ods_order partition(dt='20151010');
load data local inpath '/home/anjianbing/soft/data/t_order_data20151011.txt' overwrite into table
ods_order partition(dt='20151011');