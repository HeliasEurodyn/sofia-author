package com.crm.sofia.repository.data_transfer;

import com.crm.sofia.dto.data_transfer.DataTransferDTO;
import com.crm.sofia.model.data_transfer.DataTransfer;
import com.crm.sofia.repository.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataTransferRepository extends BaseRepository<DataTransfer> {

    @Query(" SELECT DISTINCT d.currentVersion FROM DataTransfer d " +
            " WHERE d.id =:id ")
    public Integer getCurrentVersion(@Param("id") String id);
    @Query("SELECT new com.crm.sofia.dto.data_transfer.DataTransferDTO(d.id,d.name,d.createdOn) FROM DataTransfer d")
    List<DataTransferDTO> getObject();

}
