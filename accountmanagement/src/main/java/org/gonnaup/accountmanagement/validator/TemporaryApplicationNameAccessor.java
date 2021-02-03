package org.gonnaup.accountmanagement.validator;

/**
 * 临时的 {@link ApplicationNameAccessor}
 * @author gonnaup
 * @version 2021/2/3 11:17
 */
public class TemporaryApplicationNameAccessor implements ApplicationNameAccessor {

    private String appName;

    public TemporaryApplicationNameAccessor(String appName) {
        this.appName = appName;
    }

    @Override
    public String getApplicationName() {
        return appName;
    }

    @Override
    public void setApplicationName(String applicationName) {
        this.appName = applicationName;
    }
}
