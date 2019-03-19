
create table test (
  test_id 		int not null auto_increment,
  test_name 	varchar(32) not null,
  test_date 	date default null,
  test_value 	decimal(7,2) default null,
  test_type		tinyint,
  created		timestamp not null default current_timestamp,
  primary key (test_id)
);
/** 如果是invisible，这样优化器就会忽略这个索引，但是索引依然存在于引擎内部 */
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









/** 仓库 repository(repo) >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */

create table repo_category
(
  cat_id			int not null auto_increment,
  parent_id			int not null default 0 comment '0:一级目录',
  cat_name	 		varchar(32) not null,
  cat_prio			smallint not null comment '显示优先级',
  cat_status		tinyint not null default 1 comment '1:正常,0:作废',
  primary key (cat_id)
) comment='分类';


create table repo_knowledge
(
  k_id			int not null auto_increment,
  k_title		varchar(32) not null,
  cat_id		int not null,
  recommend 	tinyint not null default 3 comment '推荐星级1、2、3、4、5',
  created		timestamp not null default current_timestamp,
  created_by	int not null,
  modified		timestamp not null default current_timestamp,
  modified_by   int not null,
  main_img_src	varchar(255) comment '主图src',
  primary key (k_id)
) comment='';


create table repo_knowledge_content
(
  k_id		int not null auto_increment,
  k_content	varchar(4000),
  primary key (k_id)
) comment='';

create table repo_knowledge_img
(
  k_img_id		int not null auto_increment,
  k_id 			int not null,
  k_img_src	varchar(255) not null,
  k_img_prio	smallint not null comment '显示优先级',
  primary key (k_img_id)
) comment='非主';



















  





