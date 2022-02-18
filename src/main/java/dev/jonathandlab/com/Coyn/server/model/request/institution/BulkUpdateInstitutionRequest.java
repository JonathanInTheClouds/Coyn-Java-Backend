package dev.jonathandlab.com.Coyn.server.model.request.institution;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkUpdateInstitutionRequest {
    private List<UpdateInstitutionRequest> updateInstitutionRequests;
}
