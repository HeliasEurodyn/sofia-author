package com.crm.sofia.services.info_card;

import com.crm.sofia.dto.info_card.InfoCardDTO;
import com.crm.sofia.dto.info_card.InfoCardTextResponceDTO;
import com.crm.sofia.mapper.info_card.InfoCardMapper;
import com.crm.sofia.model.info_card.InfoCard;
import com.crm.sofia.native_repository.info_card.InfoCardNativeRepository;
import com.crm.sofia.repository.info_card.InfoCardRepository;
import com.crm.sofia.services.auth.JWTService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class InfoCardDesignerService {

    private final InfoCardRepository infoCardRepository;
    private final InfoCardMapper infoCardMapper;
    private final InfoCardNativeRepository infoCardNativeRepository;
    private final JWTService jwtService;
    private final InfoCardJavascriptService infoCardJavascriptService;

    public InfoCardDesignerService(InfoCardRepository infoCardRepository,
                                   InfoCardMapper infoCardMapper,
                                   InfoCardNativeRepository infoCardNativeRepository,
                                   JWTService jwtService,
                                   InfoCardJavascriptService infoCardJavascriptService) {
        this.infoCardRepository = infoCardRepository;
        this.infoCardMapper = infoCardMapper;
        this.infoCardNativeRepository = infoCardNativeRepository;
        this.jwtService = jwtService;
        this.infoCardJavascriptService = infoCardJavascriptService;
    }

    public List<InfoCardDTO> getObject() {
        List<InfoCard> infoCards = this.infoCardRepository.findAll();
        List<InfoCardDTO> infoCardDTOS = this.infoCardMapper.map(infoCards);
        infoCardDTOS.forEach(infoCardDTO -> infoCardDTO.setQuery(""));
        return infoCardDTOS;
    }

    public InfoCardDTO getObject(String id) {
        Optional<InfoCard> optionalInfoCard = this.infoCardRepository.findById(id);
        if (!optionalInfoCard.isPresent()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Info card does not exist");
        }
        InfoCardDTO infoCardDTO = this.infoCardMapper.map(optionalInfoCard.get());
        String encQuery = Base64.getEncoder().encodeToString(infoCardDTO.getQuery().getBytes(StandardCharsets.UTF_8));
        infoCardDTO.setQuery(encQuery);
        return infoCardDTO;
    }

    @Transactional
    @Modifying
    public InfoCardDTO postObject(InfoCardDTO dto) throws Exception {

        String decQuery = new String(Base64.getDecoder().decode(dto.getQuery().getBytes(StandardCharsets.UTF_8)));
        dto.setQuery(decQuery);

        InfoCard infoCard = this.infoCardMapper.map(dto);

        infoCard.setModifiedOn(Instant.now());
        infoCard.setModifiedBy(jwtService.getUserId());
        if (infoCard.getId() == null) infoCard.setCreatedOn(Instant.now());
        if (infoCard.getId() == null) infoCard.setCreatedBy(jwtService.getUserId());

        InfoCard createdInfoCard = this.infoCardRepository.save(infoCard);

        String script = this.infoCardJavascriptService.generateDynamicScript(createdInfoCard);
        String scriptMin = this.infoCardJavascriptService.minify(script);
        this.infoCardRepository.updateScripts(createdInfoCard.getId(), script, scriptMin);

        return this.infoCardMapper.map(createdInfoCard);
    }

    public void deleteObject(String id) {
        Optional<InfoCard> optionalInfoCard = this.infoCardRepository.findById(id);
        if (!optionalInfoCard.isPresent()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "InfoCard does not exist");
        }
        this.infoCardRepository.deleteById(optionalInfoCard.get().getId());
    }

    public InfoCardTextResponceDTO getData(String sql, Map<String, String> parameters) {
        return this.infoCardNativeRepository.getData(sql, parameters);
    }
}