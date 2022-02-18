package dev.jonathandlab.com.Coyn.server.service.account;

import com.plaid.client.model.*;
import com.plaid.client.request.PlaidApi;
import dev.jonathandlab.com.Coyn.server.exception.CoynAppException;
import dev.jonathandlab.com.Coyn.server.model.entity.account.AccountBalanceEntity;
import dev.jonathandlab.com.Coyn.server.model.entity.account.AccountEntity;
import dev.jonathandlab.com.Coyn.server.model.entity.institution.InstitutionEntity;
import dev.jonathandlab.com.Coyn.server.model.entity.user.AppUserEntity;
import dev.jonathandlab.com.Coyn.server.model.response.account.AddAccountResponse;
import dev.jonathandlab.com.Coyn.server.repository.AccountRepository;
import dev.jonathandlab.com.Coyn.server.repository.AppUserRepository;
import dev.jonathandlab.com.Coyn.server.repository.BalanceRepository;
import dev.jonathandlab.com.Coyn.server.repository.InstitutionRepository;
import dev.jonathandlab.com.Coyn.server.service.user.IAppUserService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
@Transactional
public class AccountService implements IAccountService {

    private PlaidApi plaidApi;
    private IAppUserService appUserService;
    private InstitutionRepository institutionRepository;
    private AppUserRepository appUserRepository;
    private AccountRepository accountRepository;
    private BalanceRepository balanceRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public void getAccount() {

    }

    @Override
    public AccountsGetResponse getAccounts(String institutionId, String accessToken) {
        AppUserEntity appUserEntity = appUserService.getCurrentAppUser();
        AccountsGetRequest accountsGetRequest = new AccountsGetRequest();
        accountsGetRequest.setAccessToken(accessToken);
        try {
            AccountsGetResponse accountsGetResponse = Optional.ofNullable(plaidApi.accountsGet(accountsGetRequest).execute().body())
                    .orElseThrow(() -> {
                        throw new CoynAppException(HttpStatus.BAD_REQUEST, "Response Null");
                    });
            final Set<InstitutionEntity> institutions = appUserEntity.getInstitutions();
            boolean isNewInstitution = institutions.stream().noneMatch(institution -> institution.getGeneralId().equalsIgnoreCase(institutionId));
            if (isNewInstitution) {
                final String institutionName = Optional.ofNullable(accountsGetResponse.getAccounts().get(0).getOfficialName())
                        .orElse("Unknown");
                saveInstitutionEntity(institutionId, institutionName, accessToken, appUserEntity);
            }
            return accountsGetResponse;
        } catch (IOException e) {
            e.printStackTrace();
            throw new CoynAppException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


    private void saveInstitutionEntity(String institutionId, String institutionName, String accessToken, AppUserEntity appUserEntity) {
        final InstitutionEntity institutionEntity = new InstitutionEntity();
        final String encryptedAccessToken = passwordEncoder.encode(accessToken);
        institutionEntity.setGeneralId(institutionId);
        institutionEntity.setName(institutionName);
        institutionEntity.setEncryptedAccessToken(encryptedAccessToken);
        institutionEntity.setAppUser(appUserEntity);
        final InstitutionEntity savedInstitution = institutionRepository.save(institutionEntity);
        appUserEntity.getInstitutions().add(savedInstitution);
        appUserRepository.save(appUserEntity);
    }

}
