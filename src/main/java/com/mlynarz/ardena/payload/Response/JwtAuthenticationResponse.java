package com.mlynarz.ardena.payload.Response;

import com.mlynarz.ardena.payload.Response.RoleResponse;

import java.util.List;

public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";

    private List<RoleResponse> roles;

    public JwtAuthenticationResponse(String accessToken, List<RoleResponse> roles) {
        this.accessToken = accessToken;
        this.roles=roles;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public List<RoleResponse> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleResponse> roles) {
        this.roles = roles;
    }
}
