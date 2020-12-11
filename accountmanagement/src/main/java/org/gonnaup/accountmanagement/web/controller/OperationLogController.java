package org.gonnaup.accountmanagement.web.controller;

import org.gonnaup.accountmanagement.dto.OperationLogQueryDTO;
import org.gonnaup.accountmanagement.entity.OperationLog;
import org.gonnaup.accountmanagement.service.OperationLogService;
import org.gonnaup.accountmanagement.vo.OperationLogVO;
import org.gonnaup.common.domain.Page;
import org.gonnaup.common.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**操作日志controller
 * @author hy
 * @version 1.0
 * @Created on 2020/12/4 11:37
 */
@RestController
@RequestMapping("/operationlog")
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    @GetMapping("/list")
    public Page<OperationLogVO> listpage(OperationLogQueryDTO queryparam, @RequestParam("page") Integer page, @RequestParam("limit") Integer size) {
        Page<OperationLog> pagedData = operationLogService.findAllConditionalPaged(OperationLog.fromDTO(queryparam), Pageable.of(page, size));
        //VO转换
        List<OperationLogVO> voList = pagedData.getData().stream().map(OperationLogVO::build).collect(Collectors.toList());
        return Page.of(voList, pagedData.getTotal());
    }

}
