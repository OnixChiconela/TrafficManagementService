package trafficmanagement.traffic_source.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.common.ratelimiter.configuration.RateLimiterConfigCustomizer;

@Configuration
public class Resilience4jConfig {

    @Bean
    public RateLimiterConfigCustomizer defaultCustomizer() {
        return RateLimiterConfigCustomizer.of("default", builder ->
            builder.limitRefreshPeriod(Duration.ofMinutes(1))
                .limitForPeriod(10)
        );
    }
}
