package com.phenix.core.security.configuration;

import com.phenix.core.security.oauth2.client.PhenixOAuth2ClientDetails;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = "phenix.oauth2.client.admin")
public class PhenixOAuthAdminProperties  extends PhenixOAuth2ClientDetails {
}
