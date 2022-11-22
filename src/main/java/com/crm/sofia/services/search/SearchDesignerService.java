package com.crm.sofia.services.search;


import com.crm.sofia.dto.search.SearchDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.search.SearchMapper;
import com.crm.sofia.model.search.Search;
import com.crm.sofia.repository.search.SearchRepository;
import com.crm.sofia.services.auth.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class SearchDesignerService {
    @Autowired
    private  SearchRepository searchRepository;
    @Autowired
    private  SearchMapper searchMapper;
    @Autowired
    private  JWTService jwtService;
    @Autowired
    private  EntityManager entityManager;



    public List<SearchDTO> getObject() {
        List<Search> searcies = this.searchRepository.findAll();
        return this.searchMapper.map(searcies);
    }

    public SearchDTO getObject(String id) {
        Search optionalSearch = this.searchRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("Search Does Not Exist"));

        return this.searchMapper.map(optionalSearch);
    }

    @Transactional
    @Modifying
    public SearchDTO postObject(SearchDTO dto) {
        Search search = this.searchMapper.map(dto);
        Search createdSearch = this.searchRepository.save(search);
        return this.searchMapper.map(createdSearch);
    }

    public void deleteObject(String id) {
        Search optionalSearch = this.searchRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("Search Does Not Exist"));
        this.searchRepository.deleteById(optionalSearch.getId());
    }

}
