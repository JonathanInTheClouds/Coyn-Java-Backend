package dev.jonathandlab.com.Coyn.server.service.device;

import dev.jonathandlab.com.Coyn.server.model.entity.user.AppUser;

public interface IDeviceService {
    void verifyDevice(AppUser user);
    void createDevice(AppUser user);
}
