package org.gonnaup.accountmanagement.enums;

/**
 * 操作类型枚举
 * @author gonnaup
 * @version 1.0
 * @Created on 2020/11/2 15:30
 */
public enum OperateType {

    A("添加"),
    U("更新"),
    D("删除");


    private final String description;

    private OperateType(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }

}
