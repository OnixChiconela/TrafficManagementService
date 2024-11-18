package trafficmanagement.traffic_source.traffic.model;

public class TrafficRequest {
    private String pageUrl;
    private String referer;
    
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

    
}