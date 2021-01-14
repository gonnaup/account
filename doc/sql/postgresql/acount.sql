-- auto update timestamp when update the row
create or replace function autoupdate_timestamp()
returns trigger as
$$
begin
    new.updatetime = now();
    return new;
end;
$$ language plpgsql;

-- -----------------------------------------------------
-- Table account
-- -----------------------------------------------------

DROP TABLE IF EXISTS account cascade;

CREATE TABLE account
(
    id               BIGINT       NOT NULL,
    application_name VARCHAR(50)  NOT NULL,
    account_name     VARCHAR(50)  NOT NULL,
    account_nickname VARCHAR(50)  NOT NULL,
    account_avatar   VARCHAR(256) NULL,
    account_state    CHAR(1)      NOT NULL DEFAULT 'N',
    last_logintime   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tag              VARCHAR(255) NULL,
    createtime       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatetime       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
comment on column account.id is 'ID';
comment on column account.application_name is '应用名称';
comment on column account.account_name is '用户名，同一应用中唯一';
comment on column account.account_nickname is '昵称';
comment on column account.account_avatar is '头像';
comment on column account.account_state is '账户状态\nN - 正常\nF - 禁用';
comment on column account.last_logintime is '最近成功登陆时间，可用于清除僵尸账户';
comment on column account.tag is '账户标记，管理员使用';
comment on column account.createtime is '创建时间';
comment on column account.updatetime is '更新时间';
COMMENT on table account is '账户信息';
CREATE UNIQUE INDEX account_application_account_name ON account (application_name ASC, account_name ASC);
CREATE INDEX account_application_account_nickname ON account (application_name ASC, account_nickname ASC);
create trigger auto_updatetime_account before update on account for each row execute procedure autoupdate_timestamp();-- auto update column updatetime

-- -----------------------------------------------------
-- Table authentication
-- -----------------------------------------------------
DROP TABLE IF EXISTS authentication cascade;

CREATE TABLE authentication
(
    id               BIGINT       NOT NULL,
    account_id       BIGINT       NOT NULL,
    application_name VARCHAR(50)  NOT NULL,
    auth_type        CHAR(1)      NOT NULL,
    identifier       VARCHAR(255) NOT NULL,
    credential       VARCHAR(255) NULL,
    expires          BIGINT       NULL,
    createtime       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatetime       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_Authentication_1 FOREIGN KEY (account_id) REFERENCES account (id)
);
comment on column authentication.id is 'ID';
comment on column authentication.account_id is '账号ID';
comment on column authentication.application_name is '应用名称';
comment on column authentication.auth_type is '认证类型\nP-密码\nE-邮箱\nW-微信\nQ-QQ\nB-微博';
comment on column authentication.identifier is '唯一标识(用户名，\n邮箱或第三方应用\n的唯一标识)';
comment on column authentication.credential is '凭证(密码或第三方token)';
comment on column authentication.expires is '过期时间，OAuth2登录时使用';
comment on column authentication.createtime is '创建时间';
comment on column authentication.updatetime is '更新时间';
COMMENT on table authentication is '账户认证信息';
CREATE UNIQUE INDEX auth_unique_index ON authentication (application_name ASC, auth_type ASC, identifier ASC);
create trigger auto_updatetime_authentication before update on authentication for each row execute procedure autoupdate_timestamp();-- auto update column updatetime


-- -----------------------------------------------------
-- Table role
-- -----------------------------------------------------
DROP TABLE IF EXISTS role cascade;

CREATE TABLE role
(
    id               BIGINT       NOT NULL,
    application_name VARCHAR(50)  NOT NULL,
    role_name        VARCHAR(64)  NOT NULL,
    score            VARCHAR(30)  NULL,
    description      VARCHAR(512) NULL,
    createtime       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatetime       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
comment on column role.id is 'ID';
comment on column role.application_name is '所属服务(为不同服务定制不同角色)';
comment on column role.role_name is '角色名';
comment on column role.score is '权限总分数用于判断是否有某种权限，可选用作扩展';
comment on column role.description is '角色描述';
comment on column role.createtime is '创建时间';
comment on column role.updatetime is '更新时间';
COMMENT on table role is '账户角色表';
create trigger auto_updatetime_role before update on role for each row execute procedure autoupdate_timestamp();-- auto update column updatetime


-- -----------------------------------------------------
-- Table permission
-- -----------------------------------------------------
DROP TABLE IF EXISTS permission cascade;

CREATE TABLE permission
(
    id               BIGINT       NOT NULL,
    application_name VARCHAR(50)  NOT NULL,
    permission_name  VARCHAR(30)  NOT NULL,
    weight           VARCHAR(30)  NULL,
    description      VARCHAR(512) NULL,
    createtime       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatetime       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
comment on column permission.id is 'ID';
comment on column permission.application_name is '所属服务(为不同服务定制不同角色)';
comment on column permission.permission_name is '权限名称';
comment on column permission.weight is '权重，用来计算权限，可选用作扩展';
comment on column permission.description is '权限描述';
comment on column permission.createtime is '创建时间';
comment on column permission.updatetime is '更新时间';
COMMENT on table permission is '角色权限表';
create trigger auto_updatetime_permission before update on permission for each row execute procedure autoupdate_timestamp();-- auto update column updatetime


-- -----------------------------------------------------
-- Table role_permission
-- -----------------------------------------------------
DROP TABLE IF EXISTS role_permission cascade;

CREATE TABLE role_permission
(
    role_id       BIGINT    NOT NULL,
    permission_id BIGINT    NOT NULL,
    createtime    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatetime    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (role_id, permission_id),
    CONSTRAINT fk_role_permission_1 FOREIGN KEY (role_id) REFERENCES role (id),
    CONSTRAINT fk_role_permission_2 FOREIGN KEY (permission_id) REFERENCES permission (id)
);
comment on column role_permission.role_id is '角色id';
comment on column role_permission.permission_id is '权限id';
comment on column role_permission.createtime is '创建时间';
comment on column role_permission.updatetime is '更新时间';
COMMENT on table role_permission is '角色权限关联表';
CREATE INDEX fk_role_permission_2_idx ON role_permission (permission_id ASC);
create trigger auto_updatetime_role_permission before update on role_permission for each row execute procedure autoupdate_timestamp();-- auto update column updatetime


-- -----------------------------------------------------
-- Table account_role
-- -----------------------------------------------------
DROP TABLE IF EXISTS account_role cascade;

CREATE TABLE account_role
(
    account_id BIGINT    NOT NULL,
    role_id    BIGINT    NOT NULL,
    createtime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatetime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (account_id, role_id),
    CONSTRAINT fk_acount_role_1 FOREIGN KEY (account_id) REFERENCES account (id),
    CONSTRAINT fk_acount_role_2 FOREIGN KEY (role_id) REFERENCES role (id)
);
comment on column account_role.account_id is '账号ID';
comment on column account_role.role_id is '角色ID';
comment on column account_role.createtime is '创建时间';
comment on column account_role.updatetime is '更新时间';
COMMENT on table account_role is '账户权限表';
CREATE INDEX fk_acount_role_2_idx ON account_role (role_id ASC);
create trigger auto_updatetime_account_role before update on account_role for each row execute procedure autoupdate_timestamp();-- auto update column updatetime


-- -----------------------------------------------------
-- Table operation_log
-- -----------------------------------------------------
DROP TABLE IF EXISTS operation_log cascade;

CREATE TABLE operation_log
(
    id             BIGINT        NOT NULL,
    operater_type  VARCHAR(1)    NOT NULL,
    operater_id    BIGINT        NOT NULL,
    operater_name  VARCHAR(50)   NOT NULL,
    operate_type   VARCHAR(1)    NOT NULL,
    operate_detail VARCHAR(3000) NULL,
    createtime     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
comment on column operation_log.id is 'ID';
comment on column operation_log.operater_type is '操作人员类型\nA-系统管理员(一般为维护应用信息和角色关联信息)\nS-服务调用方(一般为创建用户)';
comment on column operation_log.operater_id is '账号';
comment on column operation_log.operater_name is '操作者名称';
comment on column operation_log.operate_type is '操作类型\nA-添加操作\nU-更新操作\nD-删除操作';
comment on column operation_log.operate_detail is '操作内容细节';
comment on column operation_log.createtime is '创建时间';
COMMENT on table operation_log is '系统操作日志';
CREATE INDEX operation_log_name_type ON operation_log (operater_name ASC, operate_type ASC);
create trigger auto_updatetime_operation_log before update on operation_log for each row execute procedure autoupdate_timestamp();-- auto update column updatetime


-- -----------------------------------------------------
-- Table application_code
-- -----------------------------------------------------
DROP TABLE IF EXISTS application_code cascade;

CREATE TABLE application_code
(
    application_name VARCHAR(50)  NOT NULL,
    application_code INT          NOT NULL,
    url              VARCHAR(200) NULL,
    description      VARCHAR(512) NULL,
    createtime       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatetime       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (application_name)
);
comment on column application_code.application_name is '应用名称';
comment on column application_code.application_code is '应用代码 10~99的数字';
comment on column application_code.url is '应用主页url';
comment on column application_code.description is '应用描述';
comment on column application_code.createtime is '创建时间';
comment on column application_code.updatetime is '更新时间';
COMMENT on table application_code is '应用代码';
create trigger auto_updatetime_application_code before update on application_code for each row execute procedure autoupdate_timestamp();-- auto update column updatetime


-- -----------------------------------------------------
-- Table application_sequence
-- -----------------------------------------------------
DROP TABLE IF EXISTS application_sequence cascade;

CREATE TABLE application_sequence
(
    application_name VARCHAR(50) NOT NULL,
    sequence_type    VARCHAR(20) NOT NULL,
    sequence         INT         NOT NULL,
    step             INT         NOT NULL DEFAULT 100,
    PRIMARY KEY (application_name, sequence_type)
);
comment on column application_sequence.application_name is '应用名称';
comment on column application_sequence.sequence_type is '序列类型';
comment on column application_sequence.sequence is '序列号';
comment on column application_sequence.step is '序列号步幅';
COMMENT on table application_sequence is '应用序列';
create trigger auto_updatetime_application_sequence before update on application_sequence for each row execute procedure autoupdate_timestamp();-- auto update column updatetime