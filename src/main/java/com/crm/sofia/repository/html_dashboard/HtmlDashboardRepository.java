package com.crm.sofia.repository.html_dashboard;

import com.crm.sofia.dto.html_dashboard.HtmlDashboardDTO;
import com.crm.sofia.model.html_dashboard.HtmlDashboard;
import com.crm.sofia.repository.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HtmlDashboardRepository extends BaseRepository<HtmlDashboard> {
    @Query("SELECT new com.crm.sofia.dto.html_dashboard.HtmlDashboardDTO(h.id,h.code,h.name) FROM HtmlDashboard h ORDER BY h.modifiedOn DESC")
    List<HtmlDashboardDTO> getObject();
}
