package dev.jonathandlab.com.Coyn.server.model.request.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerTokenRefreshRequest {
    private String refreshToken;
}
