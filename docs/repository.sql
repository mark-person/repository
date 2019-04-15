
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
  main_img_src		varchar(255) not null comment '主图src',
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
ALTER TABLE `repo_knowledge_usp` ADD UNIQUE INDEX `idx_usp_name` (`usp_name` ASC) VISIBLE;

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


create table repo_search_word (
  word 				varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci not null,
  k_id 				int not null,
  cat_id			int not null,
  recommend_prio	int not null,
  modified			timestamp not null default current_timestamp, 
  primary key (word, k_id)
) comment='';
ALTER TABLE `repo_search_word` ADD INDEX `idx_search_k_id` (`k_id` ASC) VISIBLE;

create table repo_search_dict (
  word			int not null,
  cat_id		int not null,
  primary key (word)
) comment='';

create table repo_user (
  repo_user_id 		int(11) not null,
  repo_user_name 	varchar(32) not null,
  repo_user_status 	tinyint(1) not null default 1 comment '0:删除 1:正常',
  favorite_n		int(11) not null default 0 comment '收藏数量',
  todo_n			int(11) not null default 0 comment '待办数量',
  created 			timestamp not null default current_timestamp,
  primary key (repo_user_id)
) comment='知识用户';

create table repo_favorite (
	repo_user_id	int(11) not null,
	k_id			int(11) not null,
	created 		timestamp not null default current_timestamp,
	primary key (repo_user_id, k_id)
) comment='收藏';

create table repo_todo (
	todo_id 		int not null auto_increment,
	todo_title 		varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
	todo_status		tinyint(1) not null default 1 comment '1:待办 2:已办',
	todo_important	tinyint(1) not null default 1 comment '0:次要 1:一般 2:重要',
	todo_emergent	tinyint(1) not null default 1 comment '0:缓慢 1:一般 2:紧急',
	modified		timestamp not null default current_timestamp,
 	modified_by   	int not null,
	primary key (todo_id)
)



create table repo_knowledge_subject (
	subject_id		int not null auto_increment,
	subject_name 	varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci not null,
	subject_prio	smallint not null comment '显示优先级',
	subject_status	tinyint not null default 1 comment '1:正常,0:作废',
	primary key (subject_id)
) comment='专题';
ALTER TABLE `repo_knowledge_subject` ADD UNIQUE INDEX `idx_subject_name` (`subject_name` ASC) VISIBLE;

create table repo_knowledge_map_subject (
	subject_id		int not null,
	k_id			int not null,
	primary key (subject_id, k_id)
) comment='';






/**
 * # 初始化数据
 * # 导有表情的 mysqldump -u root -pPASSWORD repository repo_knowledge_usp --default-character-set=utf8mb4 >c:\test.sql
 */

INSERT INTO `repo_user` (`repo_user_id`,`repo_user_name`,`repo_user_status`,`favorite_n`,`created`) VALUES (-1,'我是小丑',1,0,now());
INSERT INTO `repo_user` (`repo_user_id`,`repo_user_name`,`repo_user_status`,`favorite_n`,`created`) VALUES (-2,'我是游客',1,0,now());


insert into repo_knowledge_category values(1,0,'品牌',1,1);
insert into repo_knowledge_category values(2,0,'客户',2,1);
insert into repo_knowledge_category values(3,0,'渠道',3,1);

insert into repo_knowledge_category values(4,1,'头脑风暴',4,1);
insert into repo_knowledge_category values(5,1,'产品',5,1);
insert into repo_knowledge_category values(6,1,'常识',6,1);
insert into repo_knowledge_category values(7,1,'技术',7,1);

insert into repo_knowledge_category values(8,2,'竞争对手',8,1);
insert into repo_knowledge_category values(9,3,'运营',9,1);
insert into repo_knowledge_category values(10,3,'广告',10,1);


INSERT INTO `repo_knowledge_usp` VALUES (1,'创意💡',1,1),(2,'营销💹',2,1),(3,'省钱💰',3,1),(4,'速度💥',4,1),
(5,'好奇😲',5,1),(6,'玩乐😊',6,1),(7,'简单🔺',7,1),(8,'展示🔮',8,1);

INSERT INTO `repo_knowledge_subject` (`subject_id`,`subject_name`,`subject_prio`,`subject_status`) VALUES (1,'必备小物品',1,1);











  





