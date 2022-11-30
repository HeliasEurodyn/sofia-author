package com.crm.sofia.repository.chart;

import com.crm.sofia.dto.chart.ChartDTO;
import com.crm.sofia.model.chart.Chart;
import com.crm.sofia.repository.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChartRepository extends BaseRepository<Chart> {
    @Query("SELECT new com.crm.sofia.dto.chart.ChartDTO(c.id,c.title,c.createdOn) FROM Chart c")
    List<ChartDTO> getObject();
}
