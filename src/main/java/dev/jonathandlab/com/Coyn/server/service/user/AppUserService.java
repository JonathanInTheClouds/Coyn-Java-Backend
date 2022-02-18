package dev.jonathandlab.com.Coyn.server.service.user;

import dev.jonathandlab.com.Coyn.server.exception.CoynAppException;
import dev.jonathandlab.com.Coyn.server.model.entity.user.AppUserEntity;
import dev.jonathandlab.com.Coyn.server.model.entity.user.AppUserRoleEntity;
import dev.jonathandlab.com.Coyn.server.model.request.user.CreateAppUserRequest;
import dev.jonathandlab.com.Coyn.server.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@Service
@AllArgsConstructor
public class AppUserService implements IAppUserService, UserDetailsService {

    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public AppUserEntity createAppUser(CreateAppUserRequest createAppUserRequest) {
        String email = createAppUserRequest.getEmail();
        if (appUserRepository.existsAppUserByEmail(email)) {
            String errorMessage = createAppUserRequest.getEmail() + " already in exist in database";
            throw new CoynAppException(HttpStatus.CONFLICT, errorMessage);
        } else {
            String rawPassword = createAppUserRequest.getRawPassword();
            String encryptedPassword = passwordEncoder.encode(rawPassword);
            AppUserRoleEntity roleUser = new AppUserRoleEntity(null, "ROLE_USER");
            AppUserEntity unsavedAppUserEntity = AppUserEntity.builder()
                    .email(email)
                    .encryptedPassword(encryptedPassword)
                    .roles(new HashSet<>(List.of(roleUser)))
                    .build();
            return appUserRepository.save(unsavedAppUserEntity);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUserEntity appUserEntity = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException(email + " not found in database");
                });
        return new User(appUserEntity.getEmail(), appUserEntity.getEncryptedPassword(), new ArrayList<>());
    }

    @Override
    public AppUserEntity getCurrentAppUser() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return appUserRepository.findAppUserByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException(username + " not found in database"));
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (!(authentication instanceof AnonymousAuthenticationToken)) {
//            String currentUserName = authentication.getName();
//            return appUserRepository.findAppUserByEmail(currentUserName)
//                    .orElseThrow(() -> new UsernameNotFoundException(currentUserName + " not found in database"));
//        }
//        throw new UsernameNotFoundException("User not found in database");
    }
}
