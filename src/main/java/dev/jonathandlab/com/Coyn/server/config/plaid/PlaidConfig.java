package dev.jonathandlab.com.Coyn.server.config.plaid;

import com.plaid.client.ApiClient;
import com.plaid.client.request.PlaidApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class PlaidConfig {

    @Value("${plaid.client_id}")
    private String CLIENT_ID;

    @Value("${plaid.sandbox_id}")
    private String SANDBOX_ID;

    @Value("${plaid.development_id}")
    private String DEVELOPMENT_ID;

    @Value("${plaid.production_id}")
    private String PRODUCTION_ID;

    @Value("${plaid.environment}")
    private String ENVIRONMENT;

    @Bean
    public PlaidApi plaidApi() {
        HashMap<String, String> apiKeys = new HashMap<>();
        apiKeys.put("clientId", CLIENT_ID);
        apiKeys.put("secret", SANDBOX_ID);
        ApiClient apiClient = new ApiClient(apiKeys);
        apiClient.setPlaidAdapter(ApiClient.Sandbox);
        return apiClient.createService(PlaidApi.class);
    }

}
