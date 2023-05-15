package com.crm.sofia.controllers.author_dashboard;

import com.crm.sofia.services.author_dashboard.AuthorDashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@Validated
@RequestMapping("/author-dashboard")
public class AuthorDashboardController {

    @Autowired
    AuthorDashboardService authorDashboardService;

    @GetMapping(path = "/totals")
    Map<String, String> getComponentsTotals() {
        return authorDashboardService.getComponentsTotals();
    }
}
