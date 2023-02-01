package com.crm.sofia.repository.persistEntity;

import com.crm.sofia.model.persistEntity.ForeignKeyConstrain;
import com.crm.sofia.repository.common.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ForeignKeyConstrainRepository extends BaseRepository<ForeignKeyConstrain> {

    List<ForeignKeyConstrain> findAllByBaseTableId(String baseTableId);
}
