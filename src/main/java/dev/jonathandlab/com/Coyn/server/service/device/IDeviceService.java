package dev.jonathandlab.com.Coyn.server.service.device;

import dev.jonathandlab.com.Coyn.server.model.entity.user.AppUserEntity;

public interface IDeviceService {
    void verifyDevice(AppUserEntity user);
    void createDevice(AppUserEntity user);
}
