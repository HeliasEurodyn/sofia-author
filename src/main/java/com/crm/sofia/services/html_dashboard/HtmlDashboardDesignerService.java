package com.crm.sofia.services.html_dashboard;


import com.crm.sofia.dto.html_dashboard.HtmlDashboardDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.html_dashboard.HtmlDashboardMapper;
import com.crm.sofia.model.html_dashboard.HtmlDashboard;
import com.crm.sofia.repository.html_dashboard.HtmlDashboardRepository;
import com.crm.sofia.services.auth.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class HtmlDashboardDesignerService {
    @Autowired
    private  HtmlDashboardMapper htmlDashboardMapper;
    @Autowired
    private  HtmlDashboardRepository htmlDashboardRepository;
    @Autowired
    private  JWTService jwtService;


    public List<HtmlDashboardDTO> getObject() {
        List<HtmlDashboardDTO> htmlDashboardList = htmlDashboardRepository.getObject();
        return htmlDashboardList;
    }

    public HtmlDashboardDTO getObject(String id) {
        HtmlDashboard optionalEntity = htmlDashboardRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("HtmlDashboard Does Not Exist"));

        HtmlDashboard entity = optionalEntity;
        HtmlDashboardDTO dto = htmlDashboardMapper.map(entity);
        return dto;
    }

    @Transactional
    public HtmlDashboardDTO postObject(HtmlDashboardDTO htmlDashboardDto) {

        HtmlDashboard htmlDashboard = htmlDashboardMapper.map(htmlDashboardDto);
        if (htmlDashboardDto.getId() == null) {
            htmlDashboard.setCreatedOn(Instant.now());
            htmlDashboard.setCreatedBy(jwtService.getUserId());
        }
        htmlDashboard.setModifiedOn(Instant.now());
        htmlDashboard.setModifiedBy(jwtService.getUserId());
        HtmlDashboard savedData = htmlDashboardRepository.save(htmlDashboard);

        return htmlDashboardMapper.map(savedData);
    }

    public void deleteObject(String id) {
        Optional<HtmlDashboard> optionalEntity = htmlDashboardRepository.findById(id);
        if (!optionalEntity.isPresent()) {
            throw new DoesNotExistException("HtmlDashboard Does Not Exist");
        }
        htmlDashboardRepository.deleteById(optionalEntity.get().getId());
    }


}
