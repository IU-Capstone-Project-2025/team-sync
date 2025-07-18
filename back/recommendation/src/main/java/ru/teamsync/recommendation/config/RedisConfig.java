package ru.teamsync.recommendation.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import ru.teamsync.recommendation.model.ProjectScore;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, ProjectScore> projectScoreRedisTemplate(RedisConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        RedisTemplate<String, ProjectScore> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());

        var projectScoreSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, ProjectScore.class);
        template.setValueSerializer(projectScoreSerializer);

        template.afterPropertiesSet();

        return template;
    }

    @Bean
    public ListOperations<String, ProjectScore> projectScoreListOperations(RedisTemplate<String, ProjectScore> redisTemplate) {
        return redisTemplate.opsForList();
    }
}