package org.gonnaup.accountmanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 是否有增删改权限描述对象
 * @author gonnaup
 * @version 2021/1/9 16:20
 */
@Data
@AllArgsConstructor(staticName = "of")
public class SimplePermission {

    /**
     * 新增权限
     */
    private boolean add;

    /**
     * 删除权限
     */
    private boolean delete;

    /**
     * 修改权限
     */
    private boolean update;

}
