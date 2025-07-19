package ru.teamsync.auth.services.jwt;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.teamsync.auth.config.MapstructConfig;
import ru.teamsync.auth.model.SecurityUser;

@Mapper(config = MapstructConfig.class)
public interface JwtUserClaimsMapper {

    @Mapping(target = "internalId", source = "internalUserId")
    @Mapping(target = "roles", expression = "java(List.of(securityUser.getRole()))")
    JwtUserClaims toClaims(SecurityUser securityUser);

}
