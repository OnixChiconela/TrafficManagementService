package trafficmanagement.traffic_source.realtimeAnalytics.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RealTimeAnalyticsService {

    private SimpMessagingTemplate messagingTemplate;
    public RealTimeAnalyticsService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    private Map<String, Long> pageViews = new HashMap<>();
    private Map<String, Long> visitorsLocation = new HashMap<>();

    public void trackPageVisits(String pageUrl, String location) {
        try {
            pageViews.put(pageUrl, pageViews.getOrDefault(pageUrl, 0L) + 1);
            visitorsLocation.put(location, visitorsLocation.getOrDefault(location, 0L) + 1);

            Map<String, Object> data = new HashMap<>();
            data.put("pageViews", pageViews);
            data.put("visitors location", visitorsLocation);

            messagingTemplate.convertAndSend("/topic/analytics");
        } catch (Exception error) {
            return;
        }
    }

    public Map<String, Long> getPageViews() {
        try {
            Map<String, Long> views = pageViews;
            return views;
        } catch (Exception error) {
            return null;
        }
    }

    public Map<String, Long> getVisitorsLocation() {
        try {
            Map<String, Long> locations = visitorsLocation;
            System.out.println(locations);
            return locations;
        } catch (Exception error) {
            return null;
        }
    }

}
