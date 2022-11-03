package com.crm.sofia.model.calendar;

import com.crm.sofia.model.common.MainEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Data
@Accessors(chain = true)
@Entity(name = "Calendar")
@Table(name = "calendar")
public class Calendar extends MainEntity {

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String icon;

    @Column(columnDefinition = "TEXT")
    private String query;



}
