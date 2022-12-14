package com.crm.sofia.model.language;

import com.crm.sofia.model.common.MainEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@DynamicUpdate
@DynamicInsert
@Entity(name = "Language")
@Table(name = "language")
public class Language extends MainEntity implements Serializable {

    @Column
    private String code;

    @Column
    private String name;

    @Column
    private String image;

}
