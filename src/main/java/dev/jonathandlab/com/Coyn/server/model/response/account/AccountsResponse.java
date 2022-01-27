package dev.jonathandlab.com.Coyn.server.model.response.account;

import com.plaid.client.model.AccountBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountsResponse {
    private String institutionName;
    private String accountAccessToken;
    private List<AccountBase> accounts;
}
