package com.crm.sofia.controllers.access_control;

import com.crm.sofia.dto.access_control.AccessControlDTO;
import com.crm.sofia.dto.access_control.PrivilegeDTO;
import com.crm.sofia.dto.timeline.TimelineDTO;
import com.crm.sofia.dto.user.RoleDTO;
import com.crm.sofia.dto.user.UserDTO;
import com.crm.sofia.services.access_control.AccessControlService;
import com.crm.sofia.services.user.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping(path = "/permissions")
    List<AccessControlDTO> getPermissions() {

        return this.accessControlService.getPermisionList();
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

    @DeleteMapping
    public void deleteAccessControl(@RequestParam("id") String id) {accessControlService.deleteAccessControl(id);
    }

    @PutMapping
    public ResponseEntity<Map<String,String>> putPermissions(@RequestBody List<AccessControlDTO> accessControlList) {
        return  this.accessControlService.putPermissions(accessControlList);
    }


}
