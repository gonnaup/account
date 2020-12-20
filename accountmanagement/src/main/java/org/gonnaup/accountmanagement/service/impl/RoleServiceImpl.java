package org.gonnaup.accountmanagement.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gonnaup.account.exception.RelatedDataExistsException;
import org.gonnaup.accountmanagement.constant.AppSequenceKey;
import org.gonnaup.accountmanagement.dao.AccountRoleDao;
import org.gonnaup.accountmanagement.dao.RoleDao;
import org.gonnaup.accountmanagement.dao.RolePermissionDao;
import org.gonnaup.accountmanagement.domain.Operater;
import org.gonnaup.accountmanagement.entity.OperationLog;
import org.gonnaup.accountmanagement.entity.Role;
import org.gonnaup.accountmanagement.enums.OperateType;
import org.gonnaup.accountmanagement.service.ApplicationSequenceService;
import org.gonnaup.accountmanagement.service.OperationLogService;
import org.gonnaup.accountmanagement.service.RolePermissionService;
import org.gonnaup.accountmanagement.service.RoleService;
import org.gonnaup.common.domain.Page;
import org.gonnaup.common.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * 账户角色表(Role)表服务实现类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:30
 */
@Service("roleService")
@Slf4j
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;

    @Autowired
    private AccountRoleDao accountRoleDao;

    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Autowired
    private ApplicationSequenceService applicationSequenceService;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private RolePermissionService rolePermissionService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Role findById(Long id) {
        return this.roleDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param role     条件
     * @param pageable 分页条件
     * @return 对象页
     */
    @Override
    public Page<Role> findAllConditionalPaged(Role role, Pageable pageable) {
        role = Optional.ofNullable(role).orElse(new Role()); //为空时赋值为空对象
        List<Role> roleList = roleDao.queryAllConditionalByLimit(role, pageable.getOffset(), pageable.getSize());
        int total = roleDao.countAllConditional(role);
        return Page.of(roleList, total);
    }

    /**
     * 新增数据
     *
     * @param role     实例对象
     * @param operater 操作者
     * @return 实例对象
     */
    @Override
    @Transactional
    public Role insert(Role role, Operater operater) {
        role.setId(applicationSequenceService.produceSequence(AppSequenceKey.ROLE_PERMISSION));
        this.roleDao.insert(role);
        log.info("[{}] 新增角色 {}", operater.getOperaterId(), role);
        operationLogService.insert(OperationLog.of(operater, OperateType.A, "新增角色：" + role));
        return role;
    }


    /**
     * 修改数据
     *
     * @param role     实例对象
     * @param operater 操作者
     * @return 实例对象
     */
    @Override
    @Transactional
    public Role update(Role role, Operater operater) {
        Role origin = findById(role.getId());
        this.roleDao.update(role);
        Role after = findById(role.getId());
        log.info("[{}] 修改角色信息 {} => {}", operater.getOperaterId(), origin, after);
        operationLogService.insert(OperationLog.of(operater, OperateType.U, String.format("更新角色：%s => %s", origin, after)));
        return after;
    }

    /**
     * 通过主键删除数据
     *
     * @param id       主键
     * @param operater 操作者
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id, Operater operater) {
        //是否存在关联数据
        int relatedAccount = accountRoleDao.countRoleRelated(id);
        int relatedPermission = rolePermissionDao.countRoleRelated(id);
        StringJoiner joiner = new StringJoiner(",", "[", "]"); //拼接 [账户,权限]
        joiner.setEmptyValue("");//设置空值, 没有添加值则为"",不设置此值则为"[]"
        if (relatedAccount > 0) {
            joiner.add("账户");//账户关联
        }
        if (relatedPermission > 0) {
            joiner.add("权限");
        }
        String related = joiner.toString();
        if (!related.isEmpty()) {//至少有一种关联数据
            String message = "角色[id=" + id + "]和" + related + "存在关联数据，请先删除关联数据!";
            throw new RelatedDataExistsException(message);
        }
        //删除逻辑
        Role origin = findById(id);
        if (origin != null) {
            int count = roleDao.deleteById(id);
            log.info("[{}] 删除角色信息 {}", operater.getOperaterId(), origin);
            operationLogService.insert(OperationLog.of(operater, OperateType.D, "删除角色：" + origin));
            //删除关联的权限
            rolePermissionService.deleteByRoleId(id, operater);
            return count > 0;
        }
        return false;
    }
}