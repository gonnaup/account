insert into application_code (application_name, application_code)
values ('AccountManagement', 10);
insert into application_sequence (application_name, sequence_type, sequence, step)
values ('AccountManagement', 'account', 100, 100);
insert into application_sequence (application_name, sequence_type, sequence, step)
values ('AccountManagement', 'authentication', 100, 100);
insert into application_sequence (application_name, sequence_type, sequence, step)
values ('AccountManagement', 'role_permission', 100, 100);
insert into application_sequence (application_name, sequence_type, sequence, step)
values ('AccountManagement', 'operationLog', 100, 100);
insert into account(id, application_name, account_name, account_nickname, account_avatar, tag)
values (102020121100000001, 'AccountManagement', 'admin', 'admin', NULL, 'admin account');
-- default admin account admin-123456
insert into authentication(id, account_id, application_name, auth_type, identifier, credential, expires)
values (102020121100000001, 102020121100000001, 'AccountManagement', 'E', 'gonnaup@yeah.net', '401E07A0ECFD7A36091B3CCC194BAB65', 0)