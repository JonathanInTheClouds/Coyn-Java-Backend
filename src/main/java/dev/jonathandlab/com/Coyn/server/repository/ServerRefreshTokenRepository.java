package dev.jonathandlab.com.Coyn.server.repository;

import dev.jonathandlab.com.Coyn.server.model.entity.token.ServerRefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerRefreshTokenRepository extends JpaRepository<ServerRefreshTokenEntity, Long> {
}
