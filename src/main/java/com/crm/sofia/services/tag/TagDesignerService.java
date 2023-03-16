package com.crm.sofia.services.tag;

import com.crm.sofia.dto.tag.TagDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.tag.TagMapper;
import com.crm.sofia.model.tag.Tag;
import com.crm.sofia.repository.tag.TagRepository;
import com.crm.sofia.services.auth.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class TagDesignerService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private JWTService jwtService;

    public List<TagDTO> getObject() {
        List<TagDTO> tagList = tagRepository.getObject();
        return tagList;
    }

    public TagDTO getObject(String id) {
        Tag optionalEntity = tagRepository.findById(id).orElseThrow(() -> new DoesNotExistException("Tag Does Not Exist"));

        TagDTO dto = tagMapper.map(optionalEntity);

        return dto;
    }

    public TagDTO postObject(TagDTO tagDTO) {

        Tag tag = tagMapper.map(tagDTO);
        if (tag.getId() == null) {
            tag.setCreatedOn(Instant.now());
            tag.setCreatedBy(jwtService.getUserId());
        }
        tag.setModifiedOn(Instant.now());
        tag.setModifiedBy(jwtService.getUserId());
        Tag savedData = tagRepository.save(tag);

        return tagMapper.map(savedData);
    }

    public void deleteObject(String id) {
        Tag optionalEntity = tagRepository.findById(id).orElseThrow(() -> new DoesNotExistException("Tag Does Not Exist"));

        tagRepository.deleteById(optionalEntity.getId());
    }

}
