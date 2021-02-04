package org.gonnaup.accountmanagement.validator;

import lombok.extern.slf4j.Slf4j;
import org.gonnaup.account.domain.AccountHeader;
import org.gonnaup.account.exception.LogicValidationException;
import org.gonnaup.accountmanagement.domain.JwtData;
import org.gonnaup.accountmanagement.enums.OperaterType;
import org.gonnaup.accountmanagement.service.AccountService;
import org.gonnaup.accountmanagement.service.ApplicationCodeService;
import org.gonnaup.accountmanagement.service.RolePermissionConfirmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ValidationException;

/**
 * 应用名验证器
 *
 * @author gonnaup
 * @version 2021/1/12 20:40
 * @see ApplicationNameAccessor
 */
@Slf4j
@Component
public class ApplicationNameValidator {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ApplicationCodeService applicationCodeService;

    @Autowired
    private RolePermissionConfirmService rolePermissionConfirmService;

    /**
     * 判断并设置appName
     * <ul>
     *      <li>判断账号是否是admin角色</li>
     *      <li>是：验证参数appName不能为空，{@link OperaterType} 为A</li>
     *      <li>否：设置参数appName为账号所属appName，{@link OperaterType} 为S</li>
     * </ul>
     *
     * @param jwtData
     * @param applicationNameAccessor
     * @return
     */
    public OperaterType judgeAndSetApplicationName(JwtData jwtData, ApplicationNameAccessor applicationNameAccessor) {
        if (log.isDebugEnabled()) {
            log.debug("开始对应用名进行设值");
        }
        Long accountId = jwtData.getAccountId();
        OperaterType operaterType = null;
        if (rolePermissionConfirmService.isAdmin(accountId)) {
            if (applicationCodeService.findByApplicationName(applicationNameAccessor.getApplicationName()) == null) {
                throw new ValidationException("请选择一个正确的应用名称");
            }
            operaterType = OperaterType.A;//管理员角色
            if (log.isDebugEnabled()) {
                log.debug("账户为ADMIN，取参数的应用名");
            }
        } else {
            //非admin
            if (log.isDebugEnabled()) {
                log.debug("账号非ADMIN，设置应用名为账号所属应用名");
            }
            applicationNameAccessor.setApplicationName(jwtData.getAppName());
            operaterType = OperaterType.S;
        }
        return operaterType;
    }

    /**
     * 判断账号是否有权操作对应app的数据
     * <ul>
     *     <li>判断账号是否是ADMIN</li>
     *     <li>是：有权操作，{@link OperaterType} 为A</li>
     *     <li>否：验证账号所属appName是否和参数appName一致。yes：{@link OperaterType} 为S，no：抛异常</li>
     * </ul>
     *
     * @param jwtData jwt信息
     * @param applicationNameAccessor 应用名访问器
     * @return
     */
    public OperaterType validateApplicationName(JwtData jwtData, ApplicationNameAccessor applicationNameAccessor) {
        OperaterType operaterType = null;
        if (log.isDebugEnabled()) {
            log.debug("开始对操作进行应用名对比");
        }
        Long accountId = jwtData.getAccountId();
        if (rolePermissionConfirmService.isAdmin(accountId)) {
            //admin
            if (applicationCodeService.findByApplicationName(applicationNameAccessor.getApplicationName()) == null) {
                throw new ValidationException("请选择一个正确的应用名称");
            }
            operaterType = OperaterType.A;//管理员角色
            if (log.isDebugEnabled()) {
                log.debug("账户为ADMIN，验证通过");
            }
        } else if (jwtData.getAppName().equals(applicationNameAccessor.getApplicationName())) {
            if (log.isDebugEnabled()) {
                log.debug("非AMDIN账号访问本应用数据，验证通过");
            }
            operaterType = OperaterType.S;//非admin
        } else {
            //非法访问其他应用数据
            AccountHeader accountHeader = accountService.findHeaderById(accountId);
            log.error("账号 {}[{}] 所属应用 {} 非法访问 应用 {} 的数据", accountId, accountHeader.getAccountName(), jwtData.getAppName(), applicationNameAccessor.getApplicationName());
            throw new LogicValidationException("您不能访问其他应用的数据!");
        }
        return operaterType;
    }

    /**
     * {@link #validateApplicationName(JwtData, ApplicationNameAccessor)}
     *
     * @param jwtData jwt信息
     * @param appName 数据属实应用名
     * @return
     */
    public OperaterType validateApplicationName(JwtData jwtData, String appName) {
        ApplicationNameAccessor applicationNameAccessor = new ApplicationNameAccessor() {
            @Override
            public String getApplicationName() {
                return appName;
            }

            @Override
            public void setApplicationName(String applicationName) {
                throw new UnsupportedOperationException();
            }
        };
        return validateApplicationName(jwtData, applicationNameAccessor);
    }

    /**
     *  根据账号角色设置参数应用名，用于表格数据展示接口<br/>
     *  非ADMIN将appName设置成账号所属appName
     * @param jwtData
     * @param applicationNameAccessor
     */
    public void putApplicationNameBaseonRole(JwtData jwtData, ApplicationNameAccessor applicationNameAccessor) {
        if (!rolePermissionConfirmService.isAdmin(jwtData.getAccountId())) {
            //非ADMIN，设置应用名为账号所属用户名
            applicationNameAccessor.setApplicationName(jwtData.getAppName());
        }
    }
}
