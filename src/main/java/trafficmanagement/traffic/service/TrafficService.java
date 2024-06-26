package trafficmanagement.traffic.service;

import java.time.Duration;

import org.springframework.stereotype.Service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

@Service
public class TrafficService {
    
    private final Bucket bucket;

    public TrafficService() {
        Refill refill = Refill.intervally(10, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(10, refill);
        this.bucket = Bucket.builder()
            .addLimit(limit)
            .build();
    }

    public String checkRateLimite() {
        if (bucket.tryConsume(1)) {
            return "solicitation is inside limite";
        } else {
            return "Limite was exceeded";
        }
    }
}
