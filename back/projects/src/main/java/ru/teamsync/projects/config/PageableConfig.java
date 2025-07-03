package ru.teamsync.projects.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.teamsync.projects.config.properties.PaginationDefaultProperties;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class PageableConfig implements WebMvcConfigurer {

    private final PaginationDefaultProperties paginationDefaultProperties;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver();
        pageableResolver.setFallbackPageable(PageRequest.of(paginationDefaultProperties.pageNumber(), paginationDefaultProperties.pageSize()));
        resolvers.add(pageableResolver);
    }
}