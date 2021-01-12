package org.gonnaup.accountmanagement.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.gonnaup.account.domain.Role;
import org.gonnaup.accountmanagement.annotation.JwtDataParam;
import org.gonnaup.accountmanagement.annotation.RequireLogin;
import org.gonnaup.accountmanagement.annotation.RequirePermission;
import org.gonnaup.accountmanagement.constant.ResultConst;
import org.gonnaup.accountmanagement.domain.JwtData;
import org.gonnaup.accountmanagement.dto.RoleDTO;
import org.gonnaup.accountmanagement.dto.RoleQueryDTO;
import org.gonnaup.accountmanagement.enums.PermissionType;
import org.gonnaup.accountmanagement.service.AccountService;
import org.gonnaup.accountmanagement.service.RolePermissionConfirmService;
import org.gonnaup.accountmanagement.service.RoleService;
import org.gonnaup.accountmanagement.vo.RoleVO;
import org.gonnaup.common.domain.Page;
import org.gonnaup.common.domain.Pageable;
import org.gonnaup.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public Result<Void> add(@JwtDataParam JwtData jwtData, @Validated RoleDTO roleDTO) {


        return ResultConst.SUCCESS_NULL;
    }


    @PutMapping("/update")
    @RequirePermission(permissions = {PermissionType.APP_U})
    public Result<Void> update(@JwtDataParam JwtData jwtData, @Validated RoleDTO roleDTO) {

        return ResultConst.SUCCESS_NULL;
    }


    @DeleteMapping("/delete/{roleId}")
    @RequirePermission(permissions = {PermissionType.APP_D})
    public Result<Void> delete(@JwtDataParam JwtData jwtData, @PathVariable("roleId") Long roleId) {

        return ResultConst.SUCCESS_NULL;
    }

}
