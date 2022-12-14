package com.crm.sofia.repository.menu;

import com.crm.sofia.dto.menu.MenuDTO;
import com.crm.sofia.model.menu.Menu;
import com.crm.sofia.repository.common.BaseRepository;
import org.hibernate.annotations.OrderBy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends BaseRepository<Menu> {

    @Query(" SELECT DISTINCT c " +
            " FROM Menu c " +
            " LEFT JOIN FETCH c.menuFieldList fl " +
            " WHERE c.id =:id " +
            " ORDER BY fl.shortOrder")
    Optional<Menu> findTreeById(@Param("id") String id);

    Menu findFirstByName(String author_menu);

    @Query("SELECT new com.crm.sofia.dto.menu.MenuDTO(m.id,m.name,m.modifiedOn) FROM Menu m ORDER BY m.modifiedOn DESC")
    List<MenuDTO> getObject();
}
