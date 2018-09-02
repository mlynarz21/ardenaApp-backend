package com.capgemini.parnextgen.payload.Response;
import com.capgemini.parnextgen.model.RoleName;

public class RoleResponse {

    private RoleName name;

    public RoleResponse() {

    }

    public RoleResponse(RoleName name) {
        this.name = name;
    }

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }

}
