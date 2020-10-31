package org.gonnaup.accountmanagement.service;

import org.gonnaup.accountmanagement.entity.ApplicationSequence;

import java.util.List;

/**
 * 应用序列(ApplicationSequence)表服务接口
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:24
 */
public interface ApplicationSequenceService {

    /**
     * 通过ID查询单条数据
     *
     * @param applicationName 主键
     * @return 实例对象
     */
    ApplicationSequence queryById(String applicationName);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<ApplicationSequence> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param applicationSequence 实例对象
     * @return 实例对象
     */
    ApplicationSequence insert(ApplicationSequence applicationSequence);

    /**
     * 修改数据
     *
     * @param applicationSequence 实例对象
     * @return 实例对象
     */
    ApplicationSequence update(ApplicationSequence applicationSequence);

    /**
     * 通过主键删除数据
     *
     * @param applicationName 主键
     * @return 是否成功
     */
    boolean deleteById(String applicationName);

}