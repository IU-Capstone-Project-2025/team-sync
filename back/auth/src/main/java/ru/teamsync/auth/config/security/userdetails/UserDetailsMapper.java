package ru.teamsync.auth.config.security.userdetails;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.teamsync.auth.config.MapstructConfig;
import ru.teamsync.auth.model.SecurityUser;

@Mapper(config = MapstructConfig.class)
public interface UserDetailsMapper {

    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "internalId", source = "internalUserId")
    @Mapping(target = "roles", expression = "java(List.of(user.getRole()))")
    UserDetailsImpl toUserDetails(SecurityUser user);

}
