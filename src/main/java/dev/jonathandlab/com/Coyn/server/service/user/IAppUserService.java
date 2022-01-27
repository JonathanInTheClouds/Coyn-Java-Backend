package dev.jonathandlab.com.Coyn.server.service.user;

import dev.jonathandlab.com.Coyn.server.model.entity.user.AppUser;
import dev.jonathandlab.com.Coyn.server.model.request.user.CreateAppUserRequest;

public interface IAppUserService {
    AppUser createAppUser(CreateAppUserRequest createAppUserRequest);
    AppUser getCurrentAppUser();
}
