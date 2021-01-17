package org.gonnaup.accountmanagement.util;

import org.gonnaup.account.domain.Role;
import org.gonnaup.accountmanagement.constant.ApplicationName;
import org.gonnaup.accountmanagement.dto.RoleDTO;
import org.junit.jupiter.api.RepeatedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author gonnaup
 * @version 2021/1/17 20:15
 */
class BeanFieldCopyUtilTest {

    @RepeatedTest(2)
    void fieldCopyUtilTest() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setApplicationName(ApplicationName.APPNAME);
        roleDTO.setRoleName("AAA");
        roleDTO.setDescription("BBB");
        roleDTO.setPermissionIdList(List.of("10101010", "2020202020"));
        Role role = new Role();
        BeanFieldCopyUtil.copyProperties(roleDTO, role);
        BeanFieldCopyUtil.copyProperties(roleDTO, role);
        BeanFieldCopyUtil.copyProperties(roleDTO, role);
        assertEquals(roleDTO.getApplicationName(), role.getApplicationName());
        assertEquals(role.getRoleName(), roleDTO.getRoleName());
        assertEquals(role.getDescription(), roleDTO.getDescription());
    }

}