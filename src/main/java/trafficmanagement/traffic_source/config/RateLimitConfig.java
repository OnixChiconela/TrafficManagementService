package trafficmanagement.traffic_source.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

@Configuration
public class RateLimitConfig {
    
    @Bean
    public Bucket createBucket() {
        Refill refill = Refill.intervally(10, Duration.ofSeconds(30));
        Bandwidth limit = Bandwidth.classic(10, refill);
        return Bucket.builder()
            .addLimit(limit)
            .build();
    }
}
