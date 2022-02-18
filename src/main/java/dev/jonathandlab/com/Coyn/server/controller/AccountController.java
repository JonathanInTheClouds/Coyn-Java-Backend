package dev.jonathandlab.com.Coyn.server.controller;

import com.plaid.client.model.AccountsGetResponse;
import com.plaid.client.model.ItemPublicTokenExchangeResponse;
import dev.jonathandlab.com.Coyn.server.model.request.account.AddAccountsRequest;
import dev.jonathandlab.com.Coyn.server.model.response.account.AddAccountResponse;
import dev.jonathandlab.com.Coyn.server.service.account.IAccountService;
import dev.jonathandlab.com.Coyn.server.model.request.token.AccessTokenRequest;
import dev.jonathandlab.com.Coyn.server.model.request.token.PublicTokenExchangeRequest;
import dev.jonathandlab.com.Coyn.server.model.response.account.AccountsResponse;
import dev.jonathandlab.com.Coyn.server.service.token.ITokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(value = {"/institution"})
public class AccountController {

    private IAccountService accountService;
    private ITokenService tokenService;


    /**
     * Get AccountsResponse object using access token
     * @param accessTokenRequest AccessTokenRequest class containing accessToken
     * @return AccountsResponse class wrapped in ResponseEntity
     */
    @PostMapping(value = {"/{institutionId}/account/accessToken"})
    public ResponseEntity<AccountsResponse> getAccounts(@PathVariable String institutionId, @RequestBody AccessTokenRequest accessTokenRequest) {
        AccountsGetResponse accounts = accountService.getAccounts(institutionId, accessTokenRequest.getAccessToken());
        AccountsResponse accountsResponse = AccountsResponse.builder()
                .institutionName(accounts.getAccounts().get(0).getOfficialName()) // TODO: Fix
                .accountAccessToken(accessTokenRequest.getAccessToken())
                .accounts(accounts.getAccounts())
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(accountsResponse);
    }

    /**
     * Get AccountsResponse object using public token
     * @param publicTokenExchangeRequest PublicTokenExchangeRequest class containing publicToken
     * @return AccountsResponse class wrapped in ResponseEntity
     */
    @PostMapping("/{institutionId}/account/publicToken")
    public ResponseEntity<AccountsResponse> getAccounts(@PathVariable String institutionId, @RequestBody PublicTokenExchangeRequest publicTokenExchangeRequest) {
        ItemPublicTokenExchangeResponse publicToken = tokenService.exchangePublicToken(publicTokenExchangeRequest);
        AccountsGetResponse accounts = accountService.getAccounts(institutionId, publicToken.getAccessToken());
        final String institutionName = Optional.ofNullable(accounts.getAccounts().get(0).getOfficialName())
                .orElse("Unknown");
        AccountsResponse accountsResponse = AccountsResponse.builder()
                .institutionName(institutionName) // TODO: Fix
                .accountAccessToken(publicToken.getAccessToken())
                .accounts(accounts.getAccounts())
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(accountsResponse);
    }

}
