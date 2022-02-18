package dev.jonathandlab.com.Coyn.server.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jonathandlab.com.Coyn.server.model.response.token.ServerTokenResponse;
import dev.jonathandlab.com.Coyn.server.model.entity.user.AppUserEntity;
import dev.jonathandlab.com.Coyn.server.model.entity.user.AppUserRoleEntity;
import dev.jonathandlab.com.Coyn.server.model.request.user.LoginAppUserRequest;
import dev.jonathandlab.com.Coyn.server.repository.AppUserRepository;
import dev.jonathandlab.com.Coyn.server.service.token.ITokenService;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final AppUserRepository appUserRepository;
    private final ITokenService tokenService;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, AppUserRepository appUserRepository, ITokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.appUserRepository = appUserRepository;
        this.tokenService = tokenService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            LoginAppUserRequest loginAppUserRequest = objectMapper.readValue(request.getInputStream(), LoginAppUserRequest.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginAppUserRequest.getEmail(), loginAppUserRequest.getPassword());
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("", "");
            return authenticationManager.authenticate(authenticationToken);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        User principal = (User) authResult.getPrincipal();
        AppUserEntity appUserEntity = appUserRepository.findAppUserByEmail(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User " + principal.getUsername() + " not found"));
        List<SimpleGrantedAuthority> authorities = appUserEntity.getRoles().stream().map(AppUserRoleEntity::getName)
                .map(SimpleGrantedAuthority::new).toList();
        ServerTokenResponse serverTokenResponse = tokenService.createServerTokenResponse(appUserEntity.getEmail(), authorities);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), serverTokenResponse);
    }
}
