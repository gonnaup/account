package org.gonnaup.accountmanagement.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.gonnaup.account.domain.AccountHeader;
import org.gonnaup.account.exception.LogicValidationException;
import org.gonnaup.accountmanagement.annotation.JwtDataParam;
import org.gonnaup.accountmanagement.annotation.RequireLogin;
import org.gonnaup.accountmanagement.annotation.RequirePermission;
import org.gonnaup.accountmanagement.constant.ResultConst;
import org.gonnaup.accountmanagement.domain.JwtData;
import org.gonnaup.accountmanagement.domain.Operater;
import org.gonnaup.accountmanagement.domain.SimplePermission;
import org.gonnaup.accountmanagement.dto.ApplicationCodeDTO;
import org.gonnaup.accountmanagement.entity.ApplicationCode;
import org.gonnaup.accountmanagement.enums.OperaterType;
import org.gonnaup.accountmanagement.enums.PermissionType;
import org.gonnaup.accountmanagement.enums.ResultCode;
import org.gonnaup.accountmanagement.service.AccountService;
import org.gonnaup.accountmanagement.service.ApplicationCodeService;
import org.gonnaup.accountmanagement.service.RolePermissionConfirmService;
import org.gonnaup.accountmanagement.vo.SelectVO;
import org.gonnaup.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 应用编码{@link Applica} controller
 *
 * @author gonnaup
 * @version 2021/1/10 11:16
 */
@Slf4j
@RestController
@RequestMapping("/api/applicationCode")
@RequireLogin
public class ApplicationCodeController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ApplicationCodeService applicationCodeService;

    @Autowired
    private RolePermissionConfirmService rolePermissionConfirmService;

    /**
     * 页面认证api
     *
     * @return
     */
    @GetMapping("/display")
    @RequirePermission(permissions = {PermissionType.APP_R})
    public Result<Void> display() {
        return ResultConst.SUCCESS_NULL;
    }

    /**
     * 表格查询api
     *
     * @param jwtData
     * @param applicationCode
     * @return
     */
    @GetMapping("/list")
    @RequirePermission(permissions = {PermissionType.APP_R})
    public Result<List<ApplicationCode>> listConditional(@JwtDataParam JwtData jwtData, ApplicationCode applicationCode) {
        if (rolePermissionConfirmService.isAdmin(jwtData.getAccountId())) { //系统管理员权限返回所有数据
            List<ApplicationCode> applicationCodeList = applicationCodeService.findAllConditional(applicationCode);
            log.info("系统管理员 [{}] 查询所有应用编码共 {} 条数据", jwtData.getAccountId(), applicationCodeList.size());
            return Result.code(ResultCode.SUCCESS.code()).success().data(applicationCodeList);
        } else {//非系统管理员只返回本系统的数据
            String appName = jwtData.getAppName();
            ApplicationCode code = applicationCodeService.findByApplicationName(appName);
            log.info("用户 [{}] 查询应用 [{}] 的编码数据", jwtData.getAccountId(), appName);
            return Result.code(ResultCode.SUCCESS.code()).success().data(List.of(code));
        }
    }

    /**
     * 系统管理员用户获取所有应用编码简要信息，下拉框使用
     *
     * @return
     */
    @GetMapping("/listAll")
    @RequirePermission(permissions = {PermissionType.ALL})
    public Result<List<SelectVO>> listAll() {
        List<ApplicationCode> applicationCodeList = applicationCodeService.findAllConditional(null);
        List<SelectVO> selectVOList = applicationCodeList.stream().map(applicationCode -> SelectVO.of(applicationCode.getApplicationName(), applicationCode.getApplicationName())).collect(Collectors.toUnmodifiableList());
        log.info("查询应用编码简要信息 {} 条", selectVOList.size());
        return Result.code(ResultCode.SUCCESS.code()).success().data(selectVOList);
    }

    /**
     * 新增应用编码
     *
     * @param jwtData
     * @param applicationCode
     * @return
     */
    @PostMapping("/add")
    @RequirePermission(permissions = {PermissionType.ALL})//管理员权限
    public Result<Void> add(@JwtDataParam JwtData jwtData, @RequestBody @Validated ApplicationCodeDTO applicationCodeDTO) {
        ApplicationCode applicationCode = applicationCodeDTO.toApplicationCode();
        AccountHeader accountHeader = accountService.findHeaderById(jwtData.getAccountId());
        applicationCodeService.insert(applicationCode, Operater.of(OperaterType.A, accountHeader.getId(), accountHeader.getAccountName()));
        log.info("新增应用编码 [{}] 成功", applicationCode.getApplicationName());
        return ResultConst.SUCCESS_NULL;
    }

    /**
     * 更新应用编码
     *
     * @param jwtData
     * @param applicationCodeDTO
     * @return
     */
    @PutMapping("/update")
    @RequirePermission(permissions = {PermissionType.APP_U})
    public Result<Void> update(@JwtDataParam JwtData jwtData, @RequestBody @Validated ApplicationCodeDTO applicationCodeDTO) {
        Long accountId = jwtData.getAccountId();
        AccountHeader operaterAccount = accountService.findHeaderById(accountId);
        OperaterType operaterType = null;
        //操作者处理
        if (rolePermissionConfirmService.isAdmin(accountId)) {
            operaterType = OperaterType.A;
        } else if (Objects.equals(jwtData.getAppName(), applicationCodeDTO.getApplicationName())) {
            operaterType = OperaterType.S;
        } else {
            log.info("账号 {}[{}] 所属 {} 不能修改应用 {} 的应用序列信息", accountId, operaterAccount.getAccountName(), jwtData.getAppName(), applicationCodeDTO.getApplicationName());
            throw new LogicValidationException("您不能修改其他应用的应用编码信息");
        }
        ApplicationCode applicationCode = applicationCodeDTO.toApplicationCode();
        applicationCodeService.update(applicationCode, Operater.of(operaterType, operaterAccount.getId(), operaterAccount.getAccountName()));
        log.info("更新应用编码 [{}] 成功", applicationCode.getApplicationName());
        return ResultConst.SUCCESS_NULL;
    }

    @DeleteMapping("/delete/{applicationName}")
    @RequirePermission(permissions = {PermissionType.ALL})//系统管理员权限
    public Result<Void> delete(@JwtDataParam JwtData jwtData, @PathVariable("applicationName") String applicationName) {
        AccountHeader accountHeader = accountService.findHeaderById(jwtData.getAccountId());//处理账号
        applicationCodeService.deleteOne(applicationName, Operater.of(OperaterType.A, accountHeader.getId(), accountHeader.getAccountName()));
        return ResultConst.SUCCESS_NULL;
    }

    /**
     * 是否对应用编码有增删改权限<br/>
     * 系统管理员拥有所有权限；其他用户无增删权限，修改权限按权限分判断
     *
     * @param jwtData
     * @return
     */
    @GetMapping("/applicationCodePermission")
    public Result<SimplePermission> applicationCodePermissionResult(@JwtDataParam JwtData jwtData) {
        Long accountId = jwtData.getAccountId();
        if (rolePermissionConfirmService.isAdmin(accountId)) {//系统管理员
            if (log.isDebugEnabled()) {
                log.debug("系统管理员用户 [{}] 查询对应用编码的权限", accountId);
            }
            return Result.code(ResultCode.SUCCESS.code()).success().data(SimplePermission.of(true, true, true));
        } else {
            if (log.isDebugEnabled()) {
                log.debug("普通用户 [{}] 查询对应用编码的权限", accountId);
            }
            return Result.code(ResultCode.SUCCESS.code()).success().data(SimplePermission.of(false, false,
                    rolePermissionConfirmService.hasPermission(accountId, PermissionType.APP_U.weight())));
        }
    }

}
