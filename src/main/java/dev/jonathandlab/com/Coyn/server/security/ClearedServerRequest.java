package dev.jonathandlab.com.Coyn.server.security;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ClearedServerRequest {

    private final HttpServletRequest request;

    public ClearedServerRequest(HttpServletRequest request) {
        this.request = request;
    }

    public Boolean isClearedRequest() {
        List<Boolean> clearedRequest = List.of(
                request.getServletPath().equals("/token/plaid/link"),
                request.getServletPath().equals("/token/plaid/link/exchange"),
                request.getServletPath().equals("/token/server/refresh"),
                request.getServletPath().equals("/user/signup"),
                request.getServletPath().equals("/user/signin"),
                request.getServletPath().startsWith("/h2-console")
        );
        for (Boolean aBoolean : clearedRequest) {
            if (aBoolean) {
                return true;
            }
        }
        return false;
    }

}
