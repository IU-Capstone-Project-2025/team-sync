package ru.teamsync.projects.service.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityContextService {

    public Long getCurrentUserId() {
        return getCurrentUser().internalId();
    }

    public Long getCurrentUserProfileId() {
        return getCurrentUser().profileId();
    }

    private SecurityUser getCurrentUser() {
        return (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


}
