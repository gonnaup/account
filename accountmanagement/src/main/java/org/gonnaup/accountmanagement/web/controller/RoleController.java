package org.gonnaup.accountmanagement.web.controller;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.gonnaup.account.domain.AccountHeader;
import org.gonnaup.account.domain.Permission;
import org.gonnaup.account.domain.Role;
import org.gonnaup.account.exception.LogicValidationException;
import org.gonnaup.account.exception.RelatedDataExistsException;
import org.gonnaup.accountmanagement.annotation.JwtDataParam;
import org.gonnaup.accountmanagement.annotation.RequireLogin;
import org.gonnaup.accountmanagement.annotation.RequirePermission;
import org.gonnaup.accountmanagement.constant.ResultConst;
import org.gonnaup.accountmanagement.constant.ValidateGroups;
import org.gonnaup.accountmanagement.domain.JwtData;
import org.gonnaup.accountmanagement.domain.Operater;
import org.gonnaup.accountmanagement.domain.SimpleBooleanShell;
import org.gonnaup.accountmanagement.dto.RoleDTO;
import org.gonnaup.accountmanagement.dto.RoleQueryDTO;
import org.gonnaup.accountmanagement.entity.RolePermission;
import org.gonnaup.accountmanagement.enums.OperaterType;
import org.gonnaup.accountmanagement.enums.PermissionType;
import org.gonnaup.accountmanagement.enums.ResultCode;
import org.gonnaup.accountmanagement.service.AccountService;
import org.gonnaup.accountmanagement.service.PermissionService;
import org.gonnaup.accountmanagement.service.RolePermissionService;
import org.gonnaup.accountmanagement.service.RoleService;
import org.gonnaup.accountmanagement.validator.ApplicationNameAccessor;
import org.gonnaup.accountmanagement.validator.ApplicationNameValidator;
import org.gonnaup.accountmanagement.validator.TemporaryApplicationNameAccessor;
import org.gonnaup.accountmanagement.vo.RoleVO;
import org.gonnaup.accountmanagement.vo.SelectVO;
import org.gonnaup.common.domain.Page;
import org.gonnaup.common.domain.Pageable;
import org.gonnaup.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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
    public Page<RoleVO> list(@JwtDataParam JwtData jwtData, @Validated RoleQueryDTO queryParam, @RequestParam("page") Integer page, @RequestParam("limit") Integer size) {
        //appName deal
        applicationNameValidator.putApplicationNameBaseonRole(jwtData, queryParam);
        Role role = queryParam.toRole();

        if (log.isDebugEnabled()) {
            log.debug("查询角色列表， 参数 {}，page：{}， size： {}", queryParam, page, size);
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
    public Result<Void> add(@JwtDataParam JwtData jwtData, @RequestBody @Validated(ValidateGroups.ADD.class) RoleDTO roleDTO) {
        //appName check
        OperaterType operaterType = applicationNameValidator.judgeAndSetApplicationName(jwtData, roleDTO);

        roleExistThrow(roleDTO.getApplicationName(), roleDTO.getRoleName());

        List<Long> permissionIdList = roleDTO.getPermissionIdList();
        String addObj_appName = roleDTO.getApplicationName();

        //permission appName check，确保所有权限Id有效并且其所属应用名和新增的角色应用名相同
        checkPermissionIdBelongApp(roleDTO, permissionIdList, addObj_appName);

        //account info
        Long accountId = jwtData.getAccountId();
        AccountHeader accountHeader = accountService.findHeaderById(accountId);

        //add
        Role role = roleDTO.toRole();
        Operater operater = Operater.of(operaterType, accountId, accountHeader.getAccountName());
        Long roleId = roleService.insert(role, operater).getId();//新增角色
        insertRolePermission(permissionIdList, operater, roleId);//新增权限关联关系
        return ResultConst.SUCCESS_NULL;
    }

    /**
     * 更新角色信息
     *
     * @param jwtData
     * @param roleDTO
     * @return
     */
    @PutMapping("/update")
    @RequirePermission(permissions = {PermissionType.APP_U})
    public Result<Void> update(@JwtDataParam JwtData jwtData, @RequestBody @Validated(ValidateGroups.UPDATE.class) RoleDTO roleDTO) {
        //appName deal
        OperaterType operaterType = applicationNameValidator.validateApplicationName(jwtData, roleDTO);

        List<Long> permissionIdList = roleDTO.getPermissionIdList();
        String appName = roleDTO.getApplicationName();

        //permission appName check，确保所有权限Id有效并且其所属应用名和新增的角色应用名相同
        checkPermissionIdBelongApp(roleDTO, permissionIdList, appName);

        //account info
        Long accountId = jwtData.getAccountId();
        AccountHeader accountHeader = accountService.findHeaderById(accountId);

        Operater operater = Operater.of(operaterType, accountId, accountHeader.getAccountName());
        Role role = roleDTO.toRole();

        //delete related
        Long roleId = role.getId();
        rolePermissionService.deleteByRoleId(roleId, roleDTO.getApplicationName(), operater);
        //update
        roleService.update(role, operater);
        //add related
        insertRolePermission(permissionIdList, operater, roleId);

        return ResultConst.SUCCESS_NULL;
    }


    @DeleteMapping("/delete/{roleId}")
    @RequirePermission(permissions = {PermissionType.APP_D})
    public Result<Void> delete(@JwtDataParam JwtData jwtData, @PathVariable("roleId") Long roleId) {
        Role origin = roleService.findById(roleId);
        if (origin == null) {
            log.error("要删除的角色Id={}不存在", roleId);
            throw new LogicValidationException("要删除的角色对象不存在");
        }
        //appName check
        OperaterType operaterType = applicationNameValidator.validateApplicationName(jwtData, origin.getApplicationName());
        Long accountId = jwtData.getAccountId();
        AccountHeader accountHeader = accountService.findHeaderById(accountId);
        Operater operater = Operater.of(operaterType, accountId, accountHeader.getAccountName());
        //delete related
        rolePermissionService.deleteByRoleId(roleId, origin.getApplicationName(), operater);
        //delete role
        roleService.deleteById(roleId, operater);
        return ResultConst.SUCCESS_NULL;
    }

    /**
     * 判断应用中某个名称的角色是否已经存在
     *
     * @param jwtData
     * @param appName
     * @param roleName
     * @return {@link SimpleBooleanShell} <code>true</code>已存在，<code>false</code>不存在
     * @see SimpleBooleanShell
     */
    @GetMapping("/exist/{appName}/{roleName}")
    @RequirePermission(permissions = {PermissionType.APP_R})
    public Result<SimpleBooleanShell> roleExist(@JwtDataParam JwtData jwtData, @PathVariable("appName") String appName, @PathVariable("roleName") String roleName) {
        //appName check
        applicationNameValidator.validateApplicationName(jwtData, appName);
        return Result.code(ResultCode.SUCCESS.code()).success().data(SimpleBooleanShell.of(roleService.findByApplicationNameAndRoleName(appName, roleName) != null));
    }

    /**
     * 计算权限列表的总权限分数
     *
     * @param permissionIdList
     * @return
     */
    @GetMapping("/calculatescore")
    @RequirePermission(permissions = PermissionType.APP_R)
    public Result<String> calculateScore(@RequestParam("permissionIdList") List<Long> permissionIdList) {
        final AtomicInteger score = new AtomicInteger(0);
        permissionIdList.forEach(pId -> {
                    Permission permission = permissionService.findById(pId);
                    if (permission != null) {
                        score.accumulateAndGet(Integer.parseInt(permission.getWeight(), 16), (left, right) -> left | right);
                    }
                }
        );
        return Result.code(ResultCode.SUCCESS.code()).success().data(Strings.padStart(Integer.toHexString(score.get()).toUpperCase(), 8, '0'));
    }

    /**
     * 获取某角色关联的权限ID列表
     * @param jwtData
     * @param roleId
     * @return
     */
    @GetMapping("/listPermissionIds/{roleId}")
    @RequirePermission(permissions = PermissionType.APP_R)
    public Result<List<String>> listPermissions(@JwtDataParam JwtData jwtData, @PathVariable("roleId") Long roleId) {
        Role role = roleService.findById(roleId);
        if (role == null) {
            throw new LogicValidationException("角色信息不存在");
        }
        //appName check
        applicationNameValidator.validateApplicationName(jwtData, role.getApplicationName());
        List<String> permissionIdList = rolePermissionService.findPermissionsByRoleId(roleId).stream()
                .map(permission -> Long.toString(permission.getId()))
                .collect(Collectors.toUnmodifiableList());
        return Result.code(ResultCode.SUCCESS.code()).success().data(permissionIdList);
    }

    /**
     * 获取某应用所有角色
     * @param jwtData
     * @param appName
     * @return
     */
    @GetMapping("/listAppAll")
    @RequirePermission(permissions = PermissionType.APP_R)
    public Result<List<SelectVO>> listAppAll(@JwtDataParam JwtData jwtData, @RequestParam(name = "applicationName", required = false) String appName) {
        ApplicationNameAccessor accessor = new TemporaryApplicationNameAccessor(appName);
        applicationNameValidator.judgeAndSetApplicationName(jwtData, accessor);
        return Result.code(ResultCode.SUCCESS.code()).success().data(
                roleService.findByAppName(accessor.getApplicationName()).stream()
                        .map(role -> SelectVO.of(Long.toString(role.getId()), role.getRoleName()))
                        .collect(Collectors.toUnmodifiableList())
        );
    }

    /**
     * 检查权限列表中是否有不属于角色所在应用的权限，并返回总权限分
     *
     * @param roleDTO
     * @param permissionIdList
     * @param addObj_appName
     */
    private void checkPermissionIdBelongApp(RoleDTO roleDTO, List<Long> permissionIdList, String addObj_appName) {
        if (CollectionUtils.isNotEmpty(permissionIdList) &&
                !permissionIdList.stream().allMatch(pId -> {
                            Permission permission = permissionService.findById(pId);
                            return permission != null && addObj_appName.equals(permission.getApplicationName());
                        }
                )
        ) {
            log.error("存在不属于角色 {} 所属应用 {} 中的权限信息 {}", roleDTO.toRole(), addObj_appName, roleDTO.getPermissionIdList());
            throw new LogicValidationException("存在不属于此角色应用的权限");
        }
    }

    /**
     * 检查角色是否存在，如果已存在则抛出异常
     *
     * @param appName
     * @param roleName
     * @throws RelatedDataExistsException 已存在抛出异常
     */
    private void roleExistThrow(String appName, String roleName) {
        if (roleService.findByApplicationNameAndRoleName(appName, roleName) != null) {
            throw new RelatedDataExistsException("角色信息已存在");
        }
    }

    /**
     * 新增角色权限关联关系
     *
     * @param permissionIdList
     * @param operater
     * @param roleId
     */
    private void insertRolePermission(List<Long> permissionIdList, Operater operater, Long roleId) {
        if (CollectionUtils.isNotEmpty(permissionIdList)) {
            List<RolePermission> rolePermissionList = permissionIdList.stream().map(permissionId -> {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(roleId);
                rolePermission.setPermissionId(permissionId);
                return rolePermission;
            }).collect(Collectors.toUnmodifiableList());
            rolePermissionService.insertBatch(rolePermissionList, operater);//新增和权限的关联关系
        }
    }

}
