package dev.jonathandlab.com.Coyn.server.model.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAppUserRequest {

    private String email;

    private String rawPassword;

}
