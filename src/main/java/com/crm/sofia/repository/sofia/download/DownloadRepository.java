package com.crm.sofia.repository.sofia.download;

import com.crm.sofia.model.sofia.download.Download;
import com.crm.sofia.model.sofia.report.Report;
import com.crm.sofia.repository.common.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DownloadRepository extends BaseRepository<Download> {

    List<Download> findAll();
}