package org.gonnaup.accountmanagement.service;

import org.gonnaup.accountmanagement.entity.ApplicationCode;

import java.util.List;

/**
 * 应用代码(ApplicationCode)表服务接口
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:22
 */
public interface ApplicationCodeService {

    /**
     * 通过ID查询单条数据
     *
     * @param applicationName 主键
     * @return 实例对象
     */
    ApplicationCode queryById(String applicationName);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<ApplicationCode> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param applicationCode 实例对象
     * @return 实例对象
     */
    ApplicationCode insert(ApplicationCode applicationCode);

    /**
     * 修改数据
     *
     * @param applicationCode 实例对象
     * @return 实例对象
     */
    ApplicationCode update(ApplicationCode applicationCode);

    /**
     * 通过主键删除数据
     *
     * @param applicationName 主键
     * @return 是否成功
     */
    boolean deleteById(String applicationName);

}