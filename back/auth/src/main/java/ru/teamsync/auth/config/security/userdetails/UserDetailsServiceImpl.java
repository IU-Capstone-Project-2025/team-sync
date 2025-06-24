package ru.teamsync.auth.config.security.userdetails;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.teamsync.auth.model.SecurityUser;
import ru.teamsync.auth.model.SecurityUserRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SecurityUserRepository securityUserRepository;
    private final UserDetailsMapper userDetailsMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<SecurityUser> userOpt = securityUserRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException(email);
        }

        var securityUser = userOpt.get();
        return userDetailsMapper.toUserDetails(securityUser);
    }
}
