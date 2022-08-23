package dev.jonathandlab.com.Coyn.server.controller;

import com.plaid.client.model.Transaction;
import dev.jonathandlab.com.Coyn.server.model.response.transaction.BulkTransactionListResponse;
import dev.jonathandlab.com.Coyn.server.model.response.transaction.TransactionListResponse;
import dev.jonathandlab.com.Coyn.server.service.transaction.ITransactionService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@AllArgsConstructor
@RequestMapping(value = {"/institution"})
public class TransactionController {

    private ITransactionService transactionService;

    @GetMapping("/account/transaction")
    public ResponseEntity<BulkTransactionListResponse> getTransactions(
            @RequestHeader("plaid-access-tokens") String plaidAccessTokens,
            @RequestParam("start-date") @DateTimeFormat(pattern = "MM/dd/yyyy") Date startDate,
            @RequestParam("end-date") @DateTimeFormat(pattern = "MM/dd/yyyy") Date endDate
            ) {
        return ResponseEntity.ok(transactionService.getTransactions(plaidAccessTokens, startDate, endDate));
    }

    @GetMapping("/{institutionId}/account/{accountId}/transaction")
    public ResponseEntity<TransactionListResponse> getTransactions(@PathVariable(required = false) String institutionId,
                                                                   @PathVariable String accountId,
                                                                   @RequestHeader("plaid-access-tokens") String plaidAccessTokens) {
        return ResponseEntity.ok(transactionService.getTransactions(institutionId, accountId, plaidAccessTokens));
    }

    @GetMapping("/{institutionId}/account/{accountId}/transaction/{transactionId}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable String institutionId,
                                                      @PathVariable String accountId,
                                                      @PathVariable String transactionId,
                                                      @RequestHeader("plaid-access-tokens") String plaidAccessTokens) {
        return ResponseEntity.ok(transactionService.getTransaction(institutionId, accountId, transactionId, plaidAccessTokens));
    }

}
