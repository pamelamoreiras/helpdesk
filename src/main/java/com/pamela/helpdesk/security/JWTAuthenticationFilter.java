package com.pamela.helpdesk.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pamela.helpdesk.domain.dtos.CredenciaisDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JWTUtill jwtUtill;

    public JWTAuthenticationFilter(final AuthenticationManager authenticationManager, final JWTUtill jwtUtill) {
        this.authenticationManager = authenticationManager;
        this.jwtUtill = jwtUtill;
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) throws AuthenticationException {
        try{
            final CredenciaisDTO creds = new ObjectMapper().readValue(request.getInputStream(), CredenciaisDTO.class);
            final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getSenha(), new ArrayList<>());
            final Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return authentication;
        } catch (final Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain, final Authentication authResult) throws IOException, ServletException {
        final String userName = ((UserSS) authResult.getPrincipal()).getUsername();
        final String token = jwtUtill.generateToken(userName);
        response.setHeader("Access-control-expose-headers", "Authorization");
        response.setHeader("Authorization","Bearer" + token);
    }

    @Override
    protected void unsuccessfulAuthentication(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(401);
        response.setContentType("application/json");
        response.getWriter().append(json());
    }

    private static CharSequence json() {
        final long date = new Date().getTime();
        return "{"
                + "\"timestamp\": " + date + ", "
                + "\"status\": 401, "
                + "\"error\": \"Não autorizado\", "
                + "\"message\": \"Email ou senha inválidos\", "
                + "\"path\": \"/login\"}";
    }
}
