package trafficmanagement.traffic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import trafficmanagement.traffic.service.TrafficService;

@RestController
@RequestMapping("/api/traffic")
public class TrafficController {
    
    @Autowired
    private TrafficService trafficService;

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("Traffic management service is working");
    }

    @GetMapping("/limite")
    public ResponseEntity<String> checkRateLimite() {
        return ResponseEntity.ok(trafficService.checkRateLimite());
    }
}
