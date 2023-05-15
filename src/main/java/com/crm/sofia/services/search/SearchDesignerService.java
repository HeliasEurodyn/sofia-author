package com.crm.sofia.services.search;


import com.crm.sofia.dto.list.ListDTO;
import com.crm.sofia.dto.search.SearchDTO;
import com.crm.sofia.dto.tag.TagDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.search.SearchMapper;
import com.crm.sofia.model.search.Search;
import com.crm.sofia.repository.search.SearchRepository;
import com.crm.sofia.services.auth.JWTService;
import com.crm.sofia.utils.EncodingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

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
        List<SearchDTO> searciesList = this.searchRepository.getObject();
        return searciesList;
    }

    public SearchDTO getObject(String id) {
        Search optionalSearch = this.searchRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("Search Does Not Exist"));

        SearchDTO dto = searchMapper.map(optionalSearch);

        if (dto.getQuery() != null) {
            String uriEncoded = EncodingUtil.encodeURIComponent(dto.getQuery());
            String encodedQuery = Base64.getEncoder().encodeToString(uriEncoded.getBytes(StandardCharsets.UTF_8));
            dto.setQuery(encodedQuery);
        }

        return dto;
    }

    public List<TagDTO> getTag() {
        List<TagDTO> tag = searchRepository.findTagDistinct();
        return tag;
    }

    public List<SearchDTO> getObjectByTag(String tag) {
        return this.searchRepository.getObjectByTag(tag);
    }

    @Transactional
    @Modifying
    public SearchDTO postObject(SearchDTO dto) {

        List<TagDTO> tags =
                dto.getTags().stream().collect(Collectors.toMap(TagDTO::getId, p -> p, (p, q) -> p))
                        .values()
                        .stream().collect(Collectors.toList());

        dto.setTags(tags);

        if (dto.getQuery() != null) {
            byte[] decodedHtml = Base64.getDecoder().decode(dto.getQuery());
            String Query = EncodingUtil.decodeURIComponent(new String(decodedHtml));
            dto.setQuery(Query);
        }


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
