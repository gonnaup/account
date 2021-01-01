package org.gonnaup.accountmanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * jwt包含数据
 *
 * @author gonnaup
 * @version 2020/12/22 20:58
 */
@Data
@AllArgsConstructor(staticName = "of")
public class JwtData {

    /**
     * 账号id
     */
    private final Long accountId;

    /**
     * 账号所属app名称
     */
    private final String appName;

    /**
     * jwt剩余时间
     */
    private final Long remainder;

}
