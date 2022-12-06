package com.crm.sofia.repository.search;

import com.crm.sofia.dto.search.SearchDTO;
import com.crm.sofia.model.search.Search;
import com.crm.sofia.repository.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public  interface SearchRepository extends BaseRepository<Search> {
    @Query("SELECT new com.crm.sofia.dto.search.SearchDTO(s.id,s.name,s.modifiedOn) FROM Search s ORDER BY s.modifiedOn DESC")
    List<SearchDTO> getObject();
}
