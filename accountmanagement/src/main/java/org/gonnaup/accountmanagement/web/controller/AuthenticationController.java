package org.gonnaup.accountmanagement.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.gonnaup.account.domain.AccountHeader;
import org.gonnaup.account.domain.Authentication;
import org.gonnaup.account.exception.LogicValidationException;
import org.gonnaup.accountmanagement.annotation.JwtDataParam;
import org.gonnaup.accountmanagement.annotation.RequirePermission;
import org.gonnaup.accountmanagement.constant.ResultConst;
import org.gonnaup.accountmanagement.domain.JwtData;
import org.gonnaup.accountmanagement.dto.AuthenticationDTO;
import org.gonnaup.accountmanagement.dto.AuthenticationQueryDTO;
import org.gonnaup.accountmanagement.enums.PermissionType;
import org.gonnaup.accountmanagement.service.AccountService;
import org.gonnaup.accountmanagement.service.AuthenticationService;
import org.gonnaup.accountmanagement.validator.ApplicationNameValidator;
import org.gonnaup.accountmanagement.vo.AuthenticationVO;
import org.gonnaup.common.domain.Page;
import org.gonnaup.common.domain.Pageable;
import org.gonnaup.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 认证信息controller
 * @author gonnaup
 * @version 2021/1/17 21:15
 */
@Slf4j
@RestController
@RequestMapping("/api/authentication")
@RequirePermission(permissions = {PermissionType.APP_R})//应用管理员
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ApplicationNameValidator applicationNameValidator;

    /**
     * 判断是否有权限显示此页面，使用鉴权拦截器实现，通过验证后直接返回成功
     *
     * @return
     */
    @GetMapping("/display")
    public Result<Void> display() {
        return ResultConst.SUCCESS_NULL;
    }

    @GetMapping("/list")
    public Page<AuthenticationVO> list(@JwtDataParam JwtData jwtData, @Valid AuthenticationQueryDTO queryParam, @RequestParam("page") Integer page, @RequestParam("limit") Integer size) {
        //appName check
        applicationNameValidator.putApplicationNameBaseonRole(jwtData, queryParam);
        if (log.isDebugEnabled()) {
            AccountHeader accountHeader = accountService.findHeaderById(jwtData.getAccountId());
            log.debug("账户 {}[{}] 查询认证信息，参数 {}，page {}，size {}", jwtData.getAccountId(), accountHeader.getAccountName(), queryParam, page, size);
        }
        Page<Authentication> paged = authenticationService.findAllConditionalPaged(queryParam.toAuthentication(), Pageable.of(page, size));
        List<AuthenticationVO> authenticationVOList = paged.getData().stream().map(AuthenticationVO::fromAuthentication).collect(Collectors.toUnmodifiableList());
        return Page.of(authenticationVOList, paged.getTotal());
    }

    /**
     * 更新认证信息，只能修改认证标识和密码
     * @param jwtData
     * @param authentication
     * @return
     */
    @PutMapping("/update")
    public Result<Void> update(@JwtDataParam JwtData jwtData, @RequestBody @Valid AuthenticationDTO authenticationDTO) {
        //appName check
        Authentication origin = authenticationService.findById(Long.valueOf(authenticationDTO.getId()));
        if (origin == null) {
            log.error("传入的认证信息ID={}不存在!", authenticationDTO.getId());
            throw new LogicValidationException("要更新的认证信息不存在!");
        }
        applicationNameValidator.validateApplicationName(jwtData, origin.getApplicationName());
        Authentication authentication = authenticationDTO.toAuthentication();
        authenticationService.update(authentication);
        return ResultConst.SUCCESS_NULL;
    }


}
