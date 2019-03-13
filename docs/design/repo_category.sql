/*
-- Query: select * from repo_category
LIMIT 0, 500

-- Date: 2019-03-11 20:31
*/
INSERT INTO `repo_category` (`cat_id`,`parent_id`,`cat_name`,`cat_prio`,`cat_status`) VALUES (1,0,'总体',0,1);
INSERT INTO `repo_category` (`cat_id`,`parent_id`,`cat_name`,`cat_prio`,`cat_status`) VALUES (2,0,'产品',1,1);
INSERT INTO `repo_category` (`cat_id`,`parent_id`,`cat_name`,`cat_prio`,`cat_status`) VALUES (3,0,'技术',2,1);
INSERT INTO `repo_category` (`cat_id`,`parent_id`,`cat_name`,`cat_prio`,`cat_status`) VALUES (4,0,'经营',3,1);
INSERT INTO `repo_category` (`cat_id`,`parent_id`,`cat_name`,`cat_prio`,`cat_status`) VALUES (5,0,'对手',4,1);


insert into repo_category values(1,0,'brand',1,1);
insert into repo_category values(2,0,'customer',2,1);
insert into repo_category values(3,0,'channel',3,1);
insert into repo_category values(4,1,'brain',4,1);
insert into repo_category values(5,1,'product',5,1);
insert into repo_category values(6,1,'tech',6,1);
insert into repo_category values(7,3,'operation',7,1);
insert into repo_category values(8,2,'competitor',8,1);
insert into repo_category values(9,1,'common',9,1);


select concat((select cat_name from repo_category where cat_id = c.parent_id), '-', cat_name) cat_name
from repo_category c where c.parent_id != 0 order by c.parent_id, c.cat_prio




