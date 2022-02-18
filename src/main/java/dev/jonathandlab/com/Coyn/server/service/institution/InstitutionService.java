package dev.jonathandlab.com.Coyn.server.service.institution;

import com.plaid.client.model.AccountBalance;
import com.plaid.client.model.AccountBase;
import dev.jonathandlab.com.Coyn.server.exception.CoynAppException;
import dev.jonathandlab.com.Coyn.server.model.entity.account.AccountBalanceEntity;
import dev.jonathandlab.com.Coyn.server.model.entity.account.AccountEntity;
import dev.jonathandlab.com.Coyn.server.model.entity.institution.InstitutionEntity;
import dev.jonathandlab.com.Coyn.server.model.entity.user.AppUserEntity;
import dev.jonathandlab.com.Coyn.server.model.request.institution.UpdateInstitutionRequest;
import dev.jonathandlab.com.Coyn.server.model.request.institution.BulkUpdateInstitutionRequest;
import dev.jonathandlab.com.Coyn.server.model.response.institution.BulkUpdateInstitutionResponse;
import dev.jonathandlab.com.Coyn.server.model.response.institution.UpdateInstitutionResponse;
import dev.jonathandlab.com.Coyn.server.repository.AccountRepository;
import dev.jonathandlab.com.Coyn.server.repository.BalanceRepository;
import dev.jonathandlab.com.Coyn.server.repository.InstitutionRepository;
import dev.jonathandlab.com.Coyn.server.service.user.IAppUserService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@AllArgsConstructor
@Transactional
public class InstitutionService implements IInstitutionService {

    private AccountRepository accountRepository;
    private BalanceRepository balanceRepository;
    private IAppUserService appUserService;

    @Override
    public BulkUpdateInstitutionResponse updateInstitution(BulkUpdateInstitutionRequest bulkUpdateInstitutionRequest) {
        final AppUserEntity currentAppUser = appUserService.getCurrentAppUser();
        final Set<InstitutionEntity> appUserInstitutions = currentAppUser.getInstitutions();
        List<UpdateInstitutionResponse> updateInstitutionResponses = new ArrayList<>();
        for (UpdateInstitutionRequest updateInstitutionRequest : bulkUpdateInstitutionRequest.getUpdateInstitutionRequests()) {
            final String institutionId = updateInstitutionRequest.getInstitutionId();
            final InstitutionEntity targetInstitution = appUserInstitutions.stream()
                    .filter(institution -> institution.getGeneralId().equals(institutionId))
                    .findFirst().orElseThrow(() -> {
                        throw new CoynAppException(HttpStatus.NOT_FOUND, institutionId + " not found in database.");
                    });
            final Set<AccountEntity> accounts = targetInstitution.getAccounts();
            final List<AccountBalanceEntity> balances = accounts.stream().map(AccountEntity::getBalance).toList();
            targetInstitution.setAccounts(new HashSet<>());
            balanceRepository.deleteAll(balances);
            accountRepository.deleteAll(accounts);
            // Add New Accounts
            List<String> failedAccounts = new ArrayList<>();
            final List<AccountBase> institutionRequestAccounts = updateInstitutionRequest.getAccounts();
            for (AccountBase accountBase : institutionRequestAccounts) {
                try {
                    saveAccountEntity(accountBase, targetInstitution);
                } catch (Exception e) {
                    failedAccounts.add(accountBase.getAccountId());
                }
            }
            final UpdateInstitutionResponse updateInstitutionResponse = UpdateInstitutionResponse.builder()
                    .institutionId(updateInstitutionRequest.getInstitutionId())
                    .failedAccounts(failedAccounts)
                    .build();
            updateInstitutionResponses.add(updateInstitutionResponse);
        }

        final BulkUpdateInstitutionResponse bulkUpdateInstitutionResponse = new BulkUpdateInstitutionResponse();
        bulkUpdateInstitutionResponse.setUpdateInstitutionResponses(updateInstitutionResponses);
        return bulkUpdateInstitutionResponse;
    }

    private void saveAccountEntity(AccountBase accountBase, InstitutionEntity institution) throws Exception {
        final AccountEntity accountEntity = convertToAccountEntity(accountBase);
        final AccountBalanceEntity accountBalanceEntity = convertToBalanceEntity(accountBase.getBalances());
        accountEntity.setBalance(accountBalanceEntity);
        accountEntity.setInstitution(institution);
        try {
            final AccountEntity saveAccountEntity = accountRepository.save(accountEntity);
            accountBalanceEntity.setAccount(saveAccountEntity);
            balanceRepository.save(accountBalanceEntity);
            institution.getAccounts().add(saveAccountEntity);
        } catch (DataAccessException e) {
            throw new Exception(accountBase.getAccountId());
        }
    }

    private AccountEntity convertToAccountEntity(AccountBase accountBase) {
        final AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountId(accountBase.getAccountId());
        accountEntity.setMask(accountBase.getMask());
        accountEntity.setName(accountBase.getName());
        accountEntity.setOfficialName(accountBase.getOfficialName());
        accountEntity.setType(accountBase.getType().getValue());
        if (Optional.ofNullable(accountBase.getVerificationStatus()).isPresent()) {
            accountEntity.setVerificationStatus(accountBase.getVerificationStatus().getValue());
        }
        if (Optional.ofNullable(accountBase.getSubtype()).isPresent()) {
            accountEntity.setSubType(accountBase.getSubtype().getValue());
        }
        return accountEntity;
    }

    private AccountBalanceEntity convertToBalanceEntity(AccountBalance accountBalance) {
        AccountBalanceEntity accountBalanceEntity = new AccountBalanceEntity();
        accountBalanceEntity.setCurrent(accountBalance.getCurrent());
        accountBalanceEntity.setIsoCurrencyCode(accountBalance.getIsoCurrencyCode());
        accountBalanceEntity.setUnofficialCurrencyCode(accountBalance.getUnofficialCurrencyCode());
        accountBalanceEntity.setLastUpdatedDatetime(accountBalance.getLastUpdatedDatetime());
        return accountBalanceEntity;
    }
}
