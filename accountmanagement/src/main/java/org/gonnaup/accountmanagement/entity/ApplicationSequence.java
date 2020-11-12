package org.gonnaup.accountmanagement.entity;

import lombok.Data;
import org.gonnaup.account.domain.ApplicationSequenceKey;

import java.io.Serializable;

/**
 * 应用序列(ApplicationSequence)实体类
 *
 * @author gonnaup
 * @see org.gonnaup.account.domain.ApplicationSequenceKey
 * @see org.gonnaup.accountmanagement.service.ApplicationSequenceService
 * @since 2020-10-29 10:53:24
 */
@Data
public class ApplicationSequence implements Serializable {
    private static final long serialVersionUID = -89012557809334620L;
    /**
     * 应用名称
     */
    private String applicationName;
    /**
     * 序列类型
     */
    private String sequenceType;
    /**
     * 序列号
     */
    private int sequence;
    /**
     * 序列号间隔
     */
    private int step;

    /**
     * 静态构建方法
     *
     * @param applicationSequenceKey
     * @param sequence
     * @param step
     * @return
     */
    public static ApplicationSequence of(ApplicationSequenceKey applicationSequenceKey, int sequence, int step) {
        ApplicationSequence applicationSequence = new ApplicationSequence();
        applicationSequence.setApplicationName(applicationSequenceKey.getApplicationName());
        applicationSequence.setSequenceType(applicationSequenceKey.getSequenceType());
        applicationSequence.setSequence(sequence);
        applicationSequence.setStep(step);
        return applicationSequence;
    }


}