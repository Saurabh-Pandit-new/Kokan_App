package com.kokanapp.user_service.controller;


import com.kokanapp.user_service.dto.RegisterUserRequest;
import com.kokanapp.user_service.model.Role;
import com.kokanapp.user_service.model.SellerRequestStatus;
import com.kokanapp.user_service.model.User;
import com.kokanapp.user_service.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterUserRequest request) {
        try {
            request.setRole(Role.ADMIN);
            User createdAdmin = userService.createAdminUser(request);
            return ResponseEntity.ok(createdAdmin);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Admin registration failed: " + e.getMessage());
        }
    }

    @GetMapping("/seller-requests")
    public ResponseEntity<?> getAllSellerRequests() {
        try {
            List<User> pendingRequests = userService.getAllSellerRequests();
            return ResponseEntity.ok(pendingRequests);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/seller-requests/{userId}")
    public ResponseEntity<?> updateSellerRequestStatus(@PathVariable Long userId,
                                                       @RequestParam SellerRequestStatus status) {
        try {
            User updatedUser = userService.updateSellerRequestStatus(userId, status);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Update failed: " + e.getMessage());
        }
    }

    @GetMapping("/allusers")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching users: " + e.getMessage());
        }
    }
    @GetMapping("/getUsers")
    public ResponseEntity<?> getUsers(@RequestParam boolean includeSellers) {
        List<User> users = userService.getUsersIncludingSellers(includeSellers);
        return ResponseEntity.ok(users);
    }
    @GetMapping("/users/non-admin")
    public ResponseEntity<List<User>> getNonAdminUsers(@RequestParam(defaultValue = "true") boolean includeSellers) {
        return ResponseEntity.ok(userService.getNonAdminUsers(includeSellers));
    }



}

