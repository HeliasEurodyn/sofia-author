package com.crm.sofia.repository.dashboard;

import com.crm.sofia.dto.dashboard.DashboardDTO;
import com.crm.sofia.model.dashboard.Dashboard;
import com.crm.sofia.repository.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DashboardRepository extends BaseRepository<Dashboard> {
    @Query("SELECT new com.crm.sofia.dto.dashboard.DashboardDTO(d.id,d.description,d.createdOn) FROM Dashboard d ORDER BY d.modifiedOn DESC")
    List<DashboardDTO> getObject();
}
