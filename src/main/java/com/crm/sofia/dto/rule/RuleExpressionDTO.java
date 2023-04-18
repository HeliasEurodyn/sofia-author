package com.crm.sofia.dto.rule;

import com.crm.sofia.dto.common.BaseDTO;
import com.crm.sofia.dto.menu.MenuFieldDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class RuleExpressionDTO extends BaseDTO {

    String field;

    String operator;

    String command;

    String color;

    String joinType;

    String childrenJoinType;

    private List<RuleExpressionDTO> ruleExpressionList = null;
}