package org.gonnaup.accountmanagement.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.gonnaup.account.domain.AccountHeader;
import org.gonnaup.account.domain.Permission;
import org.gonnaup.account.domain.Role;
import org.gonnaup.account.exception.LogicValidationException;
import org.gonnaup.accountmanagement.annotation.JwtDataParam;
import org.gonnaup.accountmanagement.annotation.RequireLogin;
import org.gonnaup.accountmanagement.annotation.RequirePermission;
import org.gonnaup.accountmanagement.constant.ResultConst;
import org.gonnaup.accountmanagement.domain.JwtData;
import org.gonnaup.accountmanagement.domain.Operater;
import org.gonnaup.accountmanagement.dto.RoleDTO;
import org.gonnaup.accountmanagement.dto.RoleQueryDTO;
import org.gonnaup.accountmanagement.entity.RolePermission;
import org.gonnaup.accountmanagement.enums.OperaterType;
import org.gonnaup.accountmanagement.enums.PermissionType;
import org.gonnaup.accountmanagement.service.*;
import org.gonnaup.accountmanagement.vo.RoleVO;
import org.gonnaup.common.domain.Page;
import org.gonnaup.common.domain.Pageable;
import org.gonnaup.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色controller
 *
 * @author gonnaup
 * @version 2021/1/3 20:02
 */
@Slf4j
@RestController
@RequireLogin
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RolePermissionService rolePermissionService;

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
    public Page<RoleVO> list(@JwtDataParam JwtData jwtData, RoleQueryDTO queryParam, @RequestParam("page") Integer page, @RequestParam("limit") Integer size) {
        if (!rolePermissionConfirmService.isAdmin(jwtData.getAccountId())) {
            //非ADMIN，设置应用名为账号所属用户名
            queryParam.setApplicationName(jwtData.getAppName());
        }
        Role role = queryParam.toRole();
        if (log.isDebugEnabled()) {
            log.debug("查询权限列表， 参数 {}，page：{}， size： {}", role, page, size);
        }
        Page<Role> paged = roleService.findAllConditionalPaged(role, Pageable.of(page, size));
        List<RoleVO> roleVOList = paged.getData().stream().map(RoleVO::fromRole).collect(Collectors.toUnmodifiableList());
        return Page.of(roleVOList, paged.getTotal());
    }

    /**
     * 新增角色对象
     *
     * @param jwtData
     * @param roleDTO
     * @return
     */
    @PostMapping("/add")
    @RequirePermission(permissions = {PermissionType.APP_A})
    public Result<Void> add(@JwtDataParam JwtData jwtData, @RequestBody @Validated RoleDTO roleDTO) {
        //appName check
        Long accountId = jwtData.getAccountId();
        OperaterType operaterType;
        if (rolePermissionConfirmService.isAdmin(accountId)) {
            if (StringUtils.isBlank(roleDTO.getApplicationName())) {
                throw new ValidationException("请选择一个应用名称");
            }
            operaterType = OperaterType.A;
        } else {
            roleDTO.setApplicationName(jwtData.getAppName());
            operaterType = OperaterType.S;
        }
        List<String> permissionIdList = roleDTO.getPermissionIdList();
        String addObj_appName = roleDTO.getApplicationName();
        //permission appName check，确保所有权限Id有效并且其所属应用名和新增的角色应用名相同
        if (CollectionUtils.isNotEmpty(permissionIdList) && !permissionIdList.stream().allMatch(pId -> {
            Permission permission = permissionService.findById(Long.parseLong(pId));
            return permission != null && addObj_appName.equals(permission.getApplicationName());
        })) {
            log.error("存在不属于新增角色 {} 所属应用 {} 中的权限信息 {}", roleDTO.toRole(), addObj_appName, roleDTO.getPermissionIdList());
            throw new LogicValidationException("存在不属于此角色应用的权限");
        }
        AccountHeader accountHeader = accountService.findHeaderById(accountId);

        //add
        Role role = roleDTO.toRole();
        Operater operater = Operater.of(operaterType, accountId, accountHeader.getAccountName());
        Long roleId = roleService.insert(role, operater).getId();//新增角色
        if (CollectionUtils.isNotEmpty(permissionIdList)) {
            List<RolePermission> rolePermissionList = permissionIdList.stream().map(permissionId -> {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(roleId);
                rolePermission.setPermissionId(Long.parseLong(permissionId));
                return rolePermission;
            }).collect(Collectors.toUnmodifiableList());
            rolePermissionService.insertBatch(rolePermissionList, operater);//新增和权限的关联关系
        }
        return ResultConst.SUCCESS_NULL;
    }


    @PutMapping("/update")
    @RequirePermission(permissions = {PermissionType.APP_U})
    public Result<Void> update(@JwtDataParam JwtData jwtData, @RequestBody @Validated RoleDTO roleDTO) {

        return ResultConst.SUCCESS_NULL;
    }


    @DeleteMapping("/delete/{roleId}")
    @RequirePermission(permissions = {PermissionType.APP_D})
    public Result<Void> delete(@JwtDataParam JwtData jwtData, @PathVariable("roleId") Long roleId) {

        return ResultConst.SUCCESS_NULL;
    }

}
