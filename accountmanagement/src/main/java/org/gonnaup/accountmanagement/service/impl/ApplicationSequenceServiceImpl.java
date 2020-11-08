package org.gonnaup.accountmanagement.service.impl;

import org.gonnaup.accountmanagement.dao.ApplicationSequenceDao;
import org.gonnaup.accountmanagement.entity.ApplicationSequenceHeader;
import org.gonnaup.accountmanagement.service.ApplicationSequenceKey;
import org.gonnaup.accountmanagement.service.ApplicationSequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 应用序列(ApplicationSequence)表服务实现类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:24
 */
@Service("applicationSequenceService")
public class ApplicationSequenceServiceImpl implements ApplicationSequenceService {

    @Autowired
    private ApplicationSequenceDao applicationSequenceDao;

    /**
     * 生成的序列
     */
    private final Map<ApplicationSequenceKey, LinkedBlockingQueue<Long>> cachedSequences = new ConcurrentHashMap<>(1);

    /**
     * 生成新的序列号
     *
     * @param applicationSequenceKey {@link ApplicationSequenceKey} 参数
     * @return 生成的序列号
     */
    @Override
    @Transactional
    public long produceSequence(ApplicationSequenceKey applicationSequenceKey) {
        LinkedBlockingQueue<Long> queue = cachedSequences.get(applicationSequenceKey);
        if (queue.size() == 0) {

            //生成数据
        }

        return 0;
    }

    /**
     * 当对应序列队列为空时，填充序列队列
     * @param sequence 开始序列
     * @param step 步数
     */
    private void fillSequenceQueue(ApplicationSequenceKey applicationSequenceKey) {
        ApplicationSequenceHeader applicationSequenceHeader = applicationSequenceDao.queryByIdForUpdate(applicationSequenceKey.getApplicationName(), applicationSequenceKey.getSequenceType());
        int sequence = applicationSequenceHeader.getSequence();
        int step = applicationSequenceHeader.getStep();

    }



}