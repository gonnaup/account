package org.gonnaup.accountmanagement.enums;

/**
 * 系统权限枚举设计
 * <ul>
 *     <li>采用整数存储权限，整型数值十六进制分为8位数，每位代表一种权限，然后采用位运算进行权限判断</li>
 *     <li>鉴权方法：角色score: S；需要的权限weight：W。通过S | W = S?位运算判断是否有此权限</li>
 *     <li>添加角色时score计算方法：权限1：w1，权限2：w2... 权限n：wn，score = w1|w2|wn</li>
 *     <li>只读权限比较特殊，值为0X0000_000F，只需判断score > 0X0000_000F</li>
 * </ul>
 *
 * @author gonnaup
 * @version 2021/1/4 11:02
 */
public enum PermissionType {

    ALL(0X7FFF_FFFF, "系统所有权限"),
    APP_ALL(0x0FFF_FFFF, "应用级别所有权限"),
    APP_D(0X0000_F000, "应用删除权限"),
    APP_A(0X0000_0F00, "应用新增权限"),
    APP_U(0X0000_00F0, "应用修改权限"),
    APP_R(0X0000_000F, "应用只读权限");

    private final int weight;

    private final String description;

    private PermissionType(int weight, String description) {
        this.weight = weight;
        this.description = description;
    }

    public int weight() {
        return weight;
    }

    public String description() {
        return description;
    }
}
