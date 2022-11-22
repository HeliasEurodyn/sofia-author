package com.crm.sofia.services.data_transfer;

import com.crm.sofia.dto.data_transfer.DataTransferDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.data_transfer.DataTransferMapper;
import com.crm.sofia.model.data_transfer.DataTransfer;
import com.crm.sofia.repository.data_transfer.DataTransferRepository;
import com.crm.sofia.services.auth.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class DataTransferServiceTest {

    @Mock
    private DataTransferRepository dataTransferRepository;

    @InjectMocks
    private DataTransferService dataTransferService;

    @Mock
    private JWTService jwtService;

    private List<DataTransfer> dataTransferList;

    private DataTransfer dataTransfer;

    private DataTransferDTO dataTransferDTO;

    @Mock
    private DataTransferMapper dataTransferMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        dataTransferList = new ArrayList<>();
        dataTransfer = new DataTransfer().setName("dummyName").setQuery("dummyQuery");
        dataTransferList.add(dataTransfer);
        dataTransferDTO = new DataTransferDTO().setName("dummyNameDTO").setQuery(Base64.getEncoder().encodeToString("dummyQueryDTO".getBytes(StandardCharsets.UTF_8)));
        dataTransferDTO.setId("1");
    }

    @Test
    public void getObjectTest() {
        given(dataTransferRepository.findAll()).willReturn(dataTransferList);
        given(dataTransferMapper.map(ArgumentMatchers.any(List.class))).willReturn(List.of(dataTransferDTO));
        List<DataTransferDTO> list = dataTransferService.getObject();
        assertThat(list).isNotEmpty();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getName()).isEqualTo("dummyNameDTO");
    }

    @Test
    public void getObjectByIdTest() {
        given(dataTransferRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(dataTransfer));
        given(dataTransferMapper.map(ArgumentMatchers.any(DataTransfer.class))).willReturn(dataTransferDTO);
        DataTransferDTO dto = dataTransferService.getObject("1");
        assertThat(dto).isNotNull();
        assertThat(dto.getId().equals("1"));
    }

    @Test
    public void getObjectByIdWhenEmptyTest() {
        given(dataTransferRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            dataTransferService.getObject("1");
        });

        String expectedMessage = "DataTransfer Does Not Exist";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void postObjectTest() {
        given(dataTransferMapper.map(ArgumentMatchers.any(DataTransferDTO.class))).willReturn(dataTransfer);
        given(dataTransferRepository.save(ArgumentMatchers.any(DataTransfer.class))).willReturn(dataTransfer);
        given(dataTransferMapper.map(ArgumentMatchers.any(DataTransfer.class))).willReturn(dataTransferDTO);
        DataTransferDTO dto = dataTransferService.postObject(dataTransferDTO);
        assertThat(dto).isNotNull();
        assertThat(dto.getId().equals("1"));
    }


    @Test
    public void getDeleteByIdWhenEmptyTest() {
        given(dataTransferRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            dataTransferService.deleteObject("1");
        });
        String expectedMessage = "DataTransfer Does Not Exist";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}
