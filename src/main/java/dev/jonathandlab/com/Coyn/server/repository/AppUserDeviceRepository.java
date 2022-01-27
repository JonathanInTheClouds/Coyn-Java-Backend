package dev.jonathandlab.com.Coyn.server.repository;

import dev.jonathandlab.com.Coyn.server.model.entity.user.AppUserDevice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserDeviceRepository extends JpaRepository<AppUserDevice, Long> {
}
