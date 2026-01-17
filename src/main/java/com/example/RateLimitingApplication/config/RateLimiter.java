package com.example.RateLimitingApplication.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimiter {

    @Value("${spring.redis.url}")
    private String redisUrl;

    @Value("${app.rate-limit.capacity}")
    private int capacity;

    @Value("${app.rate-limit.refill-tokens}")
    private int refillTokens;

    @Value("${app.rate-limit.refill-minutes}")
    private int refillMinutes;

    //Bucket Configuration
    @Bean
    public BucketConfiguration bucketConfiguration(){
        return BucketConfiguration.builder()
                .addLimit(Bandwidth.classic(capacity,
                        Refill.intervally(refillTokens,Duration.ofMinutes(refillMinutes))))
                .build();

    }

    @Bean
    public ProxyManager<String> lettuceProxyManager(){
        // 1 Connect to upstash (Use your URL from the Upstash console)
       // String redisURL = "rediss://default:AXwJAAIncDExODM0NWVmMzkyMGI0YzQwYjA0MzNmMGZmZjRiMmU1M3AxMzE3NTM@true-mutt-31753.upstash.io:6379";
        RedisClient redisClient = RedisClient.create(redisUrl);
        // Open a connection that speaks bytes (so we can save Java Objects)
        StatefulRedisConnection<String, byte[]> connection = redisClient.connect(
                RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));
        //Create the ProxyManager (The remote control)
        return LettuceBasedProxyManager.builderFor(connection).build();
    }
}
// token - AXwJAAIncDExODM0NWVmMzkyMGI0YzQwYjA0MzNmMGZmZjRiMmU1M3AxMzE3NTM
