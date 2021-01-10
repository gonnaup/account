package org.gonnaup.accountmanagement.service.impl;

import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.gonnaup.account.domain.ApplicationSequenceKey;
import org.gonnaup.account.exception.DataNotInitialized;
import org.gonnaup.accountmanagement.dao.ApplicationSequenceDao;
import org.gonnaup.accountmanagement.entity.ApplicationCode;
import org.gonnaup.accountmanagement.entity.ApplicationSequence;
import org.gonnaup.accountmanagement.entity.ApplicationSequenceHeader;
import org.gonnaup.accountmanagement.service.ApplicationCodeService;
import org.gonnaup.accountmanagement.service.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * sequence队列填充
 * <p>
 * 带事务方法防止for update方法多线程时查询出重复值
 * </p>
 *
 * @author gonnaup
 * @version 1.0
 * @Created on 2020/11/11 22:44
 */
@Service
@Slf4j
public class SequenceServiceImpl implements SequenceService {
    @Autowired
    ApplicationSequenceDao applicationSequenceDao;

    @Autowired
    ApplicationCodeService applicationCodeService;

    /**
     * 应用编码缓存
     */
    private final LoadingCache<String, Integer> applicationCodeCache = CacheBuilder.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(30))//失效时间30'
            .build(CacheLoader.from(applicationName -> {
                ApplicationCode applicationCode = applicationCodeService.findByApplicationName(applicationName);
                if (applicationCode == null) {
                    throw new DataNotInitialized("应用代码未初始化");
                }
                log.info("应用代码初始化 {}", applicationCode);
                return applicationCode.getApplicationCode();
            }));

    //日期格式化器
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    //序列部最大值
    private static final int MAX_SEQUENCE = 99999999;

    /**
     * 填充sequence队列，带事务方法
     *
     * @param applicationSequenceKey 主键对象
     * @param queue                  要填充的队列
     * @throws DataNotInitialized 当应用编码或序列对象未初始化时抛出此异常
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW) //传播方式设置为REQUIRES_NEW，防止当嵌套在其他事务中回滚造成序列重复
    public void fillSequenceQueue(ApplicationSequenceKey applicationSequenceKey, BlockingQueue<Long> queue) throws DataNotInitialized {
        ApplicationSequenceHeader applicationSequenceHeader = applicationSequenceDao.queryByIdForUpdate(applicationSequenceKey.getApplicationName(), applicationSequenceKey.getSequenceType());
        int originsequence = applicationSequenceHeader.getSequence();
        int step = applicationSequenceHeader.getStep();
        //sequence位数溢出，则重新设置
        if (originsequence + step > MAX_SEQUENCE) {
            log.info("序列[{}] + 间隔[{}] 大于 最大序列号[{}], 重置序列号为0", originsequence, step, MAX_SEQUENCE);
            originsequence = 0;
        }
        final int sequence = originsequence;
        String date = dateFormatter.format(LocalDate.now()); // date 8位
        String appcode = Integer.toString(applicationCodeCache.getUnchecked(applicationSequenceKey.getApplicationName())); //appcode 2位
        List<Long> seqlist = IntStream.range(1, step + 1)
                .mapToLong(s -> {
                    int sq = sequence + s; //sequence 8位
                    String seq = Strings.padStart(Integer.toString(sq), 8, '0'); //填充左边
                    return Long.parseLong(appcode + date + seq); // ${appcode} + ${date} + ${sequence}
                })
                .boxed()
                .collect(Collectors.toList());
        log.info("填充序列队列 {}, sequence {}, step {}", applicationSequenceKey, sequence, step);
        queue.addAll(seqlist);
        applicationSequenceDao.updateSequence(ApplicationSequence.of(applicationSequenceKey, sequence + step, step));
    }

}
