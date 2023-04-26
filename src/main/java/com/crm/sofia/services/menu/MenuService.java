package com.crm.sofia.services.menu;

import com.crm.sofia.dto.common.BaseDTO;
import com.crm.sofia.dto.menu.MenuDTO;
import com.crm.sofia.dto.menu.MenuFieldDTO;
import com.crm.sofia.dto.tag.TagDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.menu.MenuMapper;
import com.crm.sofia.model.menu.Menu;
import com.crm.sofia.repository.menu.MenuRepository;
import com.crm.sofia.services.auth.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuService {
    @Autowired
    private  MenuRepository menuRepository;

    @Autowired
    private  MenuMapper menuMapper;
    @Autowired
    private  MenuFieldService menuFieldService;
    @Autowired
    private  JWTService jwtService;


    public List<MenuDTO> getObject() {
        List<MenuDTO> menuList = this.menuRepository.getObject();
        return menuList;
    }

    public MenuDTO getObject(String id, String languageId) {

        Optional<Menu> optionalEntity = this.menuRepository.findTreeById(id);

        if (!optionalEntity.isPresent()) {
            throw new DoesNotExistException("Menu Does Not Exist");
        }
        Menu entity = optionalEntity.get();
        MenuDTO dto = this.menuMapper.mapMenu(entity, languageId);

        List<MenuFieldDTO> menuFieldList =
                dto.getMenuFieldList()
                        .stream()
                        .sorted(Comparator.comparingLong(MenuFieldDTO::getShortOrder))
                        .collect(Collectors.toList());

        menuFieldList.forEach( x -> {
            List<MenuFieldDTO> childrenDTOs = this.menuFieldService.getObjectTree( x.getId());
            x.setMenuFieldList(childrenDTOs);
        });

        dto.setMenuFieldList(menuFieldList);

        return dto;
    }

    public List<TagDTO> getTag(){
        List<TagDTO> tag = menuRepository.findTagDistinct();
        return  tag;
    }

    public List<MenuDTO> getObjectByTag(String tag){
        return this.menuRepository.getObjectByTag(tag);
    }

    public void deleteObject(String id) {
        Menu optionalEntity = this.menuRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("Menu Does Not Exist"));

        this.menuRepository.deleteById(optionalEntity.getId());
    }

    @Transactional
    public MenuDTO postObject(MenuDTO menuDTO) {
        Menu component = this.menuMapper.mapDTO(menuDTO);
        component.setCreatedOn(Instant.now());
        component.setModifiedOn(Instant.now());
        component.setCreatedBy(jwtService.getUserId());
        component.setModifiedBy(jwtService.getUserId());

        Menu createdComponent = this.menuRepository.save(component);
        return this.menuMapper.map(createdComponent);
    }

    @Transactional
    public MenuDTO putObject(MenuDTO menuDTO) {

        List<TagDTO> tags =
                menuDTO.getTags().stream().collect(Collectors.toMap(TagDTO::getId, p -> p, (p, q) -> p))
                        .values()
                        .stream().collect(Collectors.toList());

        menuDTO.setTags(tags);

       Menu optionalComponent = this.menuRepository.findById(menuDTO.getId())
               .orElseThrow(() -> new DoesNotExistException("Menu Does Not Exist"));

        Menu entity = optionalComponent;

        menuMapper.mapDtoToEntity(menuDTO, entity);

        entity.setModifiedOn(Instant.now());
        entity.setModifiedBy(jwtService.getUserId());

        Menu createdEntity = this.menuRepository.save(entity);
        MenuDTO createdDto = this.menuMapper.map(createdEntity);

        return createdDto;
    }

}
