
create table base_properties (
	prop_name varchar(32) not null,
	prop_value varchar(32) not null,
	prop_value_type tinyint not null default 0 comment '0:String，1:Integer, 2:Double',
	prop_module varchar(32) not null,
	prop_desc varchar(256),
	primary key (prop_name)
) comment='配置信息';


INSERT INTO `base_properties` (`prop_name`,`prop_value`,`prop_value_type`,`prop_module`,`prop_desc`) VALUES ('ADMIN_PASSWORD','ee70e58531bc525d5b818be90357faa4',0,'auth','明文admin');
INSERT INTO `base_properties` (`prop_name`,`prop_value`,`prop_value_type`,`prop_module`,`prop_desc`) VALUES ('DUMP_MAX_TIME','5000',1,'monitor','单位秒,需要排除导入导出等长时间的操作');
INSERT INTO `base_properties` (`prop_name`,`prop_value`,`prop_value_type`,`prop_module`,`prop_desc`) VALUES ('GATHER_INTERVAL','300000',1,'monitor','单位秒,采集间格5分钟 5 * 60 * 1000');
INSERT INTO `base_properties` (`prop_name`,`prop_value`,`prop_value_type`,`prop_module`,`prop_desc`) VALUES ('JWT_PASSWORD','JWTPASSWORD',0,'auth',NULL);
INSERT INTO `base_properties` (`prop_name`,`prop_value`,`prop_value_type`,`prop_module`,`prop_desc`) VALUES ('JWT_VALIDATE_SECOND','86400',1,'auth','jwt 一天=86400s 说明 1.tocken过期时，将重新验证，合法就产生新的tocken，不合法就跳到login页');
INSERT INTO `base_properties` (`prop_name`,`prop_value`,`prop_value_type`,`prop_module`,`prop_desc`) VALUES ('MAX_CPU_DUMP','0.85',2,'monitor','cpu使用率超过就dum');
INSERT INTO `base_properties` (`prop_name`,`prop_value`,`prop_value_type`,`prop_module`,`prop_desc`) VALUES ('MAX_MEMORY_DUMP','0.85',2,'monitor','堆内存占比超过就dump');
INSERT INTO `base_properties` (`prop_name`,`prop_value`,`prop_value_type`,`prop_module`,`prop_desc`) VALUES ('SYNC_CONF_INTERVAL','180000',1,'monitor','单位秒,同步配置数据180秒');




create table test (
  test_id 		int not null auto_increment,
  test_name 	varchar(32) not null,
  test_date 	date default null,
  test_value 	decimal(7,2) default null,
  test_type		tinyint,
  created		timestamp not null default current_timestamp,
  primary key (test_id)
);
/** invisible，这样优化器就会忽略这个索引，但是索引依然存在于引擎内部 */
ALTER TABLE test ADD INDEX idx_test_name (test_name ASC) VISIBLE;



create table login_user
(
  user_id		int not null auto_increment,
  user_name 	varchar(32) not null,
  primary key (user_id)
) comment='用户';

create table category
(
  cat_id			int not null auto_increment,
  cat_name	 		varchar(32) not null,
  cat_prio			smallint not null comment '显示优先级',
  cat_status		tinyint not null default 1 comment '1:正常', 
  cat_desc			varchar(255),
  primary key (cat_id)
) comment='分类';

create table product
(
  prod_id		int not null auto_increment,
  cat_id		int not null,
  prod_title	varchar(32) not null,
  prod_price	decimal(7,2),
  recommend 	tinyint not null comment '推荐星级',
  main_img_src	varchar(255) not null comment '主图src',
  user_agent  	varchar(32),
  prod_usp		varchar(32),
  creator		int not null,
  created		timestamp not null default current_timestamp,
  primary key (prod_id)
) comment='商品';

create table product_img
(
  prod_img_id	int not null auto_increment,
  prod_id 		int not null,
  prod_img_src	varchar(255) not null,
  prod_img_prio	smallint not null comment '显示优先级',
  primary key (prod_img_id)
) comment='商品图片(非主图)';




  





