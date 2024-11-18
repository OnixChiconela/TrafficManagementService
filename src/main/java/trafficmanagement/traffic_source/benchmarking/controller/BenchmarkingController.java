package trafficmanagement.traffic_source.benchmarking.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import trafficmanagement.traffic_source.benchmarking.service.BenchmarkingService;

@RestController
@RequestMapping("/api/benchmarking")
public class BenchmarkingController {
    
    private BenchmarkingService benchmarkingService;
    BenchmarkingController(BenchmarkingService benchmarkingService) {
        this.benchmarkingService = benchmarkingService;
    }

    @GetMapping("/compare")
    public Map<String, Map<String, Object>> compareSites() {
        try {
            Map<String, String> sites = new HashMap<>();
            sites.put("my site", "http://localhost:4080/cars/buy");
            sites.put("traffic system", "http://localhost:9020/api/traffic/status");
            sites.put("othersite2", "thissite.com");

            return benchmarkingService.benchmarkSites(sites);
        } catch (Exception error) {
            return null;
        }
    }
}
