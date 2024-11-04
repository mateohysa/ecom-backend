package com.mateo.ecom.backend.api.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.mateo.ecom.backend.models.AppUser;
import com.mateo.ecom.backend.models.dao.UserRepository;
import com.mateo.ecom.backend.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private JWTService jwtService;
    private UserRepository UserRepository;
    public JWTFilter(JWTService jwtService, UserRepository UserRepository) {
        this.jwtService = jwtService;
        this.UserRepository = UserRepository;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //request comes in
        //we see if the request has a header = Authorization
        String tokenheader = request.getHeader("Authorization");
        //check if the token header starts with bearer
        if (tokenheader != null && tokenheader.startsWith("Bearer ")) {
            //if so we chop the bearer part off
            String token = tokenheader.substring(7);
            try {
                //we try to decode the token and get the username
                String username = jwtService.findUsername(token);
                //take the username and try to find the user in the DB
                Optional<AppUser> optUser = UserRepository.findByUsernameLikeIgnoreCase(username);
                if (optUser.isPresent()) {
                    AppUser appUser = optUser.get();
                    //if the user is present we build up the authenticaion object of the user
                    UsernamePasswordAuthenticationToken token1 = new UsernamePasswordAuthenticationToken(appUser, null, new ArrayList<>());
                    //set some extra details to the auth obj so that the
                    //web auth knows about the req
                    token1.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    //the context is set to have the auth that we just created (technically full permissions but we dont have permissions implemented).
                    SecurityContextHolder.getContext().setAuthentication(token1);
                }
                //if we cant decode the token to get the username we throw this exception
            }catch (JWTDecodeException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or null token");
                return;
            }
        }
        filterChain.doFilter(request, response);

    }
}
