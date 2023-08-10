package com.crm.sofia.services.html_dashboard;


import com.crm.sofia.dto.html_dashboard.HtmlDashboardDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.html_dashboard.HtmlDashboardMapper;
import com.crm.sofia.model.html_dashboard.HtmlDashboard;
import com.crm.sofia.repository.html_dashboard.HtmlDashboardRepository;
import com.crm.sofia.services.auth.JWTService;
import com.crm.sofia.services.info_card.InfoCardJavascriptService;
import com.crm.sofia.utils.EncodingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
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
    @Autowired
    private HtmlDashboardJavascriptService htmlDashboardJavascriptService;

    public List<HtmlDashboardDTO> getObject() {
        List<HtmlDashboardDTO> htmlDashboardList = htmlDashboardRepository.getObject();
        return htmlDashboardList;
    }

    public HtmlDashboardDTO getObject(String id) {
        HtmlDashboard optionalEntity = htmlDashboardRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("HtmlDashboard Does Not Exist"));

        HtmlDashboard entity = optionalEntity;
        HtmlDashboardDTO dto = htmlDashboardMapper.map(entity);

        if (dto.getHtml() != null) {
            String uriEncoded = EncodingUtil.encodeURIComponent(dto.getHtml());
            String encodedHtml = Base64.getEncoder().encodeToString(uriEncoded.getBytes(StandardCharsets.UTF_8));
            dto.setHtml(encodedHtml);
        }

        if(dto.getScripts() != null)
        dto.getScripts()
                .stream()
                .filter(scriptDTO -> scriptDTO.getScript() != null)
                .forEach(scriptDto -> {
                    String uriEncoded = EncodingUtil.encodeURIComponent(scriptDto.getScript());
                    String encoded = Base64.getEncoder().encodeToString(uriEncoded.getBytes(StandardCharsets.UTF_8));
                    scriptDto.setScript(encoded);
                });

        return dto;
    }

    @Transactional
    public HtmlDashboardDTO postObject(HtmlDashboardDTO htmlDashboardDto) {

        if (htmlDashboardDto.getHtml() != null) {
            byte[] decodedHtml = Base64.getDecoder().decode(htmlDashboardDto.getHtml());
            String Html = EncodingUtil.decodeURIComponent(new String(decodedHtml));
            htmlDashboardDto.setHtml(Html);
        }

        if(htmlDashboardDto.getScripts() != null)
        htmlDashboardDto.getScripts()
                .stream()
                .filter(scriptDTO -> scriptDTO.getScript() != null)
                .forEach(scriptDto -> {
                    byte[] decodeScript = Base64.getDecoder().decode(scriptDto.getScript());
                    String Script = EncodingUtil.decodeURIComponent(new String(decodeScript));
                    scriptDto.setScript(Script);
                });

        HtmlDashboard htmlDashboard = htmlDashboardMapper.map(htmlDashboardDto);
        if (htmlDashboardDto.getId() == null) {
            htmlDashboard.setCreatedOn(Instant.now());
            htmlDashboard.setCreatedBy(jwtService.getUserId());
        }
        htmlDashboard.setModifiedOn(Instant.now());
        htmlDashboard.setModifiedBy(jwtService.getUserId());
        HtmlDashboard savedData = htmlDashboardRepository.save(htmlDashboard);

        String script = this.htmlDashboardJavascriptService.generateDynamicScript(savedData);
        String scriptMin = null;
        try {
            scriptMin = this.htmlDashboardJavascriptService.minify(script);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.htmlDashboardRepository.updateScripts(savedData.getId(), script, scriptMin);

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
