-- MySQL Script generated by MySQL Workbench
-- Thu Oct 29 10:50:45 2020
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Table `account`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `account` ;

CREATE TABLE IF NOT EXISTS `account` (
  `id` BIGINT NOT NULL COMMENT 'ID',
  `application_name` VARCHAR(50) NOT NULL COMMENT '应用名称',
  `account_name` VARCHAR(50) NOT NULL COMMENT '用户名，同一应用中唯一',
  `account_nickname` VARCHAR(50) NOT NULL COMMENT '昵称',
  `account_avatar` VARCHAR(256) NULL COMMENT '头像',
  `account_state` CHAR(1) NOT NULL DEFAULT 'N' COMMENT '账户状态\nN - 正常\nF - 禁用',
  `last_logintime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最近成功登陆时间，可用于清除僵尸账户',
  `tag` VARCHAR(255) NULL COMMENT '账户标记，管理员使用',
  `createtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updatetime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4 COLLATE utf8mb4_0900_as_cs
COMMENT = '账户信息';

CREATE UNIQUE INDEX `account_application_account_name` ON `account` (`application_name` ASC, `account_name` ASC);
CREATE INDEX `account_application_account_nickname` ON `account` (`application_name` ASC, `account_nickname` ASC);


-- -----------------------------------------------------
-- Table `authentication`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `authentication` ;

CREATE TABLE IF NOT EXISTS `authentication` (
  `id` BIGINT NOT NULL COMMENT 'ID',
  `account_id` BIGINT NOT NULL,
  `application_name` VARCHAR(50) NOT NULL COMMENT '应用名称',
  `auth_type` CHAR(1) NOT NULL COMMENT '认证类型\nP-密码\nE-邮箱\nW-微信\nQ-QQ\nB-微博',
  `identifier` VARCHAR(255) NOT NULL COMMENT '唯一标识(用户名，\n邮箱或第三方应用\n的唯一标识)',
  `credential` VARCHAR(255) NULL COMMENT '凭证(密码或第三方token)',
  `expires` BIGINT NULL COMMENT '过期时间，OAuth2登录时使用',
  `createtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updatetime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Authentication_1`
    FOREIGN KEY (`account_id`)
    REFERENCES `account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4 COLLATE utf8mb4_0900_as_cs
COMMENT = '账户认证信息';
CREATE UNIQUE INDEX `auth_unique_index` ON `authentication`(`application_name` ASC, `auth_type` ASC, `identifier` ASC);


-- -----------------------------------------------------
-- Table `role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `role` ;

CREATE TABLE IF NOT EXISTS `role` (
  `id` BIGINT NOT NULL COMMENT 'ID',
  `application_name` VARCHAR(50) NOT NULL COMMENT '所属服务(为不同服务定制不同角色)',
  `role_name` VARCHAR(64) NOT NULL COMMENT '角色名',
  `score` VARCHAR(30) NULL COMMENT '权限总分数用于判断是否有某种权限，可选用作扩展',
  `description` VARCHAR(512) NULL COMMENT '角色描述',
  `createtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updatetime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4 COLLATE utf8mb4_0900_as_cs
COMMENT = '账户角色表';


-- -----------------------------------------------------
-- Table `permission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `permission` ;

CREATE TABLE IF NOT EXISTS `permission` (
  `id` BIGINT NOT NULL COMMENT 'ID',
  `application_name` VARCHAR(50) NOT NULL COMMENT '所属服务(为不同服务定制不同角色)',
  `permission_name` VARCHAR(30) NOT NULL,
  `weight` VARCHAR(30) NULL COMMENT '权重，用来计算权限，可选用作扩展',
  `description` VARCHAR(512) NULL COMMENT '权限描述',
  `createtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updatetime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4 COLLATE utf8mb4_0900_as_cs
COMMENT = '角色权限表';


-- -----------------------------------------------------
-- Table `role_permission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `role_permission` ;

CREATE TABLE IF NOT EXISTS `role_permission` (
  `role_id` BIGINT NOT NULL COMMENT '角色id',
  `permission_id` BIGINT NOT NULL COMMENT '权限id',
  `createtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updatetime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`role_id`, `permission_id`),
  CONSTRAINT `fk_role_permission_1`
    FOREIGN KEY (`role_id`)
    REFERENCES `role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_role_permission_2`
    FOREIGN KEY (`permission_id`)
    REFERENCES `permission` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4 COLLATE utf8mb4_0900_as_cs
COMMENT = '角色权限关联表';

CREATE INDEX `fk_role_permission_2_idx` ON `role_permission` (`permission_id` ASC);


-- -----------------------------------------------------
-- Table `account_role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `account_role` ;

CREATE TABLE IF NOT EXISTS `account_role` (
  `account_id` BIGINT NOT NULL COMMENT 'ID',
  `role_id` BIGINT NOT NULL,
  `createtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updatetime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`account_id`, `role_id`),
  CONSTRAINT `fk_acount_role_1`
    FOREIGN KEY (`account_id`)
    REFERENCES `account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_acount_role_2`
    FOREIGN KEY (`role_id`)
    REFERENCES `role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4 COLLATE utf8mb4_0900_as_cs
COMMENT = '账户权限表';

CREATE INDEX `fk_acount_role_2_idx` ON `account_role` (`role_id` ASC);


-- -----------------------------------------------------
-- Table `operation_log`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `operation_log` ;

CREATE TABLE IF NOT EXISTS `operation_log` (
  `id` BIGINT NOT NULL COMMENT 'ID',
  `operater_type` VARCHAR(1) NOT NULL COMMENT '操作人员类型\nA-系统管理员(一般为维护应用信息和角色关联信息)\nS-服务调用方(一般为创建用户)',
  `operater_id` BIGINT NOT NULL COMMENT '账号',
  `operater_name` VARCHAR(50) NOT NULL COMMENT '操作者名称',
  `operate_type` VARCHAR(1) NOT NULL COMMENT '操作类型\nA-添加操作\nU-更新操作\nD-删除操作',
  `operate_detail` VARCHAR(3000) NULL COMMENT '操作内容细节',
  `createtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4 COLLATE utf8mb4_0900_as_cs
COMMENT = '系统操作日志';

CREATE INDEX `operation_log_name_type` ON `operation_log` (`operater_name` ASC, `operate_type` ASC);


-- -----------------------------------------------------
-- Table `application_code`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `application_code` ;

CREATE TABLE IF NOT EXISTS `application_code` (
  `application_name` VARCHAR(50) NOT NULL COMMENT '应用名称',
  `application_code` INT NOT NULL COMMENT '应用代码 10~99的数字',
  `url` VARCHAR(200) NULL  COMMENT '应用主页url',
  `description` VARCHAR(512) COMMENT '应用描述',
  `createtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updatetime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`application_name`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4 COLLATE utf8mb4_0900_as_cs
COMMENT = '应用代码';


-- -----------------------------------------------------
-- Table `application_sequence`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `application_sequence` ;

CREATE TABLE IF NOT EXISTS `application_sequence` (
  `application_name` VARCHAR(50) NOT NULL COMMENT '应用名称',
  `sequence_type` VARCHAR(20) NOT NULL COMMENT '序列类型',
  `sequence` INT NOT NULL COMMENT '序列号',
  `step` INT NOT NULL DEFAULT 100 COMMENT '序列号间隔',
  PRIMARY KEY (`application_name`, `sequence_type`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4 COLLATE utf8mb4_0900_as_cs
COMMENT = '应用序列';


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;