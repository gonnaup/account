package org.gonnaup.accountmanagement.constant;

import org.gonnaup.account.domain.ApplicationSequenceKey;

/**
 * {@link ApplicationSequenceKey} 常量值
 * @author gonnaup
 * @version 1.0
 * @Created on 2020/11/10 16:06
 */
public abstract class AppSequenceKey {
    //用于账户id序列
    public static final ApplicationSequenceKey ACCOUNT = ApplicationSequenceKey.of(ApplicationName.APPNAME, "account");

    //用于认证信息序列
    public static final ApplicationSequenceKey AUTH = ApplicationSequenceKey.of(ApplicationName.APPNAME, "authentication");

    //角色和权限序列
    public static final ApplicationSequenceKey ROLE_PERMISSION = ApplicationSequenceKey.of(ApplicationName.APPNAME, "role_permission");

    //操作日志序列
    public static final ApplicationSequenceKey OPERATIONLOG = ApplicationSequenceKey.of(ApplicationName.APPNAME, "operationLog");

}
