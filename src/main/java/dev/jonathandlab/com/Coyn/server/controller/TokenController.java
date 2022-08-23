package dev.jonathandlab.com.Coyn.server.controller;

import com.plaid.client.model.ItemPublicTokenExchangeResponse;
import com.plaid.client.model.LinkTokenCreateResponse;
import dev.jonathandlab.com.Coyn.server.model.request.token.PublicTokenExchangeRequest;
import dev.jonathandlab.com.Coyn.server.model.request.token.ServerTokenRefreshRequest;
import dev.jonathandlab.com.Coyn.server.model.response.token.ServerTokenResponse;
import dev.jonathandlab.com.Coyn.server.service.token.ITokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/token")
public class TokenController {

    private ITokenService tokenService;

    /**
     * Create link token for Plaid client
     * @return LinkTokenCreateResponse wrapped in ResponseEntity
     */
    @GetMapping("plaid/link")
    public ResponseEntity<LinkTokenCreateResponse> createLinkToken() {
        LinkTokenCreateResponse linkToken = tokenService.getLinkToken();
        return ResponseEntity.status(HttpStatus.OK)
                .body(linkToken);
    }

    /**
     * Exchange public token for access token
     * @param publicTokenExchangeRequest PublicTokenExchangeRequest object containing public token
     * @return ItemPublicTokenExchangeResponse containing access token
     */
    @PostMapping("plaid/link/exchange")
    public ResponseEntity<ItemPublicTokenExchangeResponse> exchangePublicToken(@RequestBody PublicTokenExchangeRequest publicTokenExchangeRequest) {
        ItemPublicTokenExchangeResponse publicToken = tokenService.exchangePublicToken(publicTokenExchangeRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(publicToken);
    }

    @PutMapping("/server/refresh")
    public ResponseEntity<ServerTokenResponse> refreshServerToken(@RequestBody ServerTokenRefreshRequest serverTokenRefreshRequest) {
        ServerTokenResponse serverTokenResponse = tokenService.refreshServerToken(serverTokenRefreshRequest);
        return ResponseEntity.ok(serverTokenResponse);
    }

}
