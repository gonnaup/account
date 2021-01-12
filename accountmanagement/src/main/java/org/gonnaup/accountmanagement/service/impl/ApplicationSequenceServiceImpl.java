package org.gonnaup.accountmanagement.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gonnaup.account.domain.ApplicationSequenceKey;
import org.gonnaup.account.exception.DataNotInitialized;
import org.gonnaup.accountmanagement.dao.ApplicationSequenceDao;
import org.gonnaup.accountmanagement.domain.Operater;
import org.gonnaup.accountmanagement.entity.ApplicationSequence;
import org.gonnaup.accountmanagement.entity.OperationLog;
import org.gonnaup.accountmanagement.enums.OperateType;
import org.gonnaup.accountmanagement.service.ApplicationSequenceService;
import org.gonnaup.accountmanagement.service.OperationLogService;
import org.gonnaup.accountmanagement.service.SequenceService;
import org.gonnaup.common.domain.Page;
import org.gonnaup.common.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 应用序列(ApplicationSequence)表服务实现类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:24
 */
@Service("applicationSequenceService")
@Slf4j
public class ApplicationSequenceServiceImpl implements ApplicationSequenceService {

    @Autowired
    private ApplicationSequenceDao applicationSequenceDao;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private SequenceService sequenceService;

    /**
     * 生成的序列
     */
    private final Map<ApplicationSequenceKey, LinkedBlockingQueue<Long>> cachedSequences = new ConcurrentHashMap<>(1);

    /**
     * 当某个序列号没有初始化时的互斥锁，多组序列号共用一个
     */
    private static final Object QUEUE_NOTINIT_MUTEX = new Object();

    /**
     * 当缓存序列数量小于此值，重新填充序列队列
     */
    private static final short lowestFactor = 5;

    /**
     * 生成新的序列号
     * <p>
     * <b>序列组成规则:</b>
     * <code>应用编码(2位) + 日期(8位) + 序列(8位)</code>
     * <p>序列大于8位时重新设置为0</p>
     * </p>
     *
     * @param applicationSequenceKey {@link ApplicationSequenceKey} 参数
     * @return 生成的序列号
     */
    @Override
    @SuppressWarnings("all")
    public long produceSequence(ApplicationSequenceKey applicationSequenceKey) throws DataNotInitialized {
        LinkedBlockingQueue<Long> queue = cachedSequences.get(applicationSequenceKey);
        //队列未初始化
        if (queue == null) {
            synchronized (QUEUE_NOTINIT_MUTEX) {
                if ((queue = cachedSequences.get(applicationSequenceKey)) == null) {//双重检测，防止重复填充
                    log.info("{} 队列未初始化，开始初始化...", applicationSequenceKey);
                    //初始化队列
                    cachedSequences.put(applicationSequenceKey, queue = new LinkedBlockingQueue<>());
                    //填充队列
                    sequenceService.fillSequenceQueue(applicationSequenceKey, queue);
                }
            }
            /**
             * 当队列中的序列数量少于此值时，重新填充队列，
             * 此值不设置为0的原因：防止当size为1，多线程取值时同时
             * 通过判断后只有一个线程取到值
             */
        } else if (queue.size() <= lowestFactor) {
            synchronized (queue) {//因为同一个key的队列对象是同一实例，多线程竞争这个key的队列使用权时使用这个queue实例对象作为锁即可
                if (queue.size() <= lowestFactor) { //双重检测，防止重复填充
                    if (log.isDebugEnabled()) {
                        log.debug("{} 队列填充", applicationSequenceKey);
                    }
                    //填充队列
                    sequenceService.fillSequenceQueue(applicationSequenceKey, queue);
                }
            }
        }
        return queue.poll();
    }

    /**
     * 根据主键查询
     *
     * @param applicationSequenceKey
     * @return 序列对象
     */
    @Override
    public ApplicationSequence findByKey(ApplicationSequenceKey applicationSequenceKey) {
        return applicationSequenceDao.queryById(applicationSequenceKey.getApplicationName(), applicationSequenceKey.getSequenceType());
    }

    /**
     * 分页查询
     *
     * @param applicationSequenceKey 条件对象
     * @param pageable               分页对象
     * @return 数据
     */
    @Override
    public Page<ApplicationSequence> findAllConditionalPaged(ApplicationSequenceKey applicationSequenceKey, Pageable pageable) {
        applicationSequenceKey = Optional.ofNullable(applicationSequenceKey).orElse(new ApplicationSequenceKey());
        List<ApplicationSequence> applicationSequenceList = applicationSequenceDao.queryAllConditionalByLimit(applicationSequenceKey, pageable.getOffset(), pageable.getSize());
        int total = applicationSequenceDao.countAllConditional(applicationSequenceKey);
        return Page.of(applicationSequenceList, total);
    }

    /**
     * 新增应用序列对象
     *
     * @param applicationSequence 序列对象
     * @param operater            操作者
     * @return 新增后的对象
     */
    @Override
    @Transactional
    public ApplicationSequence insert(ApplicationSequence applicationSequence, Operater operater) {
        applicationSequenceDao.insert(applicationSequence);
        log.info("[{}] 新增应用序列 {}", operater.getOperaterId(), applicationSequence);
        operationLogService.insert(OperationLog.of(operater, OperateType.A, applicationSequence.toString()));
        return applicationSequence;
    }

    /**
     * 更新应用序列
     *
     * @param applicationSequence 序列对象
     * @param operater            操作者
     * @return 序列对象
     */
    @Override
    @Transactional
    public ApplicationSequence update(ApplicationSequence applicationSequence, Operater operater) {
        applicationSequenceDao.updateStep(applicationSequence);
        log.info("[{}] 更新应用序列 {}", operater.getOperaterId(), applicationSequence);
        operationLogService.insert(OperationLog.of(operater, OperateType.U, "更新应用序列：" + applicationSequence.toString()));
        return applicationSequence;
    }

    /**
     * 删除一个应用序列
     *
     * @param applicationSequenceKey 主键对象
     * @param operater               操作者
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean deleteOne(ApplicationSequenceKey applicationSequenceKey, Operater operater) {
        int count = applicationSequenceDao.deleteById(applicationSequenceKey.getApplicationName(), applicationSequenceKey.getSequenceType());
        log.info("[{}] 删除应用序列 {}", operater.getOperaterId(), applicationSequenceKey);
        operationLogService.insert(OperationLog.of(operater, OperateType.D, "删除应用序列：" + applicationSequenceKey.toString()));
        return count > 0;
    }
}