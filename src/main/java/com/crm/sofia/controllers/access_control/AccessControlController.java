package com.crm.sofia.controllers.access_control;

import com.crm.sofia.dto.access_control.PrivilegeDTO;
import com.crm.sofia.dto.user.RoleDTO;
import com.crm.sofia.dto.user.UserDTO;
import com.crm.sofia.services.access_control.AccessControlService;
import com.crm.sofia.services.user.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/access_control")
public class AccessControlController {

    private final AccessControlService accessControlService;


    public AccessControlController(AccessControlService accessControlService) {
        this.accessControlService = accessControlService;
    }

    @GetMapping(path = "/by-role")
    List<UserDTO> getUsersByRole(@RequestParam("roleId") String roleId) {

        return this.accessControlService.getUsersByRole(roleId);
    }

    @GetMapping(path = "/available")
    List<UserDTO> getAvailableUsers(@RequestParam("roleId") String roleId) {

        return this.accessControlService.getAvailableUsers(roleId);
    }


    @PostMapping(path = "/add-users-to-role")
    ResponseEntity<?> addUsersToRole(@RequestBody PrivilegeDTO privilegeDTO) {

       return this.accessControlService.addUsersToRole(privilegeDTO);

    }

    @PostMapping(path = "/remove-users-from-role")
    ResponseEntity<?> removeUsersFromRole(@RequestBody  PrivilegeDTO privilegeDTO) {

      return  this.accessControlService.removeUsersFromRole(privilegeDTO);

    }


}
