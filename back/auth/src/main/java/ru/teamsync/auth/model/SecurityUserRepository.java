package ru.teamsync.auth.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecurityUserRepository extends JpaRepository<SecurityUser, Integer> {

    Optional<SecurityUser> findByEmail(String email);

    Optional<SecurityUser> findByExternalUserId(String id);

    boolean existsByEmail(String email);
}
