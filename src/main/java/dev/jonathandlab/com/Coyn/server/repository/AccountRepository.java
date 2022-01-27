package dev.jonathandlab.com.Coyn.server.repository;

import dev.jonathandlab.com.Coyn.server.model.entity.account.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
}
