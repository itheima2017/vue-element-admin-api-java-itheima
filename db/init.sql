#设置字符编码
set character set utf8;
#创建数据库
CREATE DATABASE IF NOT EXISTS vue_element_admin DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;
USE vue_element_admin;
#创建表
CREATE TABLE `pe_permission_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `name` varchar(255) DEFAULT NULL COMMENT '权限名称',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_k3beff7qglfn58qsf2yvbg41i` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `pe_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `description` text COMMENT '权限描述',
  `name` varchar(255) DEFAULT NULL COMMENT '权限名称',
  `type` tinyint(4) DEFAULT NULL COMMENT '权限类型 1为菜单 2为功能 3为API',
  `pid` bigint(20) DEFAULT NULL COMMENT '主键',
  `permission_api_extend_id` bigint(20) DEFAULT NULL COMMENT '主键ID',
  `permission_menu_extend_id` bigint(20) DEFAULT NULL COMMENT '主键ID',
  `permission_point_extend_id` bigint(20) DEFAULT NULL COMMENT '主键ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `pe_permission_api_extend` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `api_level` int(11) DEFAULT NULL COMMENT '权限等级，1为通用接口权限，2为需校验接口权限',
  `api_method` varchar(255) DEFAULT NULL COMMENT '请求类型',
  `api_url` varchar(255) DEFAULT NULL COMMENT '链接',
  `permission_id` bigint(20) DEFAULT NULL COMMENT '主键',
  PRIMARY KEY (`id`),
  KEY `FKcuumocmq03no1grx1pp7mi0ya` (`permission_id`),
  CONSTRAINT `FKcuumocmq03no1grx1pp7mi0ya` FOREIGN KEY (`permission_id`) REFERENCES `pe_permission` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `pe_permission_menu_extend` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `code` text COMMENT '权限代码',
  `permission_id` bigint(20) DEFAULT NULL COMMENT '主键',
  PRIMARY KEY (`id`),
  KEY `FK42ix9ooed2li4ig9ry78chkaq` (`permission_id`),
  CONSTRAINT `FK42ix9ooed2li4ig9ry78chkaq` FOREIGN KEY (`permission_id`) REFERENCES `pe_permission` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `pe_permission_point_extend` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `code` text COMMENT '权限代码',
  `permission_id` bigint(20) DEFAULT NULL COMMENT '主键',
  PRIMARY KEY (`id`),
  KEY `FKml56235rji52opnlq6cr143l1` (`permission_id`),
  CONSTRAINT `FKml56235rji52opnlq6cr143l1` FOREIGN KEY (`permission_id`) REFERENCES `pe_permission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `pe_permission` ADD CONSTRAINT `FKcl4mcrsqinb3q8iwsyr35u6nw` FOREIGN KEY (`pid`) REFERENCES `pe_permission` (`id`);
ALTER TABLE `pe_permission` ADD CONSTRAINT `FKi0m9bwff032wcooqvql5frdbg` FOREIGN KEY (`permission_point_extend_id`) REFERENCES `pe_permission_point_extend` (`id`);
ALTER TABLE `pe_permission` ADD CONSTRAINT `FKkbf8xphs59e2lebopx3npjd2q` FOREIGN KEY (`permission_menu_extend_id`) REFERENCES `pe_permission_menu_extend` (`id`);
ALTER TABLE `pe_permission` ADD CONSTRAINT `FKs1u37sxlynb8jsx1dfptwdnpo` FOREIGN KEY (`permission_api_extend_id`) REFERENCES `pe_permission_api_extend` (`id`);

CREATE TABLE `a_permission_permission_group` (
  `pgid` bigint(20) NOT NULL COMMENT '权限组ID',
  `pid` bigint(20) NOT NULL COMMENT '权限ID',
  PRIMARY KEY (`pgid`,`pid`),
  KEY `FK74qx7rkbtq2wqms78gljv87a0` (`pid`),
  CONSTRAINT `FK74qx7rkbtq2wqms78gljv87a0` FOREIGN KEY (`pid`) REFERENCES `pe_permission` (`id`),
  CONSTRAINT `FKee9dk0vg99shvsytflym6egxd` FOREIGN KEY (`pgid`) REFERENCES `pe_permission_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `bs_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `introduction` text COMMENT '介绍',
  `last_update_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  `password` text COMMENT '密码',
  `phone` varchar(255) DEFAULT NULL COMMENT '手机号码',
  `status` int(11) DEFAULT '0' COMMENT '账号状态 0为启用，1为禁用',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名称',
  `permission_group_id` bigint(20) DEFAULT NULL COMMENT '权限组ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_9d5myufq0sev17gw41fcio4jf` (`email`),
  UNIQUE KEY `UK_h2hvo40aswrpdlj6mrjw0jo17` (`phone`),
  KEY `FK9uhq7vr73fopxl940siqo037k` (`permission_group_id`),
  CONSTRAINT `FK9uhq7vr73fopxl940siqo037k` FOREIGN KEY (`permission_group_id`) REFERENCES `pe_permission_group` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `method` varchar(255) DEFAULT NULL COMMENT 'method',
  `operation_date` datetime DEFAULT NULL COMMENT '操作时间',
  `operation_result` tinyint(1) DEFAULT NULL COMMENT '操作结果',
  `request_body` text COMMENT '参数内容',
  `url` varchar(255) DEFAULT NULL COMMENT 'url',
  `user_id` bigint(20) DEFAULT NULL COMMENT '操作人ID',
  PRIMARY KEY (`id`),
  KEY `FKlt2yft8n91ep783g16knhvilp` (`user_id`),
  CONSTRAINT `FKlt2yft8n91ep783g16knhvilp` FOREIGN KEY (`user_id`) REFERENCES `bs_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4;

#添加菜单权限
INSERT INTO pe_permission (name,type,create_time) VALUES ("后台框架",1,NOW());
INSERT INTO pe_permission_menu_extend (code,permission_id) VALUES ("base",(SELECT id FROM pe_permission WHERE name="后台框架"));
UPDATE pe_permission SET permission_menu_extend_id = (SELECT id FROM pe_permission_menu_extend WHERE code="base") WHERE name = "后台框架";

INSERT INTO pe_permission (name,type,create_time) VALUES ("用户管理",1,NOW());
INSERT INTO pe_permission_menu_extend (code,permission_id) VALUES ("base-users",(SELECT id FROM pe_permission WHERE name="用户管理"));
UPDATE pe_permission SET permission_menu_extend_id = (SELECT id FROM pe_permission_menu_extend WHERE code="base-users"),pid=(SELECT a.id FROM (SELECT id FROM pe_permission WHERE name="后台框架") a) WHERE name = "用户管理";

INSERT INTO pe_permission (name,type,create_time) VALUES ("菜单管理",1,NOW());
INSERT INTO pe_permission_menu_extend (code,permission_id) VALUES ("base-menus",(SELECT id FROM pe_permission WHERE name="菜单管理"));
UPDATE pe_permission SET permission_menu_extend_id = (SELECT id FROM pe_permission_menu_extend WHERE code="base-menus"),pid=(SELECT a.id FROM (SELECT id FROM pe_permission WHERE name="后台框架") a) WHERE name = "菜单管理";

INSERT INTO pe_permission (name,type,create_time) VALUES ("权限管理",1,NOW());
INSERT INTO pe_permission_menu_extend (code,permission_id) VALUES ("base-permissions",(SELECT id FROM pe_permission WHERE name="权限管理"));
UPDATE pe_permission SET permission_menu_extend_id = (SELECT id FROM pe_permission_menu_extend WHERE code="base-permissions"),pid=(SELECT a.id FROM (SELECT id FROM pe_permission WHERE name="后台框架") a) WHERE name = "权限管理";

INSERT INTO pe_permission (name,type,create_time) VALUES ("日志管理",1,NOW());
INSERT INTO pe_permission_menu_extend (code,permission_id) VALUES ("base-logs",(SELECT id FROM pe_permission WHERE name="日志管理"));
UPDATE pe_permission SET permission_menu_extend_id = (SELECT id FROM pe_permission_menu_extend WHERE code="base-logs"),pid=(SELECT a.id FROM (SELECT id FROM pe_permission WHERE name="后台框架") a) WHERE name = "日志管理";

#添加“超级管理员”权限组
INSERT INTO pe_permission_group(name,create_time,update_time) VALUES ("超级管理员",NOW(),NOW());

#添加权限组与权限关系
DELIMITER ;;
CREATE PROCEDURE insert_init()
begin
declare num int ;
set num = (select MIN(id) from pe_permission) ;
set @pcount = (select MAX(id) from pe_permission);
set @curpgid = (SELECT id FROM pe_permission_group WHERE name="超级管理员");
while num <= @pcount do
    insert into a_permission_permission_group (pgid,pid)
values
    (@curpgid,num) ;
set num = num + 1 ;
end WHILE;
END;;
CALL insert_init();
DROP PROCEDURE insert_init;

#添加用户,密码原文为：root123456，采用sha256加密
INSERT INTO bs_user(username,password,email,create_time,last_update_time,permission_group_id) VALUES("超级管理员","28f4c77c534d5358329b61b326c995cd1743e2e37dd13949ace9c9b816de1fa9","root@admin.com",NOW(),NOW(),(SELECT id FROM pe_permission_group where name = "超级管理员"));