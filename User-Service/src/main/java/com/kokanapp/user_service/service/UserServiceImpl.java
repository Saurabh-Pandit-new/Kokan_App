package com.kokanapp.user_service.service;

import com.kokanapp.user_service.dto.RegisterUserRequest;
import com.kokanapp.user_service.dto.SellerRequestDTO;
import com.kokanapp.user_service.model.Role;
import com.kokanapp.user_service.model.SellerRequestStatus;
import com.kokanapp.user_service.model.User;
import com.kokanapp.user_service.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ✅ Constructor injection (preferred and testable)
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRole(Role.USER);
        user.setSellerStatus(SellerRequestStatus.NONE);
        user.setIsSeller(false);

        return userRepository.save(user);
    }

    @Override
    public void requestSeller(SellerRequestDTO requestDTO) {
        User user = userRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Set sellerRequest to true to indicate the user has made a request to become a seller
        user.setSellerRequest(true);
        
        // Set the seller status to PENDING, indicating the request is under review
        user.setSellerStatus(SellerRequestStatus.PENDING);
        
        // Save the updated user to the database
        userRepository.save(user);
    }


    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    


    // 🔐 Used for authentication
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()  // Add roles/authorities here if needed
        );
    }
    @Override
    public User createAdminUser(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Admin already exists with this email");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRole(Role.ADMIN);  // Important
        user.setIsSeller(false);

        return userRepository.save(user);
    }


    @Override
    public User updateUserProfile(String email, User updatedUser) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update the user fields with provided values
        if (updatedUser.getName() != null) {
            user.setName(updatedUser.getName());
        }
        if (updatedUser.getPhone() != null) {
            user.setPhone(updatedUser.getPhone());
        }
        if (updatedUser.getPassword() != null) {
            // Encode the new password before setting it
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepository.save(user);
    }
    @Override
    public List<User> getAllSellerRequests() {
        return userRepository.findBySellerStatus(SellerRequestStatus.PENDING);
    }


    @Override
    public User updateSellerRequestStatus(Long userId, SellerRequestStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setSellerStatus(status);
        user.setSellerRequest(false);

        if (status == SellerRequestStatus.APPROVED) {
            user.setIsSeller(true);
            user.setRole(Role.SELLER);
            // Generate a unique seller ID (e.g., UUID or custom format)
            user.setSellerId("SELLER-" + UUID.randomUUID().toString().substring(0, 8));
        } else {
            user.setIsSeller(false);
            user.setSellerId(null); // Optional: clear if rejected
        }


        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @Override
    public List<User> getUsersIncludingSellers(boolean includeSellers) {
        if (includeSellers) {
            // Return all users including sellers
            return userRepository.findAll();
        } else {
            // Return only users who are not sellers
            return userRepository.findByIsSeller(false);
        }
    }
    @Override
    public List<User> getUsersExcludingAdmins() {
        return userRepository.findByRoleNot(Role.ADMIN);
    }
    @Override
    public List<User> getNonAdminUsers(boolean includeSellers) {
        List<User> nonAdminUsers = userRepository.findByRoleNot(Role.ADMIN);

        if (!includeSellers) {
            return nonAdminUsers.stream()
                    .filter(user -> !user.isSeller())
                    .collect(Collectors.toList());
        }

        return nonAdminUsers;
    }

	@Override
	public List<User> getUsersFiltered(boolean includeSellers) {
		// TODO Auto-generated method stub
		return null;
	}

	public Optional<User> findById(Long id) {
        // Find user by ID using the repository
        return userRepository.findById(id);
    }






}
