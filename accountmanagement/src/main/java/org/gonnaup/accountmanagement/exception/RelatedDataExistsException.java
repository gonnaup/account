package org.gonnaup.accountmanagement.exception;

/**
 * 存在关联数据异常
 * 当删除含关联外键数据时抛出此异常
 * @author gonnaup
 * @version 1.0
 * @Created on 2020/11/15 21:47
 */
public class RelatedDataExistsException extends RuntimeException {

    private static final long serialVersionUID = 6891195479535161680L;

    public RelatedDataExistsException(String message) {
        super(message);
    }

}
