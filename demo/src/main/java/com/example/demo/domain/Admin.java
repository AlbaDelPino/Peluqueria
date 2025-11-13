package com.example.demo.domain;

import jakarta.persistence.Column;

public class Admin extends User{
    @Column(name = "especialiad")
    private String especialiad;

    public Admin(String username, String email, String password, ERole role, String especialiad) {
        super(username, email, password, role.ROLE_ADMIN);
        this.especialiad = especialiad;
    }

    public String getEspecialiad() {
        return especialiad;
    }

    public void setEspecialiad(String especialiad) {
        this.especialiad = especialiad;
    }


}
