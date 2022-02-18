package dev.jonathandlab.com.Coyn.server.repository;

import dev.jonathandlab.com.Coyn.server.model.entity.institution.InstitutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionRepository extends JpaRepository<InstitutionEntity, Long> {
}
