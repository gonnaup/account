insert into application_code (application_name, application_code)
values ('AccountManagement', 10);
insert into application_sequence (application_name, sequence_type, sequence, step)
values ('AccountManagement', 'account', 0, 100);
insert into application_sequence (application_name, sequence_type, sequence, step)
values ('AccountManagement', 'authentication', 0, 100);
insert into application_sequence (application_name, sequence_type, sequence, step)
values ('AccountManagement', 'role_permission', 0, 100);
insert into application_sequence (application_name, sequence_type, sequence, step)
values ('AccountManagement', 'operationLog', 0, 100);