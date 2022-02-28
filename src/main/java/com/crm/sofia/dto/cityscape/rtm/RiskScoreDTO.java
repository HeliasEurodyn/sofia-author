package com.crm.sofia.dto.cityscape.rtm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class RiskScoreDTO {

    private Double confidentiality;

    private Double integrity;

    private Double availability;

//    private Double confidentiality_score;
//
//    private Double integrity_score;
//
//    private Double availability_score;
//
//    private Double score_sum;

}