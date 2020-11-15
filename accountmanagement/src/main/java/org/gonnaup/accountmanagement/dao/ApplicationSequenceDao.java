package org.gonnaup.accountmanagement.dao;

import org.apache.ibatis.annotations.Param;
import org.gonnaup.account.domain.ApplicationSequenceKey;
import org.gonnaup.accountmanagement.entity.ApplicationSequence;
import org.gonnaup.accountmanagement.entity.ApplicationSequenceHeader;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 应用序列(ApplicationSequence)表数据库访问层
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:24
 */
@Repository
public interface ApplicationSequenceDao {

    /**
     * 通过ID查询单条数据
     *
     * @param applicationName 应用名称
     * @Param sequenceType 序列类型
     * @return 实例对象
     */
    ApplicationSequenceHeader queryByIdForUpdate(@Param("applicationName") String applicationName, @Param("sequenceType") String sequenceType);

    /**
     * 修改sequence列
     *
     * @param applicationSequence 实例对象
     * @return 影响行数
     */
    int updateSequence(ApplicationSequence applicationSequence);

    /**
     * 修改step列
     * @param applicationSequence
     * @return
     */
    int updateStep(ApplicationSequence applicationSequence);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param applicationSequence 实例对象
     * @return 对象列表
     */
    List<ApplicationSequence> queryAllConditionalByLimit(@Param("applicationSequenceKey") ApplicationSequenceKey applicationSequenceKey, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 查询符合条件的总记录数
     * @param applicationSequence
     * @return 总记录数
     */
    int countAllConditional(@Param("applicationSequenceKey") ApplicationSequenceKey applicationSequenceKey);

    /**
     * 新增数据
     *
     * @param applicationSequence 实例对象
     * @return 影响行数
     */
    int insert(ApplicationSequence applicationSequence);

    /**
     * 通过主键删除数据
     *
     * @param applicationName 应用名称
     * @param sequenceType 序列类型
     * @return 影响行数
     */
    int deleteById(@Param("applicationName") String applicationName, @Param("sequenceType") String sequenceType);

}