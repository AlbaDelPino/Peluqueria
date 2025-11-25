package com.example.demo.controller;

import com.example.demo.domain.Admin;
import com.example.demo.domain.User;
import com.example.demo.security.service.AdminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public List<User> getAllAdmins() {
        return adminService.getAllAdmins();
    }



    @PutMapping("/{id}")
    public Admin updateAdmin(@PathVariable Long id, @RequestBody Admin admin) {
        return (Admin) adminService.updateAdmin(id, admin);
    }


    @DeleteMapping("/{id}")
    public boolean deleteAdmin(@PathVariable Long id) {
        return adminService.deleteAdmin(id);
    }
}
