package org.gonnaup.accountmanagement.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.gonnaup.account.domain.AccountHeader;
import org.gonnaup.account.domain.Permission;
import org.gonnaup.account.exception.LogicValidationException;
import org.gonnaup.accountmanagement.annotation.JwtDataParam;
import org.gonnaup.accountmanagement.annotation.RequireLogin;
import org.gonnaup.accountmanagement.annotation.RequirePermission;
import org.gonnaup.accountmanagement.constant.ResultConst;
import org.gonnaup.accountmanagement.domain.JwtData;
import org.gonnaup.accountmanagement.domain.Operater;
import org.gonnaup.accountmanagement.dto.PermissionDTO;
import org.gonnaup.accountmanagement.dto.PermissionQueryDTO;
import org.gonnaup.accountmanagement.enums.OperaterType;
import org.gonnaup.accountmanagement.enums.PermissionType;
import org.gonnaup.accountmanagement.enums.ResultCode;
import org.gonnaup.accountmanagement.service.AccountService;
import org.gonnaup.accountmanagement.service.ApplicationCodeService;
import org.gonnaup.accountmanagement.service.PermissionService;
import org.gonnaup.accountmanagement.service.RolePermissionConfirmService;
import org.gonnaup.accountmanagement.validator.ApplicationNameValidator;
import org.gonnaup.accountmanagement.vo.PermissionVO;
import org.gonnaup.accountmanagement.vo.SelectVO;
import org.gonnaup.common.domain.Page;
import org.gonnaup.common.domain.Pageable;
import org.gonnaup.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 权限controller
 *
 * @author gonnaup
 * @version 2021/1/3 20:02
 */
@Slf4j
@RestController
@RequireLogin
@RequestMapping("/api/permission")
public class PermissionController {

    @Autowired
    private ApplicationCodeService applicationCodeService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PermissionService permissionService;

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
    public Page<PermissionVO> list(@JwtDataParam JwtData jwtData, PermissionQueryDTO queryParam, @RequestParam("page") Integer page, @RequestParam("limit") Integer size) {
        applicationNameValidator.putApplicationNameBaseonRole(jwtData, queryParam);
        Permission permission = queryParam.toPermission();
        if (log.isDebugEnabled()) {
            log.debug("查询权限列表， 参数 {}，page：{}， size： {}", permission, page, size);
        }
        Page<Permission> paged = permissionService.findAllConditionalPaged(permission, Pageable.of(page, size));
        List<PermissionVO> permissionVOList = paged.getData().stream().map(PermissionVO::fromPermission).collect(Collectors.toUnmodifiableList());
        return Page.of(permissionVOList, paged.getTotal());
    }

    /**
     * 查询某应用的所有的权限列表，前台下拉框使用
     *
     * @return
     */
    @GetMapping("/listAppAll")
    @RequirePermission(permissions = {PermissionType.APP_R})
    public Result<List<SelectVO>> listAppAll(@JwtDataParam JwtData jwtData, @RequestParam(name = "appName", required = false) String appName) {
        if (rolePermissionConfirmService.isAdmin(jwtData.getAccountId())) {
            if (applicationCodeService.findByApplicationName(appName) == null) {
                throw new ValidationException("请选择一个正确的应用名称");
            }
        } else {
            appName = jwtData.getAppName();
        }
        return Result.code(ResultCode.SUCCESS.code()).success().data(
                permissionService.findByAppName(appName).stream()
                        .map(permission -> SelectVO.of(Long.toString(permission.getId()), permission.getPermissionName()))
                        .collect(Collectors.toUnmodifiableList())
        );
    }

    /**
     * 展示权限的描述信息
     * @param jwtData
     * @param permissionId
     * @return
     */
    @GetMapping("/description/{permissionId}")
    @RequirePermission(permissions = {PermissionType.APP_R})
    public Result<String> description(@JwtDataParam JwtData jwtData, @PathVariable("permissionId") Long permissionId) {
        Permission permission = permissionService.findById(permissionId);
        if (permission == null) {
            log.error("权限 id = {} 不存在", permissionId);
            throw new LogicValidationException("权限信息不存在");
        }
        applicationNameValidator.validateApplicationName(jwtData, permission.getApplicationName());
        return Result.code(ResultCode.SUCCESS.code()).success().data(Optional.ofNullable(permission.getDescription()).orElse(""));
    }

    /**
     * 新增权限对象
     *
     * @param jwtData
     * @param permissionDTO
     * @return
     */
    @PostMapping("/add")
    @RequirePermission(permissions = {PermissionType.APP_A})
    public Result<Void> add(@JwtDataParam JwtData jwtData, @RequestBody @Validated PermissionDTO permissionDTO) {
        //appName deal
        OperaterType operaterType = applicationNameValidator.judgeAndSetApplicationName(jwtData, permissionDTO);
        Long accountId = jwtData.getAccountId();
        AccountHeader accountHeader = accountService.findHeaderById(accountId);
        permissionService.insert(permissionDTO.toPermission(), Operater.of(operaterType, accountId, accountHeader.getAccountName()));
        return ResultConst.SUCCESS_NULL;
    }


    @PutMapping("/update")
    @RequirePermission(permissions = {PermissionType.APP_U})
    public Result<Void> update(@JwtDataParam JwtData jwtData, @RequestBody @Validated PermissionDTO permissionDTO) {
        //appName deal
        OperaterType operaterType = applicationNameValidator.validateApplicationName(jwtData, permissionDTO);
        Long accountId = jwtData.getAccountId();
        AccountHeader accountHeader = accountService.findHeaderById(accountId);
        permissionService.update(permissionDTO.toPermission(), Operater.of(operaterType, accountId, accountHeader.getAccountName()));
        return ResultConst.SUCCESS_NULL;
    }


    @DeleteMapping("/delete/{permissionId}")
    @RequirePermission(permissions = {PermissionType.APP_D})
    public Result<Void> delete(@JwtDataParam JwtData jwtData, @PathVariable("permissionId") Long permissionId) {
        Permission origin = permissionService.findById(permissionId);
        if (origin == null) {
            throw new LogicValidationException("要删除的权限对象不存在");
        }
        //appName deal
        OperaterType operaterType = applicationNameValidator.validateApplicationName(jwtData, origin.getApplicationName());
        Long accountId = jwtData.getAccountId();
        AccountHeader accountHeader = accountService.findHeaderById(accountId);
        permissionService.deleteById(permissionId, Operater.of(operaterType, accountId, accountHeader.getAccountName()));
        return ResultConst.SUCCESS_NULL;
    }


}
