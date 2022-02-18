package dev.jonathandlab.com.Coyn.server.repository;

import dev.jonathandlab.com.Coyn.server.model.entity.user.AppUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUserEntity, Long> {
    Optional<AppUserEntity> findAppUserByEmail(String email);
    boolean existsAppUserByEmail(String email);

}
