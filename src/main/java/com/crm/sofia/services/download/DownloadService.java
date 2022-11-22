package com.crm.sofia.services.download;

import com.crm.sofia.dto.download.DownloadDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.download.DownloadMapper;
import com.crm.sofia.model.download.Download;
import com.crm.sofia.repository.download.DownloadRepository;
import com.crm.sofia.services.auth.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class DownloadService {
    @Autowired
    private DownloadMapper downloadMapper;
    @Autowired
    private DownloadRepository downloadRepository;
    @Autowired
    private JWTService jwtService;

    public List<DownloadDTO> getObject() {
        List<Download> entites = downloadRepository.findAll();
        return downloadMapper.map(entites);
    }

    public DownloadDTO getObject(String id) {
       Download optionalEntity = downloadRepository.findById(id)
               .orElseThrow(() -> new DoesNotExistException("Download Does Not Exist"));

        DownloadDTO dto = downloadMapper.map(optionalEntity);
        return dto;
    }

    //    @Transactional
    public DownloadDTO postObject(DownloadDTO downloadDto) {

        Download download = downloadMapper.map(downloadDto);
        if (downloadDto.getId() == null) {
            download.setCreatedOn(Instant.now());
            download.setCreatedBy(jwtService.getUserId());
        }
        download.setModifiedOn(Instant.now());
        download.setModifiedBy(jwtService.getUserId());
        Download savedData = downloadRepository.save(download);

        return downloadMapper.map(savedData);
    }

    public void deleteObject(String id) {
        Download optionalEntity = downloadRepository.findById(id)
                        .orElseThrow(() -> new DoesNotExistException("Download Does Not Exist"));

        downloadRepository.deleteById(optionalEntity.getId());
    }

}
