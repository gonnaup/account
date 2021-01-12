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
import org.gonnaup.accountmanagement.service.AccountService;
import org.gonnaup.accountmanagement.service.PermissionService;
import org.gonnaup.accountmanagement.service.RolePermissionConfirmService;
import org.gonnaup.accountmanagement.vo.PermissionVO;
import org.gonnaup.common.domain.Page;
import org.gonnaup.common.domain.Pageable;
import org.gonnaup.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
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
    private AccountService accountService;

    @Autowired
    private PermissionService permissionService;

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
        if (!rolePermissionConfirmService.isAdmin(jwtData.getAccountId())) {
            //非ADMIN，设置应用名为账号所属用户名
            queryParam.setApplicationName(jwtData.getAppName());
        }
        Permission permission = queryParam.toPermission();
        if (log.isDebugEnabled()) {
            log.debug("查询权限列表， 参数 {}，page：{}， size： {}", permission, page, size);
        }
        Page<Permission> paged = permissionService.findAllConditionalPaged(permission, Pageable.of(page, size));
        List<PermissionVO> permissionVOList = paged.getData().stream().map(PermissionVO::fromPermission).collect(Collectors.toUnmodifiableList());
        return Page.of(permissionVOList, paged.getTotal());
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
    public Result<Void> add(@JwtDataParam JwtData jwtData, @Validated PermissionDTO permissionDTO) {
        Long accountId = jwtData.getAccountId();
        OperaterType operaterType = null;
        if (rolePermissionConfirmService.isAdmin(accountId)) {
            operaterType = OperaterType.A;
        } else {//非admin
            operaterType = OperaterType.S;
            permissionDTO.setApplicationName(jwtData.getAppName());
        }
        AccountHeader accountHeader = accountService.findHeaderById(accountId);
        permissionService.insert(permissionDTO.toPermission(), Operater.of(operaterType, accountId, accountHeader.getAccountName()));
        return ResultConst.SUCCESS_NULL;
    }


    @PutMapping("/update")
    @RequirePermission(permissions = {PermissionType.APP_U})
    public Result<Void> update(@JwtDataParam JwtData jwtData, @Validated PermissionDTO permissionDTO) {
        Long accountId = jwtData.getAccountId();
        OperaterType operaterType;
        AccountHeader accountHeader = accountService.findHeaderById(accountId);
        if (rolePermissionConfirmService.isAdmin(accountId)) {
            operaterType = OperaterType.A;
        } else if (Objects.equals(jwtData.getAppName(), permissionDTO.getApplicationName())) {//非admin验证应用名是否一致
            operaterType = OperaterType.S;
        } else {
            log.info("账号 {}[{}] 所属 {} 不能修改应用 {} 的应用权限信息", accountId, accountHeader.getAccountName(), jwtData.getAppName(), permissionDTO.getApplicationName());
            throw new LogicValidationException("您不能修改其他应用的应用序列信息");
        }
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
        Long accountId = jwtData.getAccountId();
        OperaterType operaterType = null;
        AccountHeader accountHeader = accountService.findHeaderById(accountId);
        if (rolePermissionConfirmService.isAdmin(accountId)) {
            operaterType = OperaterType.A;
        } else if (Objects.equals(jwtData.getAppName(), origin.getApplicationName())) {//非admin验证应用名是否一致
            operaterType = OperaterType.S;
        } else {
            log.info("账号 {}[{}] 所属 {} 不能删除应用 {} 的应用权限信息", accountId, accountHeader.getAccountName(), jwtData.getAppName(), origin.getApplicationName());
            throw new LogicValidationException("您不能删除其他应用的应用序列信息");
        }
        permissionService.deleteById(permissionId, Operater.of(operaterType, accountId, accountHeader.getAccountName()));
        return ResultConst.SUCCESS_NULL;
    }


}
