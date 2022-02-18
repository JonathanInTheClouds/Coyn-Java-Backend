package dev.jonathandlab.com.Coyn.server.service.institution;

import dev.jonathandlab.com.Coyn.server.model.request.institution.BulkUpdateInstitutionRequest;
import dev.jonathandlab.com.Coyn.server.model.response.institution.BulkUpdateInstitutionResponse;

public interface IInstitutionService {
    BulkUpdateInstitutionResponse updateInstitution(BulkUpdateInstitutionRequest bulkUpdateInstitutionRequest);
}
