package dev.jonathandlab.com.Coyn.server.controller;

import com.plaid.client.model.AccountsGetResponse;
import com.plaid.client.model.ItemPublicTokenExchangeResponse;
import dev.jonathandlab.com.Coyn.server.service.account.IAccountService;
import dev.jonathandlab.com.Coyn.server.model.request.token.AccessTokenRequest;
import dev.jonathandlab.com.Coyn.server.model.request.token.PublicTokenExchangeRequest;
import dev.jonathandlab.com.Coyn.server.model.response.account.AccountsResponse;
import dev.jonathandlab.com.Coyn.server.service.token.ITokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private IAccountService accountService;
    private ITokenService tokenService;

    /**
     * Get AccountsResponse object using public token
     * @param publicTokenExchangeRequest PublicTokenExchangeRequest class containing publicToken
     * @return AccountsResponse class wrapped in ResponseEntity
     */
    @PostMapping("publicToken")
    public ResponseEntity<AccountsResponse> getAccountsResponse(@RequestBody PublicTokenExchangeRequest publicTokenExchangeRequest) {
        ItemPublicTokenExchangeResponse publicToken = tokenService.exchangePublicToken(publicTokenExchangeRequest);
        AccountsGetResponse accounts = accountService.getAccounts(publicToken.getAccessToken());
        AccountsResponse accountsResponse = AccountsResponse.builder()
                .institutionName(accounts.getAccounts().get(0).getOfficialName()) // TODO: Fix
                .accountAccessToken(publicToken.getAccessToken())
                .accounts(accounts.getAccounts())
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(accountsResponse);
    }

    /**
     * Get AccountsResponse object using access token
     * @param accessTokenRequest AccessTokenRequest class containing accessToken
     * @return AccountsResponse class wrapped in ResponseEntity
     */
    @PostMapping("accessToken")
    public ResponseEntity<AccountsResponse> getAccountsResponse(@RequestBody AccessTokenRequest accessTokenRequest) {
        AccountsGetResponse accounts = accountService.getAccounts(accessTokenRequest.getAccessToken());
        AccountsResponse accountsResponse = AccountsResponse.builder()
                .institutionName(accounts.getAccounts().get(0).getOfficialName()) // TODO: Fix
                .accountAccessToken(accessTokenRequest.getAccessToken())
                .accounts(accounts.getAccounts())
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(accountsResponse);
    }

}
