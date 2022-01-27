package dev.jonathandlab.com.Coyn.server.model.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginAppUserRequest {
    private String email;
    private String password;
}
