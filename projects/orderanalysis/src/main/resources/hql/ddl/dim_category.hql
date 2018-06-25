#分类维度表
drop table if exists dim_category;
CREATE TABLE `dim_category` (
  `tree_id` int COMMENT '分类树ID',
  `tree_name` varchar(64) COMMENT '分类树名称',
  `category_id` int COMMENT '分类ID',
  `category_name` varchar(64) COMMENT '分类名称',
  `category_type` int COMMENT '分类类型0后台分类1前台分类',
  `parent_id` int COMMENT '上级分类树根的是0',
  `parent_name` varchar(64) COMMENT '父分类名称',
  `layer` tinyint COMMENT '层级',
  `sort` int COMMENT '分类排序',
  `path` varchar(255) COMMENT '所有上级分类',
  `is_fmcg` tinyint comment '是否快消品1是0否',
  `is_open` tinyint COMMENT '是否打开1是0否',
  `is_show` tinyint COMMENT 'app是否显示1是0否',
  `visibility` tinyint COMMENT '商户端是否显示1是0否')
  comment '分类表'
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\t'
STORED AS TEXTFILE;