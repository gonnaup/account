package org.gonnaup.accountmanagement.web.controller;

import org.gonnaup.accountmanagement.annotation.RequireRole;
import org.gonnaup.accountmanagement.dto.OperationLogQueryDTO;
import org.gonnaup.accountmanagement.entity.OperationLog;
import org.gonnaup.accountmanagement.enums.ResultCode;
import org.gonnaup.accountmanagement.enums.RoleType;
import org.gonnaup.accountmanagement.service.OperationLogService;
import org.gonnaup.accountmanagement.vo.OperationLogVO;
import org.gonnaup.common.domain.Page;
import org.gonnaup.common.domain.Pageable;
import org.gonnaup.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 操作日志controller
 *
 * @author gonnaup
 * @version 1.0
 * @Created on 2020/12/4 11:37
 */
@RestController
@RequestMapping("/api/operationlog")
@RequireRole(RoleType.ADMIN)//ADMIN角色才能访问此controller
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 判断是否有权限显示此页面，使用鉴权拦截器实现，通过验证后直接返回成功
     *
     * @return
     */
    @GetMapping("/display")
    public Result<String> display() {
        return Result.code(ResultCode.SUCCESS.code()).success().data("");
    }

    @GetMapping("/list")
    public Page<OperationLogVO> listpage(OperationLogQueryDTO queryparam, @RequestParam("page") Integer page, @RequestParam("limit") Integer size) {
        Page<OperationLog> pagedData = operationLogService.findAllConditionalPaged(queryparam.toOperationLog(), Pageable.of(page, size));
        //VO转换
        List<OperationLogVO> voList = pagedData.getData().stream().map(OperationLogVO::build).collect(Collectors.toList());
        return Page.of(voList, pagedData.getTotal());
    }

}
