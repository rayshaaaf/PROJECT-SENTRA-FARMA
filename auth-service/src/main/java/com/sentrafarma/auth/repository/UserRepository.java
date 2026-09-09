package com.sentrafarma.auth.repository;

import com.sentrafarma.auth.entity.Role;
import com.sentrafarma.auth.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndIsDeletedFalse(String email);

    boolean existsByEmailAndIsDeletedFalse(String email);

    Page<User> findByIsDeletedFalse(Pageable pageable);

    Page<User> findByRoleAndIsDeletedFalse(Role role, Pageable pageable);

    Page<User> findByNamaLengkapContainingIgnoreCaseAndIsDeletedFalse(String keyword, Pageable pageable);
}
