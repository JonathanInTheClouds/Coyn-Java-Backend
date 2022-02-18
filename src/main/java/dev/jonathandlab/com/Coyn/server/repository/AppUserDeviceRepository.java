package dev.jonathandlab.com.Coyn.server.repository;

import dev.jonathandlab.com.Coyn.server.model.entity.user.AppUserDeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserDeviceRepository extends JpaRepository<AppUserDeviceEntity, Long> {
}
