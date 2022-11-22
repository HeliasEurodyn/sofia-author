package com.crm.sofia.services.chart;

import com.crm.sofia.dto.chart.ChartDTO;
import com.crm.sofia.dto.chart.ChartFieldDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.chart.ChartMapper;
import com.crm.sofia.model.chart.Chart;
import com.crm.sofia.native_repository.chart.ChartNativeRepository;
import com.crm.sofia.repository.chart.ChartRepository;
import com.crm.sofia.services.auth.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ChartDesignerService {

    @Autowired
    private  ChartRepository chartRepository;
    @Autowired
    private  ChartMapper chartMapper;
    @Autowired
    private  ChartNativeRepository chartNativeRepository;
    @Autowired
    private  JWTService jwtService;



    @Transactional
    public List<ChartFieldDTO> generateDataFields(String sql) {
        List<ChartFieldDTO> chartFieldList = this.chartNativeRepository.generateFields(sql);

        return this.chartNativeRepository.getData(chartFieldList, sql, new HashMap<String, String>());
    }

    @Transactional
    public List<ChartFieldDTO> getData(String id) {
        ChartDTO chartDTO = this.getObject(id);
        String query = chartDTO.getQuery();
        query = query.replace("##asset_id##", this.jwtService.getUserId().toString());

        return this.chartNativeRepository.getData(chartDTO.getChartFieldList(), query,  new HashMap<String, String>());
    }

    public List<ChartDTO> getObject() {
        List<Chart> charts = this.chartRepository.findAll();
        return this.chartMapper.map(charts);
    }

    public ChartDTO getObject(String id) {
        Chart optionalChart = this.chartRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("Chart Does Not Exist"));

        ChartDTO chart = this.chartMapper.map(optionalChart);
        String encQuery = Base64.getEncoder().encodeToString(chart.getQuery().getBytes(StandardCharsets.UTF_8));
        chart.setQuery(encQuery);
        return chart;
    }

    @Transactional
    public ChartDTO postObject(ChartDTO dto) {

        String decQuery = new String(Base64.getDecoder().decode(dto.getQuery()));
        dto.setQuery(decQuery);

        Chart chart = this.chartMapper.map(dto);
        Chart createdChart = this.chartRepository.save(chart);
        return this.chartMapper.map(createdChart);
    }

    public void deleteObject(String id) {
        Chart optionalChart = this.chartRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("Chart Does Not Exist"));

        this.chartRepository.deleteById(optionalChart.getId());
    }

}
