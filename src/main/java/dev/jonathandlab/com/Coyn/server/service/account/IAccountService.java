package dev.jonathandlab.com.Coyn.server.service.account;

import com.plaid.client.model.AccountsGetResponse;


public interface IAccountService {
    void getAccount();
    AccountsGetResponse getAccounts(String institutionId, String accessToken);
}
