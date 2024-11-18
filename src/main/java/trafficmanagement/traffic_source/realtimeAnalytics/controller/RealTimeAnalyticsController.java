package trafficmanagement.traffic_source.realtimeAnalytics.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import trafficmanagement.traffic_source.realtimeAnalytics.service.RealTimeAnalyticsService;

@RestController
@RequestMapping("/api/realtime")
public class RealTimeAnalyticsController {
    
    private RealTimeAnalyticsService analyticsService;
    public RealTimeAnalyticsController(RealTimeAnalyticsService realTimeAnalyticsService) {
        this.analyticsService = realTimeAnalyticsService;
    }

    @PostMapping("/track")
    public void trackPageView(@RequestParam String pageUrl, @RequestParam String location) {
        try {
            this.analyticsService.trackPageVisits(pageUrl, location);
        } catch (Exception error) {

        }
    }

    @GetMapping("/page-views")
    public Map<String, Long> getpageView() {
        try {
            Map<String, Long> pageView = this.analyticsService.getPageViews();
            System.out.println("this's the page view endpoint: " + pageView);
            return pageView;
        } catch (Exception error) {
            return null;
        } 
    }

    @GetMapping("/visitors-location")
    public Map<String, Long> getVisitorsLocation() {
        try {
            Map<String, Long> visitorLocation = this.analyticsService.getVisitorsLocation();
            System.out.println("this's the visitor location endpoint: " + visitorLocation);
            return visitorLocation;
        } catch (Exception error) {
            System.out.println("Error occured while getting user location!!!");
            return null;
        }
    }
}
