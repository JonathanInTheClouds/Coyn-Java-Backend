package dev.jonathandlab.com.Coyn.server.model.response.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerTokenResponse {
    private String serverAccessToken;
    private String serverRefreshToken;
}
