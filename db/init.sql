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
set num = 1 ;
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