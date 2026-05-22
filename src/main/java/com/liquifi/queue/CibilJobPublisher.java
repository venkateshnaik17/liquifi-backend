package com.liquifi.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CibilJobPublisher {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper redisObjectMapper;

    @Value("${liquifi.redis.cibil-job-queue}")
    private String jobQueue;

    public void publish(CibilJobPayload payload) {
        try {
            String json = redisObjectMapper.writeValueAsString(payload);
            redisTemplate.opsForList().rightPush(jobQueue, json);
            log.info("Published CIBIL job to Redis queue. SessionId={}", payload.getSessionId());
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize CIBIL job payload for session={}", payload.getSessionId(), e);
            throw new RuntimeException("Failed to enqueue CIBIL job", e);
        }
    }
}
