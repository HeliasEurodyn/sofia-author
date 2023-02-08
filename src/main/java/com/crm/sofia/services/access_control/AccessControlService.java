package com.crm.sofia.services.access_control;

import com.crm.sofia.dto.access_control.AccessControlDTO;
import com.crm.sofia.dto.access_control.PrivilegeDTO;
import com.crm.sofia.dto.user.UserDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.access_control.PermissionMapper;
import com.crm.sofia.model.access_control.AccessControl;
import com.crm.sofia.model.user.Role;
import com.crm.sofia.model.user.User;
import com.crm.sofia.repository.access_control.AccessControlRepository;
import com.crm.sofia.repository.user.RoleRepository;
import com.crm.sofia.repository.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AccessControlService {


    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final AccessControlRepository accessControlRepository;

    private final PermissionMapper permissionMapper;


    public AccessControlService(UserRepository userRepository, RoleRepository roleRepository, AccessControlRepository accessControlRepository, PermissionMapper permissionMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.accessControlRepository = accessControlRepository;
        this.permissionMapper = permissionMapper;
    }

    public List<UserDTO> getUsersByRole(String roleId) {

        List<UserDTO> users = userRepository.findUsersByRoleId(roleId);
        return users;
    }

    public List<UserDTO> getAvailableUsers(String roleId) {

        Role role = roleRepository.findById(roleId).orElseThrow(() -> new DoesNotExistException("Role Does Not Exist"));
        List<UserDTO> users =userRepository.findUsersWithoutTheGivenRole(role);
        return users;
    }

    @Transactional
    public ResponseEntity<Map<String,String>> addUsersToRole(PrivilegeDTO privilegeDTO) {
        Role role = roleRepository.findById(privilegeDTO.getRoleId()).orElseThrow(() -> new DoesNotExistException("Role Does Not Exist"));
        List<User> users = userRepository.findUsersByIdIn(privilegeDTO.getUserIds());
        users.forEach(user -> user.addRole(role));

        Map<String,String> response = new HashMap<>();
        response.put("message","The users were added successfully to the " + role.getName() +  " role");
        return new ResponseEntity<>(response,HttpStatus.OK);

    }

    @Transactional
    public ResponseEntity<Map<String,String>>  removeUsersFromRole(PrivilegeDTO privilegeDTO) {
        Role role = roleRepository.findById(privilegeDTO.getRoleId()).orElseThrow(() -> new DoesNotExistException("Role Does Not Exist"));
        List<User> users =userRepository.findUsersByIdIn(privilegeDTO.getUserIds());
        users.forEach(user -> user.removeRole(role));

        Map<String,String> response = new HashMap<>();
        response.put("message","The users were removed successfully from the " + role.getName() +  " role");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    public List<AccessControlDTO> getPermisionList(){
        List<AccessControlDTO> permissionList = this.accessControlRepository.getAccessControlList();
        return permissionList;
    }

    @Transactional
    public void deleteAccessControl(String id) {
        AccessControl optionalEntity = accessControlRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("AccessControl Does Not Exist"));

        accessControlRepository.deleteById(optionalEntity.getId());
    }

    @Transactional
    public ResponseEntity<Map<String, String>> putPermissions(List<AccessControlDTO> accessControlList) {
         List<String> ids = accessControlList.stream().map(AccessControlDTO::getId).collect(Collectors.toList());
         List<AccessControl> accessControls = accessControlRepository.findAllById(ids);

         accessControls.stream().forEach(accessControl -> {
             accessControlList.stream().filter(accessControlDTO -> accessControlDTO.getId().equals(accessControl.getId())).forEach(accessControlDTO -> {
                 permissionMapper.map(accessControlDTO,accessControl);
             });
         });

        Map<String,String> response = new HashMap<>();
        response.put("message","The permissions have been updated successfully");
        return new ResponseEntity<>(response,HttpStatus.OK);

    }
}
