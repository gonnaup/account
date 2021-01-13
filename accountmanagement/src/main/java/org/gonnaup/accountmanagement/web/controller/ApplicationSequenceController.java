package org.gonnaup.accountmanagement.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.gonnaup.account.domain.AccountHeader;
import org.gonnaup.account.domain.ApplicationSequenceKey;
import org.gonnaup.account.exception.RelatedDataExistsException;
import org.gonnaup.accountmanagement.annotation.JwtDataParam;
import org.gonnaup.accountmanagement.annotation.RequireLogin;
import org.gonnaup.accountmanagement.annotation.RequirePermission;
import org.gonnaup.accountmanagement.constant.ResultConst;
import org.gonnaup.accountmanagement.domain.JwtData;
import org.gonnaup.accountmanagement.domain.Operater;
import org.gonnaup.accountmanagement.domain.SimpleBooleanShell;
import org.gonnaup.accountmanagement.domain.SimplePermission;
import org.gonnaup.accountmanagement.dto.ApplicationSequenceDTO;
import org.gonnaup.accountmanagement.dto.ApplicationSequenceQueryDTO;
import org.gonnaup.accountmanagement.entity.ApplicationSequence;
import org.gonnaup.accountmanagement.enums.OperaterType;
import org.gonnaup.accountmanagement.enums.PermissionType;
import org.gonnaup.accountmanagement.enums.ResultCode;
import org.gonnaup.accountmanagement.service.AccountService;
import org.gonnaup.accountmanagement.service.ApplicationSequenceService;
import org.gonnaup.accountmanagement.service.RolePermissionConfirmService;
import org.gonnaup.accountmanagement.validator.ApplicationNameValidator;
import org.gonnaup.common.domain.Page;
import org.gonnaup.common.domain.Pageable;
import org.gonnaup.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 应用序列controller
 *
 * @author gonnaup
 * @version 2021/1/11 11:32
 */
@Slf4j
@RestController
@RequireLogin
public class ApplicationSequenceController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ApplicationSequenceService applicationSequenceService;

    @Autowired
    private RolePermissionConfirmService rolePermissionConfirmService;

    @Autowired
    private ApplicationNameValidator applicationNameValidator;

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
     * 分页查询
     *
     * @param jwtData
     * @param queryParam
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list")
    @RequirePermission(permissions = {PermissionType.APP_R})
    public Page<ApplicationSequence> list(@JwtDataParam JwtData jwtData, ApplicationSequenceQueryDTO queryParam, @RequestParam("page") Integer page, @RequestParam("limit") Integer size) {
        //appName deal
        applicationNameValidator.putApplicationNameBaseonRole(jwtData, queryParam);
        ApplicationSequenceKey applicationSequenceKey = queryParam.toApplicationSequenceKey();
        if (log.isDebugEnabled()) {
            log.debug("查询应用序列列表， 参数 {}，page：{}， size： {}", applicationSequenceKey, page, size);
        }
        return applicationSequenceService.findAllConditionalPaged(applicationSequenceKey, Pageable.of(page, size));
    }

    /**
     * 新增序列
     *
     * @param jwtData
     * @param applicationSequenceDTO
     * @return
     */
    @PostMapping("/add")
    @RequirePermission(permissions = {PermissionType.APP_A})
    public Result<Void> add(@JwtDataParam JwtData jwtData, @RequestBody @Validated ApplicationSequenceDTO applicationSequenceDTO) {
        //appName deal
        OperaterType operaterType = applicationNameValidator.judgeAndSetApplicationName(jwtData, applicationSequenceDTO);
        Long accountId = jwtData.getAccountId();
        AccountHeader accountHeader = accountService.findHeaderById(accountId);
        ApplicationSequence applicationSequence = applicationSequenceDTO.toApplicationSequence();
        applicationSequenceService.insert(applicationSequence, Operater.of(operaterType, accountId, accountHeader.getAccountName()));
        return ResultConst.SUCCESS_NULL;
    }

    /**
     * 更新序列Step
     *
     * @param jwtData
     * @param applicationSequenceDTO
     * @return
     */
    @PutMapping("/update")
    @RequirePermission(permissions = {PermissionType.APP_U})
    public Result<Void> update(@JwtDataParam JwtData jwtData, @RequestBody @Validated ApplicationSequenceDTO applicationSequenceDTO) {
        //appName deal
        OperaterType operaterType = applicationNameValidator.validateApplicationName(jwtData, applicationSequenceDTO);
        Long accountId = jwtData.getAccountId();
        AccountHeader accountHeader = accountService.findHeaderById(accountId);
        ApplicationSequence applicationSequence = applicationSequenceDTO.toApplicationSequence();
        applicationSequenceService.update(applicationSequence, Operater.of(operaterType, accountId, accountHeader.getAccountName()));
        return ResultConst.SUCCESS_NULL;
    }

    /**
     * 删除序列
     *
     * @param jwtData
     * @param appName
     * @param sequenceType
     * @return
     */
    @DeleteMapping("/delete/{application}/{sequenceType}")
    @RequirePermission(permissions = {PermissionType.APP_ALL})
    public Result<Void> delete(@JwtDataParam JwtData jwtData, @PathVariable("application") String appName, @PathVariable("sequenceType") String sequenceType) {
        //appName deal
        OperaterType operaterType = applicationNameValidator.validateApplicationName(jwtData, appName);
        Long accountId = jwtData.getAccountId();
        AccountHeader accountHeader = accountService.findHeaderById(accountId);
        ApplicationSequenceKey key = ApplicationSequenceKey.of(appName, sequenceType);
        applicationSequenceService.deleteOne(key, Operater.of(operaterType, accountId, accountHeader.getAccountName()));
        return ResultConst.SUCCESS_NULL;
    }

    /**
     * 是否对应用序列有增删改权限<br/>
     *
     * @param jwtData
     * @return
     */
    @GetMapping("/applicationSequencePermission")
    public Result<SimplePermission> applicationCodePermissionResult(@JwtDataParam JwtData jwtData) {
        if (jwtData == null) {//无权限
            return Result.code(ResultCode.SUCCESS.code()).success().data(SimplePermission.of(false, false, false));
        }
        Long accountId = jwtData.getAccountId();
        return Result.code(ResultCode.SUCCESS.code()).success()
                .data(SimplePermission.of(
                        rolePermissionConfirmService.hasPermission(accountId, PermissionType.APP_A.weight()),
                        rolePermissionConfirmService.hasPermission(accountId, PermissionType.APP_ALL.weight()),
                        rolePermissionConfirmService.hasPermission(accountId, PermissionType.APP_U.weight())
                ));
    }

    /**
     * 判断应用中某个序列是否已经存在
     * @param jwtData
     * @param sequenceQueryDTO
     * @return {@link SimpleBooleanShell} <code>true</code>已存在，<code>false</code>不存在
     * @see SimpleBooleanShell
     */
    @GetMapping("/exist")
    @RequirePermission(permissions = {PermissionType.APP_R})
    public Result<SimpleBooleanShell> applicationSequenceExist(@JwtDataParam JwtData jwtData, ApplicationSequenceQueryDTO sequenceQueryDTO) {
        //appName check
        applicationNameValidator.validateApplicationName(jwtData, sequenceQueryDTO.getApplicationName());
        return Result.code(ResultCode.SUCCESS.code()).success().data(SimpleBooleanShell.of(applicationSequenceService.findByKey(sequenceQueryDTO.toApplicationSequenceKey()) != null));
    }


    /**
     * 检查应用序列是否存在，如果已存在则抛出异常
     *
     * @param key
     * @throws RelatedDataExistsException 已存在抛出异常
     */
    private void roleExistThrow(ApplicationSequenceKey key) {
        if (applicationSequenceService.findByKey(key) != null) {
            throw new RelatedDataExistsException("应用序列已存在已存在");
        }
    }

}
