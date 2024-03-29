package com.crm.sofia.dto.user;

import com.crm.sofia.config.AppConstants;
import com.crm.sofia.dto.common.BaseDTO;
import com.crm.sofia.dto.language.LanguageDTO;
import com.crm.sofia.dto.menu.MenuDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class UserDTO extends BaseDTO {

    @NotNull
    private String username;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String repeatPassword;

    @NotNull
    private AppConstants.Types.UserStatus status;

    private String dateformat;

    private MenuDTO sidebarMenu;

    private MenuDTO headerMenu;

    private String loginNavCommand;

    private String searchNavCommand;

    private String provider;

    private List<RoleDTO> roles;

    private LanguageDTO defaultLanguage;

    private List<LanguageDTO> languages;

    private LanguageDTO currentLanguage;

    public UserDTO(String id , String username, String email, AppConstants.Types.UserStatus status, Instant modifiedOn) {
        this.setId(id);
        this.username = username;
        this.email = email;
        this.status = status;
        this.setModifiedOn(modifiedOn);
    }

    public UserDTO(String id , String username) {
        this.setId(id);
        this.username = username;
    }
}
