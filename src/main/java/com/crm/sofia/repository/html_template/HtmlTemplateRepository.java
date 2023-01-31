package com.crm.sofia.repository.html_template;


import com.crm.sofia.dto.html_template.HtmlTemplateDTO;
import com.crm.sofia.model.html_template.HtmlTemplate;
import com.crm.sofia.repository.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HtmlTemplateRepository extends BaseRepository<HtmlTemplate> {

    @Query("SELECT new com.crm.sofia.dto.html_template.HtmlTemplateDTO(h.id,h.title,h.html,h.modifiedOn,c.id, c.name) FROM HtmlTemplate h LEFT JOIN h.component c ORDER BY h.modifiedOn DESC")
    List<HtmlTemplateDTO> getObject();
}
