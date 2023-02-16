package com.crm.sofia.services.html_template;

import com.crm.sofia.dto.html_template.HtmlTemplateDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.html_template.HtmlTemplateMapper;
import com.crm.sofia.model.html_template.HtmlTemplate;
import com.crm.sofia.repository.html_template.HtmlTemplateRepository;
import com.crm.sofia.services.auth.JWTService;
import com.crm.sofia.services.component.ComponentPersistEntityFieldAssignmentService;
import com.crm.sofia.utils.html_to_xhtml.HtmlToXhtml;
import com.crm.sofia.utils.xhtml_to_pdf.XhtmlToPdf;
import com.lowagie.text.DocumentException;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
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

    @Autowired
    private ComponentPersistEntityFieldAssignmentService componentPersistEntityFieldAssignmentService;

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
        if (dto.getHtml() != null) {
            String encodedHtml = Base64.getEncoder().encodeToString(dto.getHtml().getBytes(StandardCharsets.UTF_8));
            dto.setHtml(encodedHtml);
        }

        return dto;
    }


    public HtmlTemplateDTO generatePdf(String id) {
        HtmlTemplate entity = htmlTemplateRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("HtmlTemplate Does Not Exist"));

        HtmlTemplateDTO dto = htmlTemplateMapper.map(entity);

        return dto;

    }

    @Transactional
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

        this.componentPersistEntityFieldAssignmentService
                .saveFieldAssignments(htmlTemplateDTO.getComponent().getComponentPersistEntityList(),
                        "html_template",
                        savedData.getId());

        return htmlTemplateMapper.map(savedData);
    }

    public void deleteObject(String id) {
        HtmlTemplate optionalEntity = htmlTemplateRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("HtmlTemplate Does Not Exist"));

        htmlTemplateRepository.deleteById(optionalEntity.getId());
    }


    public void download(String id, HttpServletResponse response) throws IOException, DocumentException {

        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename="+ "report.pdf");
        response.setStatus(HttpServletResponse.SC_OK);
        OutputStream outputStream = response.getOutputStream();

        HtmlTemplateDTO htmlTemplateDTO = this.generatePdf(id);
        Document xhtml = HtmlToXhtml.htmlToXhtml(htmlTemplateDTO.getHtml());
        XhtmlToPdf.xhtmlToStream(xhtml, outputStream);

        outputStream.flush();
    }
}
