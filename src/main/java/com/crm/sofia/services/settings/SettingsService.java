package com.crm.sofia.services.settings;

import com.crm.sofia.dto.settings.SettingsDto;
import com.crm.sofia.exception.CouldNotSaveException;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.settings.SettingsMapper;
import com.crm.sofia.model.settings.Settings;
import com.crm.sofia.repository.settings.SettingsRepository;
import com.crm.sofia.services.auth.JWTService;
import com.crm.sofia.utils.AES_ENCRYPTION;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class SettingsService {

    private final SettingsRepository settingsRepository;
    private final SettingsMapper settingsMapper;
    private final JWTService jwtService;

    private final PasswordEncoder passwordEncoder;

    public SettingsService(SettingsRepository settingsRepository,
                           SettingsMapper settingsMapper,
                           JWTService jwtService, PasswordEncoder passwordEncoder) {
        this.settingsRepository = settingsRepository;
        this.settingsMapper = settingsMapper;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public String getSidebarImage() {
        String img = this.settingsRepository.getSidebarImage("1");
        return img;
    }

    public String getLoginImage() {
        String img = this.settingsRepository.getLoginImage("1");
        return img;
    }

    public String getIcon() {
        String icon = this.settingsRepository.getIcon("1");
        return icon;
    }

    public SettingsDto getObject() {
        Optional<Settings> settingsList = this.settingsRepository.findById("1");
        Settings settings;

        if(!settingsList.isPresent()){
            settings = new Settings();
        } else {
            settings = settingsList.get();
        }

        return settingsMapper.map(settings);
    }

    @Transactional
    public void postObject(SettingsDto settingsDto) {
        Settings settings = this.settingsMapper.map(settingsDto);
        settings.setId("1");
        encryptEmailPassword(settingsDto.getMailSenderPassword(), settings);
        this.settingsRepository.save(settings);
    }

    public String getName() {
        String name = this.settingsRepository.getName("1");
        return name;
    }

    private void encryptEmailPassword(String emailPassword, Settings settings) {
        Optional.ofNullable(emailPassword)
                .ifPresentOrElse(s -> {
                            try {
                                AES_ENCRYPTION aes_encryption = new AES_ENCRYPTION();
                                aes_encryption.init();
                                settings.setMailSenderPassword(aes_encryption.encrypt(emailPassword));
                            } catch (Exception exception) {
                                throw new CouldNotSaveException("encryption of mail password failed");
                            }
                        },
                        () -> settings.setMailSenderPassword(null));
    }


}
