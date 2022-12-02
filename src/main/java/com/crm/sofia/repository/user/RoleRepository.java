package com.crm.sofia.repository.user;

import com.crm.sofia.dto.user.RoleDTO;
import com.crm.sofia.model.user.Role;
import com.crm.sofia.repository.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends BaseRepository<Role> {

    Role findFirstByName(String name);

    List<Role> findAllByOrderByModifiedOn();

    @Query("SELECT  new com.crm.sofia.dto.user.RoleDTO(r.id,r.name) FROM Role r ORDER BY r.modifiedOn DESC ")
    List<RoleDTO> getObject();
}
