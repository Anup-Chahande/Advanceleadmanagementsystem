package com.app.security;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Entity.USER;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(USER user);

    void deleteByUser(USER user);
}