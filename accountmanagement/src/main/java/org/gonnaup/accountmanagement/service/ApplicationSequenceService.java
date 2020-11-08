package org.gonnaup.accountmanagement.service;

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
    long produceSequence(ApplicationSequenceKey applicationSequenceKey);

}