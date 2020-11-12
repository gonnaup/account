package org.gonnaup.accountmanagement.service;

import org.gonnaup.account.domain.ApplicationSequenceKey;
import org.gonnaup.account.exception.DataNotInitialized;

import java.util.concurrent.BlockingQueue;

/**
 * sequence队列填充
 * <p>
 * 带事务方法防止for update方法多线程时查询出重复值
 * </p>
 *
 * @author gonnaup
 * @version 1.0
 * @Created on 2020/11/11 22:43
 */
public interface SequenceService {

    /**
     * 填充sequence队列，带事务方法
     *
     * @param applicationSequenceKey 主键对象
     * @param queue                  要填充的队列
     * @throws DataNotInitialized 当应用编码或序列对象未初始化时抛出此异常
     */
    void fillSequenceQueue(ApplicationSequenceKey applicationSequenceKey, BlockingQueue<Long> queue) throws DataNotInitialized;
}
