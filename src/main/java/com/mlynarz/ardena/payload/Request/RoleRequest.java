package com.mlynarz.ardena.payload.Request;
import com.mlynarz.ardena.model.RoleName;

public class RoleRequest{

    private RoleName name;

    public RoleRequest() {

    }

    public RoleRequest(RoleName name) {
        this.name = name;
    }

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }

}
