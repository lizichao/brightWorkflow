
delete from pcmc_user where usertype <> '99';
delete from pcmc_user where usercode ='test';
delete from pcmc_user_dept where userid <> '1';
delete from pcmc_user_role where userid <> '1';
delete from pcmc_user_desktop;
delete from pcmc_user_ext where userid <> '1';
delete from pcmc_user_login;
delete from pcmc_uslogin_log;


delete from pcmc_log;


CREATE TABLE `seq_block` (
  `name` varchar(50) NOT NULL COMMENT 'test',
  `idx` bigint(20) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TRIGGER `t_afterinsert_on_seq_block` AFTER INSERT ON `seq_block` FOR EACH ROW BEGIN	INSERT INTO pcmc_send_queue (sys_name,table_name,pk_name,pk_value,op_model,dest_ipaddress,dest_port,created_date	)VALUES(	'yuexue','seq_block','name',new.name,'add',	(SELECT t.dest_ipaddress FROM pcmc_send_host t WHERE t.dest_type = '00'	LIMIT 1),	(SELECT	t.dest_port	FROM pcmc_send_host t WHERE	t.dest_type = '00' LIMIT 1),	SYSDATE());	END;

CREATE TRIGGER `t_afterupdate_on_seq_block` AFTER UPDATE ON `seq_block` FOR EACH ROW BEGIN 	INSERT INTO pcmc_send_queue (sys_name,table_name,pk_name,pk_value,op_model,dest_ipaddress,dest_port,created_date	)VALUES(	'yuexue','seq_block','name',old.name,'update',	(SELECT	t.dest_ipaddress FROM pcmc_send_host t	WHERE	t.dest_type = '00'	LIMIT 1),	(SELECT t.dest_port	FROM pcmc_send_host t	WHERE	t.dest_type = '00'	LIMIT 1	),	SYSDATE());   END;

CREATE TRIGGER `t_afterdelete_on_seq_block` AFTER DELETE ON `seq_block` FOR EACH ROW BEGIN	INSERT INTO pcmc_send_queue (sys_name,table_name,pk_name,pk_value,op_model,dest_ipaddress,dest_port,created_date	)VALUES(	'yuexue','seq_block','name',old.name,'delete',	(SELECT	t.dest_ipaddress FROM pcmc_send_host t WHERE t.dest_type = '00' LIMIT 1),	(SELECT	t.dest_port	FROM pcmc_send_host t	WHERE	t.dest_type = '00'	LIMIT 1),	SYSDATE());	END;