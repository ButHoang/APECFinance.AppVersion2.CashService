package com.apec_finance.cash.service;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;




@Component
public class KeycloakService {
    public Long getInvestorIdFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("No authentication found");
        }

        Jwt jwt = (Jwt) authentication.getCredentials();
        Long investorId = (Long) jwt.getClaims().get("investorId");
        return investorId;
    }
}


