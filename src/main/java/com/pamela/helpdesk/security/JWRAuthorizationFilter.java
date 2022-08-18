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

    private final JWTUtill jwtUtill;
    private final UserDetailsService userDetailsService;

    public JWRAuthorizationFilter(final AuthenticationManager authenticationManager, final JWTUtill jwtUtill, final UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.jwtUtill = jwtUtill;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")){
            final UsernamePasswordAuthenticationToken authToken = getAuthentication(header.substring(7));

            if (authToken != null){
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(final String token) {
        if(jwtUtill.tokenValido(token)){
            final String username = jwtUtill.getUsername(token);
            final UserDetails details = userDetailsService.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(details.getUsername(), null,details.getAuthorities());
        }
        return null;
    }
}
