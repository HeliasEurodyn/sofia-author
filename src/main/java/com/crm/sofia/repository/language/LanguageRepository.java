package com.crm.sofia.repository.language;

import com.crm.sofia.dto.language.LanguageDTO;
import com.crm.sofia.model.language.Language;
import com.crm.sofia.repository.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageRepository extends BaseRepository<Language> {
    @Query("SELECT new com.crm.sofia.dto.language.LanguageDTO(l.id,l.code,l.name) FROM Language l")
    List<LanguageDTO> getObject();
}
