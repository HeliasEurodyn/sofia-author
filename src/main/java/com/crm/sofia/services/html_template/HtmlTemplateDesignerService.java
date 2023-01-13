package com.crm.sofia.services.html_template;

import com.crm.sofia.dto.html_template.HtmlTemplateDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.html_template.HtmlTemplateMapper;
import com.crm.sofia.model.html_template.HtmlTemplate;
import com.crm.sofia.repository.html_template.HtmlTemplateRepository;
import com.crm.sofia.services.auth.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
@Service
public class HtmlTemplateDesignerService {
    @Autowired
    HtmlTemplateRepository htmlTemplateRepository;

    @Autowired
    HtmlTemplateMapper htmlTemplateMapper;

    @Autowired
    JWTService jwtService;

    public HtmlTemplateDesignerService() {
    }


    public List<HtmlTemplateDTO> getObject() {
        List<HtmlTemplateDTO> htmlTemplateList = htmlTemplateRepository.getObject();
        return htmlTemplateList;
    }

    public HtmlTemplateDTO getObject(String id) {
        HtmlTemplate entity = htmlTemplateRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("HtmlTemplate Does Not Exist"));

        HtmlTemplateDTO dto = htmlTemplateMapper.map(entity);
        if(dto.getHtml() != null){
            String encodedHtml = Base64.getEncoder().encodeToString(dto.getHtml().getBytes(StandardCharsets.UTF_8));
            dto.setHtml(encodedHtml);
        }

        return dto;
    }

    public HtmlTemplateDTO postObject(HtmlTemplateDTO htmlTemplateDTO) {
        if (htmlTemplateDTO.getHtml() != null) {
            byte[] decodedHtml = Base64.getDecoder().decode(htmlTemplateDTO.getHtml());
            htmlTemplateDTO.setHtml(new String(decodedHtml));
        }



        HtmlTemplate htmlTemplate = htmlTemplateMapper.map(htmlTemplateDTO);
        if (htmlTemplate.getId() == null) {
            htmlTemplate.setCreatedOn(Instant.now());
            htmlTemplate.setCreatedBy(jwtService.getUserId());
        }
        htmlTemplate.setModifiedOn(Instant.now());
        htmlTemplate.setModifiedBy(jwtService.getUserId());
        HtmlTemplate savedData = htmlTemplateRepository.save(htmlTemplate);

        return htmlTemplateMapper.map(savedData);
    }

    public void deleteObject(String id) {
        HtmlTemplate optionalEntity = htmlTemplateRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("HtmlTemplate Does Not Exist"));

        htmlTemplateRepository.deleteById(optionalEntity.getId());
    }


}
