package com.crm.sofia.model.form.translation;

import com.crm.sofia.dto.language.LanguageDTO;
import com.crm.sofia.model.common.BaseEntity;
import com.crm.sofia.model.language.Language;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
@Data
@Accessors(chain = true)
@DynamicUpdate
@DynamicInsert
@Entity(name = "FormActionButtonTranslation")
@Table(name = "form_action_button_translation")
public class FormActionButtonTranslation extends BaseEntity implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Language.class)
    @JoinColumn(name = "language_id", referencedColumnName = "id")
    private Language language;

    @Column(length=1024)
    private String description;
}
