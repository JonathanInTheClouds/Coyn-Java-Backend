package dev.jonathandlab.com.Coyn.server.model.response.transaction;

import com.plaid.client.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionListResponse {
    private String institutionId;
    private List<Transaction> transactions;
}
