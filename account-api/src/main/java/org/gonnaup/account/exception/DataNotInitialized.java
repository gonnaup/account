package org.gonnaup.account.exception;

/**
 * 数据未被初始化
 * @author gonnaup
 * @version 1.0
 * @Created on 2020/11/8 10:50
 */
public class DataNotInitialized extends RuntimeException {

    private static final long serialVersionUID = -7040701251865500072L;

    public DataNotInitialized() {}

    public DataNotInitialized(String message) {
        super(message);
    }

}
