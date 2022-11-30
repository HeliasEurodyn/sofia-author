package com.crm.sofia.repository.xls_import;

import com.crm.sofia.dto.xls_import.XlsImportDTO;
import com.crm.sofia.model.xls_import.XlsImport;
import com.crm.sofia.repository.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XlsImportRepository extends BaseRepository<XlsImport> {
    @Query("SELECT new com.crm.sofia.dto.xls_import.XlsImportDTO(x.id,x.name,x.description,x.createdOn) FROM XlsImport x")
    List<XlsImportDTO> getObject();
}
