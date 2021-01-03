package org.gonnaup.accountmanagement.constant;

import org.gonnaup.accountmanagement.enums.ResultCode;
import org.gonnaup.common.domain.Result;

/**
 * {@link Result} 实例
 *
 * @author gonnaup
 * @version 2021/1/3 20:31
 */
public class ResultConst {

    private ResultConst() {
    }

    /**
     * {@link Result} 无返回数据的成功对象
     */
    public static final Result<Void> SUCCESS_NULL = Result.code(ResultCode.SUCCESS.code()).success().data(null);

    /**
     * {@link Result} 无返回数据的失败对象
     */
    public static final Result<Void> FAIL_NULL = Result.code(ResultCode.FAIL.code()).fail().data(null);

}
