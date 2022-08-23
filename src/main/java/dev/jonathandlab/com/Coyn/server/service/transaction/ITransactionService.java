package dev.jonathandlab.com.Coyn.server.service.transaction;


import com.plaid.client.model.Transaction;
import dev.jonathandlab.com.Coyn.server.model.response.transaction.BulkTransactionListResponse;
import dev.jonathandlab.com.Coyn.server.model.response.transaction.TransactionListResponse;

import java.util.Date;

public interface ITransactionService {
    BulkTransactionListResponse getTransactions(String accessTokens, Date startDate, Date endDate);
    TransactionListResponse getTransactions(String institutionId, String accountId, String accessTokens);
    Transaction getTransaction(String institutionId, String accountId, String transactionId, String plaidAccessTokens);
}
