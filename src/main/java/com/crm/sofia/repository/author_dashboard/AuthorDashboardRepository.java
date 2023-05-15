package com.crm.sofia.repository.author_dashboard;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AuthorDashboardRepository {

    private final EntityManager entityManager;

    public AuthorDashboardRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Map<String, String> findAll() {
        Map<String, String> entityTotals = new HashMap<>();
        String queryString =
                "SELECT 'tables' AS type, COUNT(*) AS total FROM form\n" +
                        "UNION ALL \n" +
                        "SELECT 'lists' AS type, COUNT(*) AS total FROM list\n" +
                        "UNION ALL \n" +
                        "SELECT 'tables' AS type, COUNT(*) AS total FROM persist_entity WHERE entity_type = 'Table'\n" +
                        "UNION ALL \n" +
                        "SELECT 'appViews' AS type, COUNT(*) AS total FROM persist_entity WHERE entity_type = 'AppView'\n" +
                        "UNION ALL \n" +
                        "SELECT 'views' AS type, COUNT(*) AS total FROM persist_entity WHERE entity_type = 'View'\n" +
                        "UNION ALL \n" +
                        "SELECT 'components' AS type, COUNT(*) AS total FROM component\n" +
                        "UNION ALL \n" +
                        "SELECT 'users' AS type, COUNT(*) AS total FROM user\n" +
                        "UNION ALL \n" +
                        "SELECT 'roles' AS type, COUNT(*) AS total FROM role\n" +
                        "UNION ALL \n" +
                        "SELECT 'menus' AS type, COUNT(*) AS total FROM menu\n" +
                        "UNION ALL \n" +
                        "SELECT 'languages' AS type, COUNT(*) AS total FROM language\n" +
                        "UNION ALL \n" +
                        "SELECT 'chart' AS type, COUNT(*) AS total FROM chart\n" +
                        "UNION ALL \n" +
                        "SELECT 'infoCard' AS type, COUNT(*) AS total FROM info_card\n" +
                        "UNION ALL \n" +
                        "SELECT 'htmlPart' AS type, COUNT(*) AS total FROM html_dashboard\n" +
                        "UNION ALL \n" +
                        "SELECT 'dashboard' AS type, COUNT(*) AS total FROM dashboard\n" +
                        "UNION ALL \n" +
                        "SELECT 'reports' AS type, COUNT(*) AS total FROM report\n" +
                        "UNION ALL \n" +
                        "SELECT 'xlsImports' AS type, COUNT(*) AS total FROM xls_import\n" +
                        "UNION ALL \n" +
                        "SELECT 'searches' AS type, COUNT(*) AS total FROM search\n" +
                        "UNION ALL \n" +
                        "SELECT 'customQueries' AS type, COUNT(*) AS total FROM custom_query\n" +
                        "UNION ALL \n" +
                        "SELECT 'dataTransfer' AS type, COUNT(*) AS total FROM data_transfer\n";


        Query query = entityManager.createNativeQuery(queryString);

        List<Object[]> fields = query.getResultList();

        for (Object[] field : fields) {
            String type = (String) field[0];
            String totals = field[1].toString();
            entityTotals.put(type, totals);
        }

        return entityTotals;
    }

}