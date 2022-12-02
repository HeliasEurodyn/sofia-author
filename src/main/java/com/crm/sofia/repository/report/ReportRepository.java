package com.crm.sofia.repository.report;

import com.crm.sofia.dto.report.ReportDTO;
import com.crm.sofia.model.report.Report;
import com.crm.sofia.model.report.ReportParameter;
import com.crm.sofia.repository.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends BaseRepository<Report> {

    @Query(" SELECT r.reportUuid FROM Report r " +
            " WHERE r.id =:id ")
    String findUuid(@Param("id")String id);

    @Query(" SELECT r.reportType FROM Report r " +
            " WHERE r.id =:id ")
    String findType(@Param("id")String id);

    @Query(" SELECT r.reportFileData FROM Report r " +
            " WHERE r.id =:id ")
    byte[] findReportFileData(String id);

    @Query(" SELECT r.reportParameterList FROM Report r " +
            " WHERE r.id =:id ")
    List<ReportParameter> getReportParametersById(String id);
    @Query("SELECT new com.crm.sofia.dto.report.ReportDTO(r.id,r.code,r.name,r.createdOn) FROM Report r ORDER BY r.modifiedOn DESC")
    List<ReportDTO> getObject();

}
