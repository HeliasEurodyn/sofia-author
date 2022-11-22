package com.crm.sofia.services.download;

import com.crm.sofia.dto.download.DownloadDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.download.DownloadMapper;
import com.crm.sofia.model.download.Download;
import com.crm.sofia.repository.download.DownloadRepository;
import com.crm.sofia.services.auth.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class DownloadServiceTest {

    @Mock
    private DownloadRepository downloadRepository;
    @InjectMocks
    private DownloadService downloadService;

    @Mock
    private JWTService jwtService;
    private List<Download> downloadList;

    private Download download;
    private DownloadDTO downloadDto;

    @Mock
    private DownloadMapper downloadMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        downloadList = new ArrayList<>();
        download = new Download();
        downloadDto = new DownloadDTO();
        download.setCode("1234");
        download.setName("dummy");
        downloadList.add(download);
        downloadDto.setName("dummy");
        downloadDto.setCode("1234");
        downloadDto.setCode("1");
        downloadDto.setId("1");
    }

    @Test
    public void getObjectTest() {
        given(downloadRepository.findAll()).willReturn(downloadList);
        given(downloadMapper.map(ArgumentMatchers.any(List.class))).willReturn(List.of(downloadDto));
        List<DownloadDTO> list = downloadService.getObject();
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getName()).isEqualTo("dummy");
    }

    @Test
    public void getObjectByIdTest() {
        given(downloadRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(download));
        given(downloadMapper.map(ArgumentMatchers.any(Download.class))).willReturn(downloadDto);
        DownloadDTO dto = downloadService.getObject("1");
        assertThat(dto).isNotNull();
        assertThat(dto.getId().equals("1"));
    }

    @Test
    public void getObjectByIdWhenEmptyTest() {
        given(downloadRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            downloadService.getObject("1");
        });

        String expectedMessage = "Download Does Not Exist";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void postObjectTest() {
        given(downloadRepository.save(ArgumentMatchers.any(Download.class))).willReturn(download);
        given(downloadMapper.map(ArgumentMatchers.any(DownloadDTO.class))).willReturn(download);
        given(downloadMapper.map(ArgumentMatchers.any(Download.class))).willReturn(downloadDto);
        DownloadDTO dto = downloadService.postObject(downloadDto);
        assertThat(dto).isNotNull();
        assertThat(dto.getId().equals("1"));
    }


    @Test
    public void getDeleteByIdWhenEmptyTest() {
        given(downloadRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            downloadService.deleteObject("6L");
        });
        String expectedMessage = "Download Does Not Exist";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

}
