package dev.jonathandlab.com.Coyn.server.service.account;

import com.plaid.client.model.*;
import com.plaid.client.request.PlaidApi;
import dev.jonathandlab.com.Coyn.server.exception.CoynAppException;
import dev.jonathandlab.com.Coyn.server.model.entity.account.AccountBalanceEntity;
import dev.jonathandlab.com.Coyn.server.model.entity.account.AccountEntity;
import dev.jonathandlab.com.Coyn.server.model.entity.user.AppUser;
import dev.jonathandlab.com.Coyn.server.repository.AccountRepository;
import dev.jonathandlab.com.Coyn.server.repository.BalanceRepository;
import dev.jonathandlab.com.Coyn.server.service.user.IAppUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class AccountService implements IAccountService {

    private PlaidApi plaidApi;
    private IAppUserService appUserService;
    private AccountRepository accountRepository;
    private BalanceRepository balanceRepository;

    @Override
    public void getAccount() {

    }

    @Override
    public AccountsGetResponse getAccounts(String accessToken) {
        AccountsGetRequest accountsGetRequest = new AccountsGetRequest();
        accountsGetRequest.setAccessToken(accessToken);
        try {
            AccountsGetResponse accountsGetResponse = Optional.ofNullable(plaidApi.accountsGet(accountsGetRequest).execute().body())
                    .orElseThrow(() -> {
                        throw new CoynAppException(HttpStatus.BAD_REQUEST, "Response Null");
                    });
            saveAccounts(accountsGetResponse);
            return accountsGetResponse;
        } catch (IOException e) {
            e.printStackTrace();
            throw new CoynAppException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public void saveAccounts(AccountsGetResponse accountsGetResponse) {
        AppUser appUser = appUserService.getCurrentAppUser();
        for (AccountBase account : accountsGetResponse.getAccounts()) {

            AccountEntity accountEntity = new AccountEntity();
            accountEntity.setName(account.getName());
            accountEntity.setMask(account.getMask());
            accountEntity.setOfficialName(account.getOfficialName());
            accountEntity.setType(account.getType().getValue());
//            accountEntity.setSubType(account.getSubtype().toString()); // TODO: Fix to default to value
//            accountEntity.setVerificationStatus(account.getVerificationStatus().toString());
            accountEntity.setAppUser(appUser);
            accountRepository.save(accountEntity);

            AccountBalance balances = account.getBalances();
            AccountBalanceEntity accountBalanceEntity = new AccountBalanceEntity();
            accountBalanceEntity.setAvailable(balances.getAvailable());
            accountBalanceEntity.setCurrent(balances.getCurrent());
            accountBalanceEntity.setIsoCurrencyCode(balances.getIsoCurrencyCode());
            accountBalanceEntity.setUnofficialCurrencyCode(balances.getUnofficialCurrencyCode());
            accountBalanceEntity.setLastUpdatedDatetime(balances.getLastUpdatedDatetime());
            accountBalanceEntity.setAccount(accountEntity);
            balanceRepository.save(accountBalanceEntity);
        }
    }

}
