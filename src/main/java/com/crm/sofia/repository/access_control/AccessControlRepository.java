package com.crm.sofia.repository.access_control;

import com.crm.sofia.dto.access_control.AccessControlDTO;
import com.crm.sofia.model.access_control.AccessControl;
import com.crm.sofia.repository.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessControlRepository extends BaseRepository<AccessControl> {

    @Query(nativeQuery = true)
    List<AccessControlDTO> getAccessControlList();

}
