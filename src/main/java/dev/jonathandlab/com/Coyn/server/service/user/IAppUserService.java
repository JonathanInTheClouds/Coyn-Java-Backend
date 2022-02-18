package dev.jonathandlab.com.Coyn.server.service.user;

import dev.jonathandlab.com.Coyn.server.model.entity.user.AppUserEntity;
import dev.jonathandlab.com.Coyn.server.model.request.user.CreateAppUserRequest;

public interface IAppUserService {
    AppUserEntity createAppUser(CreateAppUserRequest createAppUserRequest);
    AppUserEntity getCurrentAppUser();
}
