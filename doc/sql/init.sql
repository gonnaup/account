delete from application_code;
insert into application_code (application_name, application_code)
values ('AccountManagement', 10);

delete from application_sequence;
insert into application_sequence (application_name, sequence_type, sequence, step)
values ('AccountManagement', 'account', 100, 100);
insert into application_sequence (application_name, sequence_type, sequence, step)
values ('AccountManagement', 'authentication', 100, 100);
insert into application_sequence (application_name, sequence_type, sequence, step)
values ('AccountManagement', 'role_permission', 100, 100);
insert into application_sequence (application_name, sequence_type, sequence, step)
values ('AccountManagement', 'operationLog', 100, 100);

delete from account_role;
delete from authentication;
delete from role_permission;
delete from account;
delete from role;
delete from permission;

insert into account(id, application_name, account_name, account_nickname, account_avatar, tag)
values (102020121100000001, 'AccountManagement', 'admin', 'admin', NULL, 'admin account');
-- default admin account admin-123456
insert into authentication(id, account_id, application_name, auth_type, identifier, credential, expires)
values (102020121100000001, 102020121100000001, 'AccountManagement', 'E', 'gonnaup@yeah.net',
        '401E07A0ECFD7A36091B3CCC194BAB65', 0);

insert into role(id, application_name, role_name, description)
values (102020121100000001, 'AccountManagement', 'ADMIN', '系统管理员');
insert into role(id, application_name, role_name, description)
values (102020121100000002, 'AccountManagement', 'APPALL', '应用管理员');
insert into role(id, application_name, role_name, description)
values (102020121100000003, 'AccountManagement', 'APPRAUD', '应用读、新增、修改、删除权限');
insert into role(id, application_name, role_name, description)
values (102020121100000004, 'AccountManagement', 'APPRAU', '应用读、新增、修改权限');
insert into role(id, application_name, role_name, description)
values (102020121100000005, 'AccountManagement', 'APPRUD', '应用读、修改、删除权限');
insert into role(id, application_name, role_name, description)
values (102020121100000006, 'AccountManagement', 'APPR', '应用只读角色');

insert into account_role(account_id, role_id)
values (102020121100000001, 102020121100000001);

insert into permission(id, application_name, permission_name, description)
values (102020121100000007, 'AccountManagement', 'ALL', '系统所有权限');
insert into permission(id, application_name, permission_name, description)
values (102020121100000008, 'AccountManagement', 'APP_ALL', '应用级别所有权限');
insert into permission(id, application_name, permission_name, description)
values (102020121100000009, 'AccountManagement', 'APP_RW', '应用写权限，包括新增、修改、删除');
insert into permission(id, application_name, permission_name, description)
values (102020121100000010, 'AccountManagement', 'APP_RA', '应用新增权限');
insert into permission(id, application_name, permission_name, description)
values (102020121100000011, 'AccountManagement', 'APP_RU', '应用修改权限');
insert into permission(id, application_name, permission_name, description)
values (102020121100000012, 'AccountManagement', 'APP_RD', '应用删除权限');
insert into permission(id, application_name, permission_name, description)
values (102020121100000013, 'AccountManagement', 'APP_R', '应用只读权限');

insert into role_permission(role_id, permission_id)
values (102020121100000001, 102020121100000007);
insert into role_permission(role_id, permission_id)
values (102020121100000002, 102020121100000008);
insert into role_permission(role_id, permission_id)
values (102020121100000003, 102020121100000009);
insert into role_permission(role_id, permission_id)
values (102020121100000004, 102020121100000010);
insert into role_permission(role_id, permission_id)
values (102020121100000004, 102020121100000011);
insert into role_permission(role_id, permission_id)
values (102020121100000005, 102020121100000011);
insert into role_permission(role_id, permission_id)
values (102020121100000005, 102020121100000012);
insert into role_permission(role_id, permission_id)
values (102020121100000006, 102020121100000013);