package com.crm.sofia.model.sofia.list.translation;

import com.crm.sofia.model.common.BaseEntity;
import com.crm.sofia.model.sofia.language.Language;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Accessors(chain = true)
@DynamicUpdate
@DynamicInsert
@Entity(name = "ListActionButtonTranslation")
@Table(name = "list_action_button_translation")
public class ListActionButtonTranslation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Language.class)
    @JoinColumn(name = "language_id", referencedColumnName = "id")
    private Language language;


    @Column(length=1024)
    private String description;

}