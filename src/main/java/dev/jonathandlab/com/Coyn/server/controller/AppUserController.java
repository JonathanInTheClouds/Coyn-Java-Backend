package dev.jonathandlab.com.Coyn.server.controller;

import dev.jonathandlab.com.Coyn.server.model.response.token.ServerTokenResponse;
import dev.jonathandlab.com.Coyn.server.service.device.IDeviceService;
import dev.jonathandlab.com.Coyn.server.service.token.ITokenService;
import dev.jonathandlab.com.Coyn.server.model.entity.user.AppUser;
import dev.jonathandlab.com.Coyn.server.model.request.user.CreateAppUserRequest;
import dev.jonathandlab.com.Coyn.server.service.user.IAppUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class AppUserController {

    private IAppUserService appUserService;
    private ITokenService tokenService;
    private IDeviceService deviceService;

    /**
     * Create App User
     * @param createAppUserRequest CreateAppUserRequest class containing email and raw password
     * @return ServerTokenResponse class containing serverAccessToken and serverRefreshToken wrapped with ResponseEntity
     */
    @PostMapping("signup")
    public ResponseEntity<ServerTokenResponse> createAppUser(@RequestBody CreateAppUserRequest createAppUserRequest) {
        AppUser createdAppUser = appUserService.createAppUser(createAppUserRequest);
        List<SimpleGrantedAuthority> authorities = createdAppUser.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())).toList();
        ServerTokenResponse serverAccessToken = tokenService.createServerTokenResponse(createdAppUser.getEmail(), authorities);
        deviceService.createDevice(createdAppUser);
        return ResponseEntity.status(HttpStatus.OK)
                .body(serverAccessToken);
    }

}
