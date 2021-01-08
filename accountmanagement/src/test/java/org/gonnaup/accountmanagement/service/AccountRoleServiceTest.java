package org.gonnaup.accountmanagement.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.gonnaup.account.domain.Account;
import org.gonnaup.account.domain.RoleTree;
import org.gonnaup.accountmanagement.constant.JsonUtil;
import org.gonnaup.accountmanagement.entity.AccountRole;
import org.gonnaup.accountmanagement.entity.Permission;
import org.gonnaup.accountmanagement.entity.Role;
import org.gonnaup.accountmanagement.entity.RolePermission;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.gonnaup.accountmanagement.constant.TestOperaters.ADMIN;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link AccountRoleService} 测试类
 *
 * @author gonnaup
 * @version 2020/12/21 20:03
 */
@Slf4j
@SpringBootTest
class AccountRoleServiceTest {

    @Autowired
    AccountRoleService accountRoleService;

    @Autowired
    AccountService accountService;

    @Autowired
    RoleService roleService;

    @Autowired
    PermissionService permissionService;

    @Autowired
    RolePermissionService rolePermissionService;

    @Autowired
    StringRedisTemplate redisTemplate;

    String CACHE_PREFIX = "redisCache::$";

    @Test
    @Transactional
    @Rollback
    @DisplayName("账号接口测试")
    void accountTest() {
        Account account = new Account();
        account.setAccountAvatar(null);
        account.setAccountName(RandomStringUtils.randomAlphanumeric(5, 20));
        account.setAccountNickname("nickname");
        account.setAccountState("N");
        account.setApplicationName("AccountManagement");
        account.setCreatetime(LocalDateTime.now());
        account.setUpdatetime(LocalDateTime.now());
        Long ID = accountService.insert(account).getId();
        accountService.findById(ID);//cache test
        Assertions.assertNotNull(accountService.findById(ID));
        Assertions.assertNotNull(accountService.findHeaderById(ID));
        accountService.deleteById(ID);
    }


    @Test
    @Transactional
    @DisplayName("账号角色接口测试")
    void accountRoleTest() {
        // 创建账号
        Account account = new Account();
        account.setAccountAvatar(null);
        account.setAccountName(RandomStringUtils.randomAlphanumeric(5, 20));
        account.setAccountNickname("nickname");
        account.setAccountState("N");
        account.setApplicationName("AccountManagement");
        account.setCreatetime(LocalDateTime.now());
        account.setUpdatetime(LocalDateTime.now());
        Long accountId = accountService.insert(account).getId();

        //创建角色
        Role role1 = new Role();
        role1.setApplicationName("AccountManagement");
        role1.setRoleName("admin");
        role1.setDescription("");
        Long roleId1 = roleService.insert(role1, ADMIN).getId();

        Role role2 = new Role();
        role2.setApplicationName("AccountManagement");
        role2.setRoleName("app");
        role2.setDescription("");
        Long roleId2 = roleService.insert(role2, ADMIN).getId();

        //创建账号权限
        AccountRole accountRole1 = new AccountRole();
        accountRole1.setAccountId(accountId);
        accountRole1.setRoleId(roleId1);

        AccountRole accountRole2 = new AccountRole();
        accountRole2.setAccountId(accountId);
        accountRole2.setRoleId(roleId2);

        //插入数据
        List<AccountRole> accountRoleList = List.of(accountRole1, accountRole2);
        accountRoleService.insertBatch(accountRoleList, ADMIN);

        //测试查询方法
        assertEquals(accountRoleService.findRolesByAccountId(accountId).size(), 2);

        //删除一个
        accountRoleService.deleteMany(accountId, List.of(roleId1), "AccountManagement", ADMIN);
        assertEquals(accountRoleService.findRolesByAccountId(accountId).size(), 1);

        //删除所有
        accountRoleService.deleteByAccountId(accountId, "AccountManagement", ADMIN);
        assertEquals(accountRoleService.findRolesByAccountId(accountId).size(), 0);

    }

    @Test
    @Transactional
    @SuppressWarnings("unchecked")
    @DisplayName("角色树缓存测试")
    void roleTreeCacheTest() throws JsonProcessingException {
        //================== 账户
        Account account1 = new Account();
        account1.setAccountAvatar(null);
        account1.setAccountName(RandomStringUtils.randomAlphanumeric(5, 20));
        account1.setAccountNickname("nickname");
        account1.setAccountState("N");
        account1.setApplicationName("AccountManagement");
        account1.setCreatetime(LocalDateTime.now());
        account1.setUpdatetime(LocalDateTime.now());
        Long accountId1 = accountService.insert(account1).getId();

        Account account2 = new Account();
        BeanUtils.copyProperties(account1, account2);
        account2.setAccountName(RandomStringUtils.randomAlphanumeric(5, 20));
        account2.setApplicationName("AM");
        Long accountId2 = accountService.insert(account2).getId();

        //========================== 角色
        Role role1 = new Role();
        role1.setApplicationName("AccountManagement");
        role1.setRoleName("admin");
        role1.setDescription("");
        Long roleId1 = roleService.insert(role1, ADMIN).getId();

        Role role2 = new Role();
        role2.setApplicationName("AccountManagement");
        role2.setRoleName("app");
        role2.setDescription("");
        Long roleId2 = roleService.insert(role2, ADMIN).getId();

        Role role3 = new Role();
        role3.setApplicationName("AM");
        role3.setRoleName("admin");
        role3.setDescription("");
        Long roleId3 = roleService.insert(role3, ADMIN).getId();

        //=========================  权限
        Permission permission1 = new Permission();
        permission1.setApplicationName("AccountManagement");
        permission1.setPermissionName("ADD");
        Long permissionId1 = permissionService.insert(permission1, ADMIN).getId();

        Permission permission2 = new Permission();
        permission2.setApplicationName("AccountManagement");
        permission2.setPermissionName("DEL");
        Long permissionId2 = permissionService.insert(permission2, ADMIN).getId();

        Permission permission3 = new Permission();
        permission3.setApplicationName("AM");
        permission3.setPermissionName("ALL");
        Long permissionId3 = permissionService.insert(permission3, ADMIN).getId();

        //===================== 角色权限
        RolePermission rolePermission11 = new RolePermission();
        rolePermission11.setRoleId(roleId1);
        rolePermission11.setPermissionId(permissionId1);
        RolePermission rolePermission12 = new RolePermission();
        rolePermission12.setRoleId(roleId1);
        rolePermission12.setPermissionId(permissionId2);

        RolePermission rolePermission21 = new RolePermission();
        rolePermission21.setRoleId(roleId2);
        rolePermission21.setPermissionId(permissionId1);
        RolePermission rolePermission22 = new RolePermission();
        rolePermission22.setRoleId(roleId2);
        rolePermission22.setPermissionId(permissionId2);

        RolePermission rolePermission33 = new RolePermission();
        rolePermission33.setRoleId(roleId3);
        rolePermission33.setPermissionId(permissionId3);

        rolePermissionService.insertBatch(List.of(rolePermission11, rolePermission12, rolePermission21, rolePermission22, rolePermission33), ADMIN);


        //====================== 账号权限
        AccountRole accountRole11 = new AccountRole();
        accountRole11.setAccountId(accountId1);
        accountRole11.setRoleId(roleId1);

        AccountRole accountRole12 = new AccountRole();
        accountRole12.setAccountId(accountId1);
        accountRole12.setRoleId(roleId2);

        AccountRole accountRole23 = new AccountRole();
        accountRole23.setAccountId(accountId2);
        accountRole23.setRoleId(roleId3);

        List<AccountRole> accountRoleList = List.of(accountRole11, accountRole12, accountRole23);
        accountRoleService.insertBatch(accountRoleList, ADMIN);

        //======================================= 查询 =======================================//
        List<RoleTree> roleTreesByAccountId1 = accountRoleService.findRoleTreesByAccountId(accountId1, account1.getApplicationName());
        log.info("账户1的角色树 {} => {}", roleTreesByAccountId1.size(), roleTreesByAccountId1);
        assertNotNull(redisTemplate.opsForValue().get(CACHE_PREFIX + "roleTree::" + account1.getApplicationName() + "$" + accountId1.toString()));

        List<RoleTree> roleTreesByAccountId2 = accountRoleService.findRoleTreesByAccountId(accountId2, account2.getApplicationName());
        log.info("账户1的角色树 {} => {}", roleTreesByAccountId2.size(), roleTreesByAccountId2);
        assertNotNull(redisTemplate.opsForValue().get(CACHE_PREFIX + "roleTree::" + account2.getApplicationName() + "$" + accountId2.toString()));

        //=================== 删除角色权限关联关系， 测试是否只清除本app的角色树缓存
        rolePermissionService.deleteByRoleId(roleId3, role3.getApplicationName(), ADMIN);
        assertEquals(0, Objects.requireNonNull(redisTemplate.keys(CACHE_PREFIX + "roleTree::" + role3.getApplicationName() + "*")).size());
        assertEquals(1, Objects.requireNonNull(redisTemplate.keys(CACHE_PREFIX + "roleTree::" + account1.getApplicationName() + "*")).size());

        //加载缓存
        accountRoleService.findRoleTreesByAccountId(accountId2, account2.getApplicationName());
        //================== 删除某账户的角色信息，测试是否只清除本账户的角色树缓存
        accountRoleService.deleteMany(accountId1, List.of(roleId1), account1.getApplicationName(), ADMIN);
        assertNull(redisTemplate.opsForValue().get(CACHE_PREFIX + "roleTree::" + account1.getApplicationName() + "$" + accountId1.toString()));
        assertEquals(1, Objects.requireNonNull((List<RoleTree>)JsonUtil.objectMapper.readValue(redisTemplate.opsForValue().get(CACHE_PREFIX + "roleTree::" + account2.getApplicationName() + "$" + accountId2.toString()), List.class)).size());


        accountRoleService.deleteByAccountId(accountId2, account2.getApplicationName(), ADMIN);
        assertNull(redisTemplate.opsForValue().get(CACHE_PREFIX + "roleTree::" + account2.getApplicationName() + "$" + accountId2.toString()));

    }

    @Test
    @Transactional
    @DisplayName("角色和权限接口及其缓存测试")
    void rolePermissionAndCacheTest() throws JsonProcessingException {
        String role_cachename = "role::";
        String permission_cachename = "permission::";
        //========================== 角色
        Role role1 = new Role();
        role1.setApplicationName("AccountManagement");
        role1.setRoleName("admin_");
        role1.setDescription("all");
        Long roleId1 = roleService.insert(role1, ADMIN).getId();

        //=========================  权限
        Permission permission1 = new Permission();
        permission1.setApplicationName("AccountManagement");
        permission1.setPermissionName("ADD_");
        permission1.setWeight("FFFFFFFF");
        permission1.setDescription("add");
        Long permissionId1 = permissionService.insert(permission1, ADMIN).getId();

        //角色测试
        Role byId = roleService.findById(roleId1);
        assertNotNull(byId);
        assertNotNull(redisTemplate.opsForValue().get(CACHE_PREFIX + role_cachename + byId.getId()));
        Role byAppAndRoleName = roleService.findByApplicationNameAndRoleName(role1.getApplicationName(), role1.getRoleName());
        assertNotNull(byAppAndRoleName);
        assertNotNull(redisTemplate.opsForValue().get(CACHE_PREFIX + role_cachename + "app&roleName" + byAppAndRoleName.getApplicationName() + "_" + byAppAndRoleName.getRoleName()));

        role1.setDescription("admin");
        roleService.update(role1, ADMIN);
        assertEquals(JsonUtil.objectMapper.readValue(redisTemplate.opsForValue().get(CACHE_PREFIX + role_cachename + byId.getId()), Role.class).getDescription(), "admin");
        assertEquals(JsonUtil.objectMapper.readValue(redisTemplate.opsForValue().get(CACHE_PREFIX + role_cachename +  "app&roleName" + role1.getApplicationName() + "_" + role1.getRoleName()), Role.class).getDescription(), "admin");

        roleService.deleteById(roleId1, ADMIN);
        assertNull(redisTemplate.opsForValue().get(CACHE_PREFIX + role_cachename + byId.getId()));
        assertNull(redisTemplate.opsForValue().get(CACHE_PREFIX + role_cachename + "app&roleName" + byAppAndRoleName.getApplicationName() + "_" + byAppAndRoleName.getRoleName()));
        assertNull(roleService.findById(roleId1));
        assertNull(roleService.findByApplicationNameAndRoleName(role1.getApplicationName(), role1.getRoleName()));

        //权限测试
        Permission byId1 = permissionService.findById(permissionId1);
        assertNotNull(byId1);
        assertNotNull(redisTemplate.opsForValue().get(CACHE_PREFIX + permission_cachename + byId1.getId()));
        Permission pByAppAndName = permissionService.findByApplicationNameAndPermissionName(byId1.getApplicationName(), byId1.getPermissionName());
        assertNotNull(pByAppAndName);
        assertNotNull(JsonUtil.objectMapper.readValue(redisTemplate.opsForValue().get(CACHE_PREFIX + permission_cachename + "app&permissionName" + pByAppAndName.getApplicationName() + "_" + pByAppAndName.getPermissionName()), Permission.class));

        permission1.setWeight("00000000");
        permissionService.update(permission1, ADMIN);
        assertEquals(JsonUtil.objectMapper.readValue(redisTemplate.opsForValue().get(CACHE_PREFIX + permission_cachename + byId1.getId()), Permission.class).getWeight(), "00000000");
        assertEquals(JsonUtil.objectMapper.readValue(redisTemplate.opsForValue().get(CACHE_PREFIX + permission_cachename +  "app&permissionName" + permission1.getApplicationName() + "_" + permission1.getPermissionName()), Permission.class).getWeight(), "00000000");

        permissionService.deleteById(permissionId1, ADMIN);
        assertNull(redisTemplate.opsForValue().get(CACHE_PREFIX + permission_cachename + byId1.getId()));
        assertNull(redisTemplate.opsForValue().get(CACHE_PREFIX + permission_cachename + "app&permissionName" + pByAppAndName.getApplicationName() + "_" + pByAppAndName.getPermissionName()));
        assertNull(roleService.findById(roleId1));
        assertNull(roleService.findByApplicationNameAndRoleName(role1.getApplicationName(), role1.getRoleName()));


    }


}