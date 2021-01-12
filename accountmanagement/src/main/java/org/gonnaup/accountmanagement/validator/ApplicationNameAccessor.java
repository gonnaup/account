package org.gonnaup.accountmanagement.validator;

/**
 * 应用名访问接口，用于appName验证
 * @author gonnaup
 * @version 2021/1/12 20:37
 */
public interface ApplicationNameAccessor {

    /**
     * 获取appName
     * @return
     */
    String getApplicationName();

    /**
     * 设置appName
     */
    void setApplicationName(String applicationName);

}
