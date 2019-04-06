
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


/** 仓库 repository(repo) >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */

create table repo_knowledge_category
(
  cat_id			int not null auto_increment,
  parent_id			int not null default 0 comment '0:一级目录',
  cat_name	 		varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci not null,
  cat_prio			smallint not null comment '显示优先级',
  cat_status		tinyint not null default 1 comment '1:正常,0:作废',
  primary key (cat_id)
) comment='分类';


create table repo_knowledge
(
  k_id				int not null auto_increment,
  k_title			varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci not null,
  cat_id			int not null,
  recommend 		tinyint not null default 3 comment '推荐星级1、2、3、4、5',
  recommend_prio	int not null comment '星级排序',
  modified			timestamp not null default current_timestamp,
  modified_by   	int not null,
  main_img_src		varchar(255) comment '主图src',
  primary key (k_id)
) comment='';

create table repo_knowledge_usp
(
	usp_id		int not null auto_increment,
	usp_name 	varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci not null,
	usp_prio	smallint not null comment '显示优先级',
	usp_status	tinyint not null default 1 comment '1:正常,0:作废',
	primary key (usp_id)
) comment='独特销售主张,好奇、速度';
ALTER TABLE `repository`.`repo_knowledge_usp` ADD UNIQUE INDEX `idx_usp_name` (`usp_name` ASC) VISIBLE;

create table repo_knowledge_map_usp
(
	usp_id			int not null,
	k_id			int not null,
	recommend_prio	int not null comment '星级排序',
	primary key (usp_id, k_id)
) comment='';


create table repo_knowledge_content
(
  k_id		int not null auto_increment,
  k_content	varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
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


create table repo_search_word
(
  word 		varchar(64),
  k_id 		int not null,
  cat_id	int not null,
  modified	timestamp not null default current_timestamp, 
  primary key (word, k_id)
) comment='';
ALTER TABLE `repo_search_word` ADD INDEX `idx_search_k_id` (`k_id` ASC) VISIBLE;


 

















  





