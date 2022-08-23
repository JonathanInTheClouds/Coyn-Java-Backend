package dev.jonathandlab.com.Coyn.server.service.token;

import com.plaid.client.model.ItemPublicTokenExchangeResponse;
import com.plaid.client.model.LinkTokenCreateResponse;
import dev.jonathandlab.com.Coyn.server.model.request.token.PublicTokenExchangeRequest;
import dev.jonathandlab.com.Coyn.server.model.request.token.ServerTokenRefreshRequest;
import dev.jonathandlab.com.Coyn.server.model.response.token.ServerTokenResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashMap;
import java.util.List;

public interface ITokenService {
    LinkTokenCreateResponse getLinkToken();
    ItemPublicTokenExchangeResponse exchangePublicToken(PublicTokenExchangeRequest publicTokenExchangeRequest);
    void invalidatePlaidAccessToken(String plaidAccessToken);
    HashMap<String, String> parsePlaidAccessTokens(String plaidAccessTokens);

    ServerTokenResponse createServerTokenResponse(String username, List<SimpleGrantedAuthority> authorities);
    void invalidateServerAccessToken(String serverAccessToken);

    ServerTokenResponse refreshServerToken(ServerTokenRefreshRequest serverTokenRefreshRequest);
    UsernamePasswordAuthenticationToken validateServerToken(String token);
}
