drop table if exists `dim_goods`;
CREATE TABLE `dim_goods` (
  `GOODS_ID` int COMMENT '商品ID',
  `GOODS_NAME` varchar(64) COMMENT '商品名称',
  `GOODS_LONG_NAME` varchar(100) COMMENT '商品长名称',
  `GOODS_TYPE` tinyint COMMENT '商品类型：0，普通商品；1，套餐商品；',
  `BRAND_ID` int COMMENT '品牌ID',
  `CATEGORY_ID` int COMMENT '后台分类ID',
  `SKU_NAME` varchar(64) COMMENT '销售属性名称',
  `SKU_VALUE` varchar(64) COMMENT '销售属性值',
  `SKU_GROUP` varchar(64) COMMENT '销售属性分组ID',
  `BAR_CODE` varchar(64) COMMENT '商品国条码',
  `SPEC` varchar(64) COMMENT '商品规格',
  `SPEC_UNIT` varchar(64) COMMENT '规格单位',
  `SAFE` int COMMENT '保质期',
  `SAFE_UNIT` tinyint COMMENT '保质期单位：1，天；2，月；3，年；',
  `STORAGE` tinyint COMMENT '储存方式：1，常温；2，冷藏；3，冷冻；',
  `PACK_UNIT` varchar(64) COMMENT '包装单位',
  `BOX_SPEC` int COMMENT '箱规',
  `PRODUCTION` varchar(64) COMMENT '产地',
  `PROMOTIONS` varchar(100) COMMENT '促销信息',
  `SEO_KEY` varchar(255) COMMENT 'SEO关键词',
  `SEO_CONTENT` varchar(255) COMMENT 'SEO描述',
  `COPY_SOURCE` int COMMENT '复制源',
  `IS_FMCG` tinyint COMMENT '是否是FMCG'
)  comment '商品表'
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\t'
STORED AS TEXTFILE;
