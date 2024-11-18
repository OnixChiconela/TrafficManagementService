package trafficmanagement.traffic_source.traffic.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import trafficmanagement.traffic_source.traffic.model.TrafficData;

import trafficmanagement.traffic_source.traffic.service.TrafficService;

@RestController
@RequestMapping("/api/traffic")
public class TrafficController {

    private TrafficService trafficService;

    TrafficController(TrafficService trafficService) {
        this.trafficService = trafficService;
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("Traffic management service is working");
    }

    @GetMapping("/limite")
    public ResponseEntity<String> checkRateLimite() {
        return ResponseEntity.ok(trafficService.checkRateLimite());
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^everything--above-its-just-test^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\\
    @PostMapping("/track")
    public void trackPageVisit(@RequestBody TrafficData request, HttpServletRequest httpServletRequest) {
        try {
            trafficService.trackPageVisit(
                request.getPageUrl(), 
                request.getReferer(), 
                request.getAgeGroup(),
                request.getLocation(),
                request.getPurchaseBehavior(),
                httpServletRequest
            );
            System.out.println("ip address: " + httpServletRequest);
        } catch (Exception error) {

        }
    }

    @GetMapping("/unique-visitors/{ipAddress}")
    public long getUniqueVisitors(@PathVariable String ipAddress) {
    try {
    long visitors = this.trafficService.countUniqueVisitors(ipAddress);
    return visitors;
    } catch (Exception error) {
    System.out.println("error finding unique visitors: " + error);
    throw new Error(error);
    }
    }

    @GetMapping("/{id}")
    public TrafficData getTraffic(@PathVariable long id) {
        try {
            TrafficData data = this.trafficService.getTraffic(id);
            return data;
        } catch (Exception error) {
            System.out.println("the error: "+ error);
            return null;
        }
    }

    @GetMapping("/total-visits")
    public long getTotalVisits() {
        try {
            long visits = this.trafficService.countTotalVisits();
            return visits;
        } catch (Exception error) {
            System.out.println("error while getting all visits: " + error);
            throw new Error(error);
        }
    }

    // @GetMapping("/visits")
    // public List<TrafficData> getVisits(@RequestParam String pageUrl,
    //     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
    //     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime
    // ) {
    //     return trafficService.getVisits(pageUrl, startTime, endTime);
    // }

    // @GetMapping("/by-ip")
    // public Map<String, Object> countByIp() {
    //     try {
    //         return this.trafficService.countVisitorsByIp();
    //     } catch (Exception error) {
    //         return null;
    //     }
    // }

    @GetMapping("/traffic-sources")
    public Map<String, Long> getTrafficSources() {
        try {
            return trafficService.getTrafficSources();
        } catch (Exception error) {
            return null;
        }
    }

    @GetMapping("/search-keywords")
    public Map<String, Long> getSearchKeywords() {
        try {
            return trafficService.getSearchKeywords();
        } catch (Exception error) {
            return null;
        }
    }

    // User Behaviour
    @PostMapping("/track-duration/{id}")
    public void trackPageDuration(@PathVariable Long id, @RequestBody Long duration) {
        try {
            trafficService.trackPageDuration(id, duration);
        } catch (Exception error) {
            return;
        }
    }

    @GetMapping("/page-statistics")
    public Map<String, Object> getPageStatistics() {
        try {
            return this.trafficService.getPageStatistics();
        } catch (Exception error) {
            return null;
        }
    }

    @GetMapping("/bounce-rate")
    public double getBounceRate() {
        try {
            double rejection = this.trafficService.calculateBounceRate();
            System.out.println(rejection);
            return rejection;
        } catch (Exception error) {
            return -1;
        }
    }

    //------------segmentention
    @GetMapping("/segments/age-group")
    public Map<String, Long> getVisitorsSegmentsByAgeGroup() {
        try {
            Map<String, Long> age =  this.trafficService.getVisitorSegmentsByAgeGroup();
            System.out.println("the age group " + age);
            return age;
        } catch (Exception error) {
            System.out.println("the error" + error);
            return null;
        }
    }


    @GetMapping("/segments/location")
    public Map<String, Long> getVisitorsSegmentsByLocation() {
        try {
            Map<String, Long> location = this.trafficService.getVisitorSegmentsByLocation();
            System.out.println("the locations: " + location);
            return location;
        } catch (Exception error) {
            return null;
        }
    }

    @GetMapping("/segments/purchase-behavior")
    public Map<String, Long> getVisitorsSegmentsByPurchasMap() {
        try {
            Map<String, Long> purchaseBehavior = this.trafficService.getVisitorSegmentsByPurchaseBehavior();
            System.out.println("purchase behavior: " + purchaseBehavior);
            return purchaseBehavior;
        } catch (Exception error) {
            return null;
        }
    }
}
