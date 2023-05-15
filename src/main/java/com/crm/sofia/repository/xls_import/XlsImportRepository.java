package com.crm.sofia.repository.xls_import;

import com.crm.sofia.dto.list.ListDTO;
import com.crm.sofia.dto.tag.TagDTO;
import com.crm.sofia.dto.xls_import.XlsImportDTO;
import com.crm.sofia.model.xls_import.XlsImport;
import com.crm.sofia.repository.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XlsImportRepository extends BaseRepository<XlsImport> {
    @Query("SELECT new com.crm.sofia.dto.xls_import.XlsImportDTO(x.id,x.name,x.description,x.modifiedOn) FROM XlsImport x ORDER BY x.modifiedOn DESC")
    List<XlsImportDTO> getObject();

    @Query(" SELECT DISTINCT new com.crm.sofia.dto.tag.TagDTO(xt.title, xt.color) " +
            " FROM XlsImport x  " +
            " INNER JOIN x.tags xt ")
    List<TagDTO> findTagDistinct();

    @Query("SELECT DISTINCT new com.crm.sofia.dto.xls_import.XlsImportDTO(x.id, x.name,x.description,x.modifiedOn) " +
            "FROM XlsImport x " +
            "INNER JOIN x.tags xt " +
            "WHERE xt.title = :tag " +
            "ORDER BY x.modifiedOn DESC")
    List<XlsImportDTO> getObjectByTag(@Param("tag") String tag);

}
