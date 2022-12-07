package com.crm.sofia.repository.business_unit;

import com.crm.sofia.dto.business_unit.BusinessUnitDTO;
import com.crm.sofia.model.business_unit.BusinessUnit;
import com.crm.sofia.repository.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessUnitRepository extends BaseRepository<BusinessUnit> {
    @Query("SELECT new com.crm.sofia.dto.business_unit.BusinessUnitDTO(b.id,b.title,b.modifiedOn) FROM BusinessUnit b ORDER BY b.modifiedOn DESC")
    List<BusinessUnitDTO> getObject();
}
