package com.phenix.admin.oauth;

import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * @author zhenghui
 */
@Service
public class PhenixJdbcClientDetailsService extends JdbcClientDetailsService {
    public PhenixJdbcClientDetailsService(DataSource dataSource) {
        super(dataSource);
    }
}
