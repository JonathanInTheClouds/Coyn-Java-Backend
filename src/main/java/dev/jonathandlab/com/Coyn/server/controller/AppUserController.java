package dev.jonathandlab.com.Coyn.server.controller;

import dev.jonathandlab.com.Coyn.server.model.TestResponse;
import dev.jonathandlab.com.Coyn.server.model.response.token.ServerTokenResponse;
import dev.jonathandlab.com.Coyn.server.service.device.IDeviceService;
import dev.jonathandlab.com.Coyn.server.service.token.ITokenService;
import dev.jonathandlab.com.Coyn.server.model.entity.user.AppUserEntity;
import dev.jonathandlab.com.Coyn.server.model.request.user.CreateAppUserRequest;
import dev.jonathandlab.com.Coyn.server.service.user.IAppUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

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
        AppUserEntity createdAppUserEntity = appUserService.createAppUser(createAppUserRequest);
        List<SimpleGrantedAuthority> authorities = createdAppUserEntity.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())).toList();
        ServerTokenResponse serverAccessToken = tokenService.createServerTokenResponse(createdAppUserEntity.getEmail(), authorities);
        deviceService.createDevice(createdAppUserEntity);
        return ResponseEntity.status(HttpStatus.OK)
                .body(serverAccessToken);
    }

    @GetMapping("test")
    public ResponseEntity<TestResponse> testResponseResponse() {
        return ResponseEntity.ok(new TestResponse("Hello World"));
    }

}
