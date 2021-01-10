package org.gonnaup.accountmanagement.service;

import org.gonnaup.accountmanagement.domain.Operater;
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
    ApplicationCode findByApplicationName(String applicationName);

    /**
     * 查询多条数据
     * @param applicationCode
     * @return 对象列表
     */
    List<ApplicationCode> findAllConditional(ApplicationCode applicationCode);

    /**
     * 新增数据
     *
     * @param applicationCode 实例对象
     * @param operater 操作者
     * @return 实例对象
     */
    ApplicationCode insert(ApplicationCode applicationCode, Operater operater);

    /**
     * 修改数据，只能更新code,url,description字段
     *
     * @param applicationCode 实例对象
     * @param operater 操作者
     * @return 实例对象
     */
    ApplicationCode update(ApplicationCode applicationCode, Operater operater);

    /**
     * 通过主键删除数据
     *
     * @param applicationName 主键
     * @param operater 操作者
     * @return 是否成功
     */
    ApplicationCode deleteOne(String applicationName, Operater operater);

}