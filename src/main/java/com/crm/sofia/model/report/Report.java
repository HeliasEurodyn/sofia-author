package com.crm.sofia.model.report;

import com.crm.sofia.model.access_control.AccessControl;
import com.crm.sofia.model.common.MainEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Data
@Accessors(chain = true)
@Entity(name = "Report")
@Table(name = "report")
public class Report extends MainEntity {

    @Column
    private String code;

    @Column
    private String name;

    @Column
    private String type;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = { CascadeType.ALL },
            orphanRemoval=true
    )
    @JoinColumn(name = "report_id")
    private List<ReportParameter> reportParameterList;

    @Column(columnDefinition = "LONGBLOB")
    private byte[] reportFileData;

    @Column
    private String reportUuid;

    @Column
    private String reportFilename;

    @Column
    private String reportExtension;

    @Column
    private String reportType;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "subreports_to_report",
            joinColumns = @JoinColumn(name = "report_id"),
            inverseJoinColumns = {
                    @JoinColumn(name = "subreport_id", referencedColumnName = "id")
            }
    )
    private List<Report> subreports;

    @Column
    private Boolean accessControlEnabled;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL},
            orphanRemoval = true
    )
    @JoinColumn(name = "report_id")
    private List<AccessControl> accessControls;

}
