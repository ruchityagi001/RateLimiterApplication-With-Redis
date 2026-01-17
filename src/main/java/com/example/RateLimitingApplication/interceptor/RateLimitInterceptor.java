package com.example.RateLimitingApplication.interceptor;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private ProxyManager<String> proxyManager;

    @Autowired
    private BucketConfiguration bucketConfiguration;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        // Step 1 Identify the user by IP Addr
        String key = request.getRemoteAddr();

        // Step 2 Resolve the bucket from Redis (Find existing or create new)
        Bucket bucket = proxyManager.builder().build(key, () -> bucketConfiguration);

        //STep 3  Attempt to Consume the token
        if (bucket.tryConsume(1)) {
            return true;
        }
        else{
        // Reject the request
        response.setStatus(429); //too many  requests
        response.getWriter().write("Too many requests! Try again later");
        return false; // stop the request here
        }
    }
}
