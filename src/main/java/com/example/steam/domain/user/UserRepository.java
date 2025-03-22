package com.example.steam.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
//    Optional<UserEntity> findByUsername(String username);
    boolean existsById(Long userId);
}
