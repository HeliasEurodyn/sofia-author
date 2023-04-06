package com.crm.sofia.services.author_dashboard;

import com.crm.sofia.repository.author_dashboard.AuthorDashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class AuthorDashboardService {


    @Autowired
    private AuthorDashboardRepository authorDashboardRepository;

    public AuthorDashboardService() {

    }

    public Map<String, String> getComponentsTotals() {
        Map<String, String> totals = new HashMap<>();
        totals = authorDashboardRepository.findAll();
        return totals;
    }
}
