package com.crm.sofia.services.dashboard;

import com.crm.sofia.dto.dashboard.DashboardAreaDTO;
import com.crm.sofia.dto.dashboard.DashboardDTO;
import com.crm.sofia.dto.dashboard.DashboardItemDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.dashboard.DashboardMapper;
import com.crm.sofia.model.dashboard.Dashboard;
import com.crm.sofia.repository.dashboard.DashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardDesignerService {
    @Autowired
    private  DashboardRepository dashboardRepository;
    @Autowired
    private  DashboardMapper dashboardMapper;


    public List<DashboardDTO> getObject() {
        List<DashboardDTO> dashboardsList = this.dashboardRepository.getObject();
        return dashboardsList;
    }

    public DashboardDTO getObject(String id) {
        Dashboard dashboardOptional = this.dashboardRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("Dashboard Does Not Exist"));


        DashboardDTO dashboardDTO = dashboardMapper.map(dashboardOptional);

        List<String> ids = new ArrayList<>();
        List<DashboardAreaDTO> dashboardAreaList = new ArrayList<>();
        for (DashboardAreaDTO area : dashboardDTO.getDashboardAreaList()) {
            if (!ids.contains(area.getId())) {
                ids.add(area.getId());
                dashboardAreaList.add(area);
            }
        }
        dashboardDTO.setDashboardAreaList(dashboardAreaList);

        dashboardDTO.getDashboardAreaList()
                .stream()
                .filter(x -> x.getShortOrder() == null)
                .forEach(x -> x.setShortOrder(0L));

        dashboardAreaList =
                dashboardDTO.getDashboardAreaList()
                        .stream()
                        .sorted(Comparator.comparingLong(DashboardAreaDTO::getShortOrder))
                        .collect(Collectors.toList());

        dashboardDTO.setDashboardAreaList(dashboardAreaList);

        dashboardDTO.getDashboardAreaList()
                .forEach(area -> {
                    area.getDashboardItemList().stream()
                            .filter(x -> x.getShortOrder() == null)
                            .forEach(x -> x.setShortOrder(0L));

                    List<DashboardItemDTO> dashboardItemList =
                            area.getDashboardItemList()
                                    .stream()
                                    .sorted(Comparator.comparingLong(DashboardItemDTO::getShortOrder))
                                    .collect(Collectors.toList());

                    area.setDashboardItemList(dashboardItemList);
                });

        return dashboardDTO;
    }

    @Transactional
    @Modifying
    public DashboardDTO postObject(DashboardDTO dto) {
        Dashboard dashboard = this.dashboardMapper.map(dto);
        Dashboard createdDashboard = this.dashboardRepository.save(dashboard);
        return this.dashboardMapper.map(createdDashboard);
    }

    public void deleteObject(String id) {
        Dashboard optionalDashboard = this.dashboardRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("Dashboard Does Not Exist"));

        this.dashboardRepository.deleteById(optionalDashboard.getId());
    }

}
