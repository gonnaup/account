package org.gonnaup.accountmanagement.service;

import org.gonnaup.account.domain.ApplicationSequenceKey;
import org.gonnaup.account.exception.DataNotInitialized;
import org.gonnaup.accountmanagement.domain.Operater;
import org.gonnaup.accountmanagement.entity.ApplicationSequence;
import org.gonnaup.common.domain.Page;
import org.gonnaup.common.domain.Pageable;

/**
 * 应用序列(ApplicationSequence)表服务接口
 * 参数{@link ApplicationSequenceKey} 对应的初始化数据应当事先初始化(使用应用管理账号添加)
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:24
 */
public interface ApplicationSequenceService {

    /**
     * 生成新的序列号
     * @param applicationSequenceKey {@link ApplicationSequenceKey} 参数
     * @return 生成的序列号
     */
    long produceSequence(ApplicationSequenceKey applicationSequenceKey) throws DataNotInitialized;

    /**
     * 分页查询
     * @param applicationSequenceKey 条件对象
     * @param pageable 分页对象
     * @return 数据
     */
    Page<ApplicationSequence> findAllConditionalPaged(ApplicationSequenceKey applicationSequenceKey, Pageable pageable);

    /**
     * 新增应用序列对象
     * @param applicationSequence 序列对象
     * @param operater 操作者
     * @return 新增后的对象
     */
    ApplicationSequence insert(ApplicationSequence applicationSequence, Operater operater);

    /**
     * 更新应用序列
     * @param applicationSequence 序列对象
     * @param operater 操作者
     * @return 序列对象
     */
    ApplicationSequence update(ApplicationSequence applicationSequence, Operater operater);

    /**
     * 删除一个应用序列
     * @param applicationSequenceKey 主键对象
     * @param operater 操作者
     * @return 是否成功
     */
    boolean deleteOne(ApplicationSequenceKey applicationSequenceKey, Operater operater);

}