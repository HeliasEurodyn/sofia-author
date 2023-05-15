package com.crm.sofia.services.info_card;

import com.crm.sofia.dto.info_card.InfoCardDTO;
import com.crm.sofia.dto.info_card.InfoCardTextResponceDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.info_card.InfoCardMapper;
import com.crm.sofia.model.info_card.InfoCard;
import com.crm.sofia.native_repository.info_card.InfoCardNativeRepository;
import com.crm.sofia.repository.info_card.InfoCardRepository;
import com.crm.sofia.services.auth.JWTService;
import com.crm.sofia.utils.EncodingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class InfoCardDesignerService {
    @Autowired
    private InfoCardRepository infoCardRepository;
    @Autowired
    private InfoCardMapper infoCardMapper;
    @Autowired
    private InfoCardNativeRepository infoCardNativeRepository;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private InfoCardJavascriptService infoCardJavascriptService;


    public List<InfoCardDTO> getObject() {
        List<InfoCardDTO> infoCardList = this.infoCardRepository.getObject();
        infoCardList.forEach(infoCardDTO -> infoCardDTO.setQuery(""));
        return infoCardList;
    }

    public InfoCardDTO getObject(String id) {
        InfoCard optionalInfoCard = this.infoCardRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("InfoCard does not exist"));

        InfoCardDTO infoCardDTO = this.infoCardMapper.map(optionalInfoCard);
        if (infoCardDTO.getQuery() != null) {
            String uriEncoded = EncodingUtil.encodeURIComponent(infoCardDTO.getQuery());
            String encodedHtml = Base64.getEncoder().encodeToString(uriEncoded.getBytes(StandardCharsets.UTF_8));
            infoCardDTO.setQuery(encodedHtml);
        }

        infoCardDTO.getScripts()
                .stream()
                .filter(scriptDTO -> scriptDTO.getScript() != null)
                .forEach(scriptDto -> {
                    String uriEncoded = EncodingUtil.encodeURIComponent(scriptDto.getScript());
                    String encoded = Base64.getEncoder().encodeToString(uriEncoded.getBytes(StandardCharsets.UTF_8));
                    scriptDto.setScript(encoded);


                });


        return infoCardDTO;
    }


    @Transactional
    @Modifying
    public InfoCardDTO postObject(InfoCardDTO dto) {

        if (dto.getQuery() != null) {
            byte[] decodeQuery = Base64.getDecoder().decode(dto.getQuery());
            String query = EncodingUtil.decodeURIComponent(new String(decodeQuery));
            dto.setQuery(query);
        }

        dto.getScripts()
                .stream()
                .filter(scriptDTO -> scriptDTO.getScript() != null)
                .forEach(scriptDto -> {

                    byte[] decodeScript = Base64.getDecoder().decode(scriptDto.getScript());
                    String Script = EncodingUtil.decodeURIComponent(new String(decodeScript));
                    scriptDto.setScript(Script);
                });


        InfoCard infoCard = this.infoCardMapper.map(dto);

        infoCard.setModifiedOn(Instant.now());
        infoCard.setModifiedBy(jwtService.getUserId());
        if (infoCard.getId() == null) infoCard.setCreatedOn(Instant.now());
        if (infoCard.getId() == null) infoCard.setCreatedBy(jwtService.getUserId());

        InfoCard createdInfoCard = this.infoCardRepository.save(infoCard);

        String script = this.infoCardJavascriptService.generateDynamicScript(createdInfoCard);
        String scriptMin = null;
        try {
            scriptMin = this.infoCardJavascriptService.minify(script);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.infoCardRepository.updateScripts(createdInfoCard.getId(), script, scriptMin);

        return this.infoCardMapper.map(createdInfoCard);
    }

    public void deleteObject(String id) {
        InfoCard optionalInfoCard = this.infoCardRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("InfoCard does not exist"));

        this.infoCardRepository.deleteById(optionalInfoCard.getId());
    }

    public InfoCardTextResponceDTO getData(String sql, Map<String, String> parameters) {
        return this.infoCardNativeRepository.getData(sql, parameters);
    }
}
