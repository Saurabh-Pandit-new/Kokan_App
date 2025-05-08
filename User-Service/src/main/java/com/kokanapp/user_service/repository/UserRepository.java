package com.kokanapp.user_service.repository;


import com.kokanapp.user_service.model.Role;
import com.kokanapp.user_service.model.SellerRequestStatus;
import com.kokanapp.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findBySellerStatus(SellerRequestStatus status);
    List<User> findByIsSeller(boolean isSeller);
    List<User> findByRoleNot(Role role);

}

