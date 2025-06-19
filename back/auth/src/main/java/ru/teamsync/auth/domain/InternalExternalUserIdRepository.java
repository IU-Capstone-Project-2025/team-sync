package ru.teamsync.auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InternalExternalUserIdRepository extends JpaRepository<InternalExternalUserIdKey, InternalExternalUserId> {

    boolean existsByExternalId(String externalId);
}
