package dev.jonathandlab.com.Coyn.server.model.request.institution;

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
public class UpdateInstitutionRequest {
    private String institutionId;
    private List<AccountBase> accounts;
}
