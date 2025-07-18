package ru.teamsync.recommendation.config;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;
import org.springframework.context.annotation.Configuration;

@MapperConfig(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public class MapstructConfig {
}
