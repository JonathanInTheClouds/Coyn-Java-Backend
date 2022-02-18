package dev.jonathandlab.com.Coyn.server.controller;

import dev.jonathandlab.com.Coyn.server.model.request.institution.BulkUpdateInstitutionRequest;
import dev.jonathandlab.com.Coyn.server.model.response.institution.BulkUpdateInstitutionResponse;
import dev.jonathandlab.com.Coyn.server.service.institution.IInstitutionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = {"/institution"})
public class InstitutionController {

    IInstitutionService iInstitutionService;

    @PutMapping
    public ResponseEntity<BulkUpdateInstitutionResponse> bulkUpdateInstitution(@RequestBody BulkUpdateInstitutionRequest bulkUpdateInstitutionRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(iInstitutionService.updateInstitution(bulkUpdateInstitutionRequest));
    }
}
