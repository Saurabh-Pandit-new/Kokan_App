package com.kokanapp.user_service.service;


import java.util.List;
import java.util.Optional;

import com.kokanapp.user_service.dto.RegisterUserRequest;
import com.kokanapp.user_service.dto.SellerRequestDTO;
import com.kokanapp.user_service.model.SellerRequestStatus;
import com.kokanapp.user_service.model.User;

public interface UserService {
    User registerUser(RegisterUserRequest request);
    User createAdminUser(RegisterUserRequest request);
    void requestSeller(SellerRequestDTO requestDTO);
    User getUserByEmail(String email);
    User updateUserProfile(String email, User updatedUser);
    List<User> getAllSellerRequests();  // to list all users with PENDING seller request
    User updateSellerRequestStatus(Long userId, SellerRequestStatus status);  // approve/reject
    List<User> getAllUsers();
    List<User> getUsersIncludingSellers(boolean includeSellers);
    public List<User> getUsersExcludingAdmins();
    public List<User> getUsersFiltered(boolean includeSellers);
    List<User> getNonAdminUsers(boolean includeSellers);
	Optional<User> findById(Long id);


}

