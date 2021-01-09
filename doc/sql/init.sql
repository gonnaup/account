DELETE FROM application_code;
INSERT INTO application_code (application_name, application_code)
VALUES ('AccountManagement', 10);

delete
from application_sequence;
INSERT INTO application_sequence (application_name, sequence_type, sequence, step)
VALUES ('AccountManagement', 'account', 100, 100);
INSERT INTO application_sequence (application_name, sequence_type, sequence, step)
VALUES ('AccountManagement', 'authentication', 100, 100);
INSERT INTO application_sequence (application_name, sequence_type, sequence, step)
VALUES ('AccountManagement', 'role_permission', 100, 100);
INSERT INTO application_sequence (application_name, sequence_type, sequence, step)
VALUES ('AccountManagement', 'operationLog', 100, 100);

DELETE FROM account_role;
DELETE FROM authentication;
DELETE FROM role_permission;
DELETE FROM account;
DELETE FROM role;
DELETE FROM permission;

INSERT INTO account(id, application_name, account_name, account_nickname, account_avatar, tag)
VALUES (102020121100000001, 'AccountManagement', 'admin', 'admin', NULL, 'admin account');
INSERT INTO account(id, application_name, account_name, account_nickname, account_avatar, tag)
VALUES (102020121100000002, 'AccountManagement', 'read', 'read-only', NULL, 'read only account');
-- default admin account admin-123456
INSERT INTO authentication(id, account_id, application_name, auth_type, identifier, credential, expires)
VALUES (102020121100000001, 102020121100000001, 'AccountManagement', 'E', 'gonnaup@yeah.net',
        '401E07A0ECFD7A36091B3CCC194BAB65', 0);
-- default read-only account read-123456
INSERT INTO authentication(id, account_id, application_name, auth_type, identifier, credential, expires)
VALUES (102020121100000002, 102020121100000002, 'AccountManagement', 'E', 'gonnaup@qq.com',
        '401E07A0ECFD7A36091B3CCC194BAB65', 0);

INSERT INTO role (id, application_name, role_name, description, score)
VALUES (102021010700000101, 'AccountManagement', 'ADMIN', '系统管理员', '7FFFFFFF');
INSERT INTO role (id, application_name, role_name, description, score)
VALUES (102021010700000102, 'AccountManagement', 'APPALL', '应用管理员', '0FFFFFFF');
INSERT INTO role (id, application_name, role_name, description, score)
VALUES (102021010700000103, 'AccountManagement', 'APPRDAU', '应用读取、删除、新增、修改权限', '0000FFFF');
INSERT INTO role (id, application_name, role_name, description, score)
VALUES (102021010700000104, 'AccountManagement', 'APPRAU', '应用读取、新增、修改权限', '00000FFF');
INSERT INTO role (id, application_name, role_name, description, score)
VALUES (102021010700000105, 'AccountManagement', 'APPRUD', '应用读取、修改、删除权限', '0000F0FF');
INSERT INTO role (id, application_name, role_name, description, score)
VALUES (102021010700000106, 'AccountManagement', 'APPR', '应用只读角色', '0000000F');

INSERT INTO account_role (account_id, role_id)
VALUES (102020121100000001, 102021010700000101);
INSERT INTO account_role (account_id, role_id)
VALUES (102020121100000002, 102021010700000106);

INSERT INTO permission (id, application_name, permission_name, description, weight)
VALUES (102021010700000107, 'AccountManagement', 'ALL', '系统所有权限', '7FFFFFFF');
INSERT INTO permission (id, application_name, permission_name, description, weight)
VALUES (102021010700000108, 'AccountManagement', 'APP_ALL', '应用级别所有权限', '0FFFFFFF');
INSERT INTO permission (id, application_name, permission_name, description, weight)
VALUES (102021010700000109, 'AccountManagement', 'APP_D', '应用删除权限', '0000F000');
INSERT INTO permission (id, application_name, permission_name, description, weight)
VALUES (102021010700000110, 'AccountManagement', 'APP_A', '应用新增权限', '00000F00');
INSERT INTO permission (id, application_name, permission_name, description, weight)
VALUES (102021010700000111, 'AccountManagement', 'APP_U', '应用修改权限', '000000F0');
INSERT INTO permission (id, application_name, permission_name, description, weight)
VALUES (102021010700000112, 'AccountManagement', 'APP_R', '应用只读权限', '0000000F');

INSERT INTO role_permission (role_id, permission_id)
VALUES (102021010700000101, 102021010700000107);
INSERT INTO role_permission (role_id, permission_id)
VALUES (102021010700000102, 102021010700000108);
INSERT INTO role_permission (role_id, permission_id)
VALUES (102021010700000103, 102021010700000109);
INSERT INTO role_permission (role_id, permission_id)
VALUES (102021010700000103, 102021010700000110);
INSERT INTO role_permission (role_id, permission_id)
VALUES (102021010700000103, 102021010700000111);
INSERT INTO role_permission (role_id, permission_id)
VALUES (102021010700000103, 102021010700000112);
INSERT INTO role_permission (role_id, permission_id)
VALUES (102021010700000104, 102021010700000110);
INSERT INTO role_permission (role_id, permission_id)
VALUES (102021010700000104, 102021010700000111);
INSERT INTO role_permission (role_id, permission_id)
VALUES (102021010700000104, 102021010700000112);
INSERT INTO role_permission (role_id, permission_id)
VALUES (102021010700000105, 102021010700000109);
INSERT INTO role_permission (role_id, permission_id)
VALUES (102021010700000105, 102021010700000111);
INSERT INTO role_permission (role_id, permission_id)
VALUES (102021010700000105, 102021010700000112);
INSERT INTO role_permission (role_id, permission_id)
VALUES (102021010700000106, 102021010700000112);