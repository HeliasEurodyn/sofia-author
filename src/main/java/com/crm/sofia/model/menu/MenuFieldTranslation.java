package com.crm.sofia.model.menu;

import com.crm.sofia.model.common.BaseEntity;
import com.crm.sofia.model.language.Language;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Accessors(chain = true)
@DynamicUpdate
@DynamicInsert
@Entity(name = "MenuFieldTranslation")
@Table(name = "menu_field_translation")
public class MenuFieldTranslation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Language.class)
    @JoinColumn(name = "language_id", referencedColumnName = "id")
    private Language language;

    @Column
    private String name;
}
