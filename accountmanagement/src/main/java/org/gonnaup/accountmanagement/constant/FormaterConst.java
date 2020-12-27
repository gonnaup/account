package org.gonnaup.accountmanagement.constant;

import java.time.format.DateTimeFormatter;

/**
 * 格式化常量
 *
 * @author gonnaup
 * @version 2020/12/27 14:49
 */
public class FormaterConst {

    private FormaterConst() {
    }

    //日期格式
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    //日期时间格式
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式化器
     */
    public static final DateTimeFormatter LOCALDATEFORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    /**
     * 日期时间格式化器
     */
    public static final DateTimeFormatter LOCALDATETIMEFORMATTER = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

}
