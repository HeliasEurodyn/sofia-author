package com.crm.sofia.dto.settings;

import com.crm.sofia.dto.common.BaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class SettingsDto extends BaseDTO {

    String name;

    String sidebarImage;

    String loginImage;

    String icon;

    String oauthPrototypeUserId;

    String oauthPrototypeUserName;

    String mailSenderHost;

    String mailSenderPort;

    String mailSenderUsername;

    String mailSenderPassword;
}
