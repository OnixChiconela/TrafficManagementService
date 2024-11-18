package trafficmanagement.traffic_source.traffic.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TrafficData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pageUrl;
    private String referer;
    private LocalDateTime timestamp;
    private String ipAddress; // Origins and traffic monitoring
    private Long duration;     // User behaviour
    private String ageGroup;
    private String location;
    private String purchaseBehavior;


    public TrafficData(
        String pageUrl, 
        String referer, 
        LocalDateTime timestamp, 
        String ipAddress, 
        Long duration,
        String ageGroup,
        String location, //visits durations in seconds
        String purchaseBehavior
        ) {

        this.pageUrl = pageUrl;
        this.referer = referer;
        this.timestamp = timestamp;
        this.ipAddress = ipAddress;
        this.duration = duration;
        this.ageGroup = ageGroup;
        this.location = location;
        this.purchaseBehavior = purchaseBehavior;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPurchaseBehavior() {
        return purchaseBehavior;
    }

    public void setPurchaseBehavior(String purchaseBehavior) {
        this.purchaseBehavior = purchaseBehavior;
    }

    

}
