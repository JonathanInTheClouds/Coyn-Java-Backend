package dev.jonathandlab.com.Coyn.server.model.response.institution;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkUpdateInstitutionResponse {
    private List<UpdateInstitutionResponse> updateInstitutionResponses = new ArrayList<>();
}
