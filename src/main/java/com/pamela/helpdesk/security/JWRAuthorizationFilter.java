package com.pamela.helpdesk.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWRAuthorizationFilter extends BasicAuthenticationFilter {

    private JWTUtill jwtUtill;
    private UserDetailsService userDetailsService;

    public JWRAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtill jwtUtill, UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.jwtUtill = jwtUtill;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")){
            UsernamePasswordAuthenticationToken authToken = getAuthentication(header.substring(7));

            if (authToken != null){
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        if(jwtUtill.tokenValido(token)){
            String username = jwtUtill.getUsername(token);
            UserDetails details = userDetailsService.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(details.getUsername(), null,details.getAuthorities());
        }
        return null;
    }
}
