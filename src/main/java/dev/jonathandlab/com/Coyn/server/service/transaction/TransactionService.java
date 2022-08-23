package dev.jonathandlab.com.Coyn.server.service.transaction;

import com.plaid.client.model.Transaction;
import com.plaid.client.model.TransactionsGetRequest;
import com.plaid.client.model.TransactionsGetRequestOptions;
import com.plaid.client.model.TransactionsGetResponse;
import com.plaid.client.request.PlaidApi;
import dev.jonathandlab.com.Coyn.server.exception.CoynAppException;
import dev.jonathandlab.com.Coyn.server.model.entity.account.AccountEntity;
import dev.jonathandlab.com.Coyn.server.model.entity.institution.InstitutionEntity;
import dev.jonathandlab.com.Coyn.server.model.entity.user.AppUserEntity;
import dev.jonathandlab.com.Coyn.server.model.response.transaction.BulkTransactionListResponse;
import dev.jonathandlab.com.Coyn.server.model.response.transaction.TransactionListResponse;
import dev.jonathandlab.com.Coyn.server.service.token.ITokenService;
import dev.jonathandlab.com.Coyn.server.service.user.IAppUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@AllArgsConstructor
@Transactional
public class TransactionService implements ITransactionService {

    private IAppUserService appUserService;
    private ITokenService tokenService;
    private PlaidApi plaidApi;

    @Override
    public Transaction getTransaction(String institutionId, String accountId, String transactionId, String plaidAccessTokens) {
        HashMap<String, String> parsePlaidAccessTokens = tokenService.parsePlaidAccessTokens(plaidAccessTokens);
        String plaidAccessToken = parsePlaidAccessTokens.get(institutionId);
        TransactionsGetRequestOptions transactionsGetRequestOptions = new TransactionsGetRequestOptions();
        transactionsGetRequestOptions.setAccountIds(List.of(accountId));
        TransactionsGetRequest transactionsGetRequest = new TransactionsGetRequest();
        transactionsGetRequest.setAccessToken(plaidAccessToken);
        try {
            TransactionsGetResponse transactionsGetResponse = Optional.ofNullable(plaidApi.transactionsGet(transactionsGetRequest).execute().body())
                    .orElseThrow(() -> new CoynAppException(HttpStatus.BAD_REQUEST, "Transaction Request Failed."));
            List<Transaction> transactions = transactionsGetResponse.getTransactions();
            for (Transaction transaction : transactions) {
                if (transaction.getTransactionId().equals(transactionId)) {
                    return transaction;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BulkTransactionListResponse getTransactions(String plaidAccessTokens, Date startDate, Date endDate) {
        AppUserEntity currentAppUser = appUserService.getCurrentAppUser();
        HashMap<String, String> plaidAccessTokensHashMap = tokenService.parsePlaidAccessTokens(plaidAccessTokens);
        Set<InstitutionEntity> institutions = currentAppUser.getInstitutions();

        List<TransactionListResponse> transactionListResponses = new ArrayList<>();
        for (InstitutionEntity institution : institutions) {
            System.out.println(plaidAccessTokensHashMap.get(institution.getGeneralId()));
            String plaidAccessToken = Optional.ofNullable(plaidAccessTokensHashMap.get(institution.getGeneralId()))
                    .orElseThrow(() -> new CoynAppException(HttpStatus.NOT_FOUND, institution.getGeneralId() + " not found"));
            List<String> accountIds = institutions.stream()
                    .map(InstitutionEntity::getAccounts)
                    .flatMap(Collection::stream)
                    .map(AccountEntity::getAccountId)
                    .toList();

            try {
                LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();


//                LocalDate startDate = simpleDateFormat.parse("2021-01-01").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//                LocalDate endDate = simpleDateFormat.parse("2021-12-10").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                TransactionsGetRequestOptions transactionsGetRequestOptions = new TransactionsGetRequestOptions();
                transactionsGetRequestOptions.setAccountIds(accountIds);

                TransactionsGetRequest request = new TransactionsGetRequest();
                request.setStartDate(start);
                request.setEndDate(end);
                request.setAccessToken(plaidAccessToken);
                request.setOptions(transactionsGetRequestOptions);
                Response<TransactionsGetResponse> response = plaidApi.transactionsGet(request).execute();
                if (!response.isSuccessful()) {
                    continue;
                }
                List<Transaction> bodyTransactions = Optional.ofNullable(Objects.requireNonNull(response.body()).getTransactions())
                        .orElse(new ArrayList<>());
                List<Transaction> transactions = new ArrayList<>(bodyTransactions);

                while (transactions.size() < response.body().getTotalTransactions()) {
                    TransactionsGetRequestOptions options = new TransactionsGetRequestOptions()
                            .offset(transactions.size());

                    request = new TransactionsGetRequest()
                            .accessToken(plaidAccessToken)
                            .startDate(start)
                            .endDate(end)
                            .options(options);

                    response = plaidApi.transactionsGet(request).execute();
                    bodyTransactions = Optional.ofNullable(Objects.requireNonNull(response.body()).getTransactions())
                            .orElse(new ArrayList<>());
                    transactions.addAll(bodyTransactions);
                }

                TransactionListResponse transactionListResponse = new TransactionListResponse(institution.getGeneralId(), transactions);
                transactionListResponses.add(transactionListResponse);
            } catch (Exception e) {
                e.printStackTrace();
                throw new CoynAppException(HttpStatus.NOT_FOUND, institution.getGeneralId() + " not found");
            }
        }
        return new BulkTransactionListResponse(transactionListResponses);
    }

    @Override
    public TransactionListResponse getTransactions(String institutionId, String accountId, String accessTokens) {
        TransactionsGetRequestOptions transactionsGetRequestOptions = new TransactionsGetRequestOptions();
        transactionsGetRequestOptions.setAccountIds(List.of(accountId));
        TransactionsGetRequest transactionsGetRequest = new TransactionsGetRequest();
        HashMap<String, String> plaidAccessTokens = tokenService.parsePlaidAccessTokens(accessTokens);
        String accessToken = plaidAccessTokens.get(institutionId);
        transactionsGetRequest.setAccessToken(accessToken);

        try {
            TransactionsGetResponse transactionsGetResponse = Optional.ofNullable(plaidApi.transactionsGet(transactionsGetRequest).execute().body())
                    .orElseThrow(() -> new CoynAppException(HttpStatus.UNAUTHORIZED, "No Access Token"));
            return new TransactionListResponse(institutionId, transactionsGetResponse.getTransactions());
        } catch (IOException e) {
            e.printStackTrace();
            throw new CoynAppException(HttpStatus.UNAUTHORIZED, "No Access Token");
        }
    }
}
