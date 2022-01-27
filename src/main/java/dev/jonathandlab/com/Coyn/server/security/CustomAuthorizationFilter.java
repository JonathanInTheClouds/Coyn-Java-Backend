package dev.jonathandlab.com.Coyn.server.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jonathandlab.com.Coyn.server.exception.model.CoynAppExceptionResponse;
import dev.jonathandlab.com.Coyn.server.service.token.ITokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final ITokenService tokenService;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy");

    public CustomAuthorizationFilter(ITokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ClearedServerRequest clearedServerRequest = new ClearedServerRequest(request);

        if (clearedServerRequest.isClearedRequest()) {
            filterChain.doFilter(request, response);
        } else  {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring("Bearer ".length());
                UsernamePasswordAuthenticationToken authenticationToken = tokenService.validateServerToken(token);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);
            } else {
                String errorMessage = "Invalid Server Authorization";
                response.setHeader("Error", errorMessage);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
                CoynAppExceptionResponse coynAppExceptionResponse = new CoynAppExceptionResponse(errorMessage, simpleDateFormat.format(new Date()));
                new ObjectMapper().writeValue(response.getOutputStream(), coynAppExceptionResponse);
            }
        }
    }
}
