package trafficmanagement.traffic_source.traffic.service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import trafficmanagement.traffic_source.Goal.service.GoalService;
import trafficmanagement.traffic_source.traffic.model.TrafficData;
import trafficmanagement.traffic_source.traffic.repository.TrafficDataRepository;;

@Service
public class TrafficService {

    private final Bucket bucket;
    private TrafficDataRepository trafficDataRepository;
    private GoalService goalService;

    public TrafficService(GoalService goalService, TrafficDataRepository trafficDataRepository) {
        Refill refill = Refill.intervally(10, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(100, refill);
        this.trafficDataRepository = trafficDataRepository;
        this.goalService = goalService;
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

    public void trackPageVisit(String pageUrl, String referer, String ageGroup, String location, 
            String purchaseBehavior, HttpServletRequest request) {
        if (bucket.tryConsume(1)) {
            String ipAddress = request.getRemoteAddr();
            TrafficData trafficData = new TrafficData(pageUrl, referer, LocalDateTime.now(), ipAddress, null,
                    ageGroup, location, purchaseBehavior);

            trafficDataRepository.save(trafficData);

            // chart target for visualization of a page example
            // exemplo de rastreamento de valor para visualizacao de pagina
            this.goalService.trackGoalProgress(1, 1);
        } else {
            // Action for exceded limite
            System.out.println("requesition limite was exceeded");
        }
    }

    // public List<TrafficData> getVisits(String pageUrl, LocalDateTime startTime, LocalDateTime endTime) {
    //     try {
    //         return trafficDataRepository.findTrafficInTimeRange(pageUrl, startTime, endTime);
    //     } catch(Exception error) {
    //         return null;
    //     }
    // }

    public long countUniqueVisitors(String ipAddress) {
        try {
            long visitor = trafficDataRepository.countDistinctByIpAddress(ipAddress);
            if(visitor == 0) {
                System.out.println("this page as not receive any visitor yet!");
            }
            return visitor;
        } catch (Exception error) {
            System.out.println("Unique visitor was not founded: " + error);
            return 0;
        }
    }

    public long countTotalVisits() {
        try {
            long visits = trafficDataRepository.count();
            if (visits == 0) {
                throw new Exception("Seems like does not have any visit");
            }
            return visits;
        } catch (Exception error) {
            System.out.println("Error finding vists! " + error);
            return 0;
        }
    }

    //fazer a estatistica das paginas de acordo com as datas, por ex: em um mes quantas visitas a pagina teve

    public TrafficData getTraffic(long id) {
        try {
            TrafficData data = this.trafficDataRepository.findById(id).orElse(null);
            return data;
        } catch (Exception error) {
            return null;
        }
    }

    // public Map<String, Object> countVisitorsByIp() {
    //     try {
    //         Map<String, Object> visits = new HashMap<>();
    //         List<Object[]> visitors = this.trafficDataRepository.countAllByIpAddress();

    //         for (Object[] data : visitors) {
    //             String visitData = (String) data[0];
    //             long userAddress = (Long) data[1];

    //             Map<String, Object> ipStats = new HashMap<>();
    //             ipStats.put("ip address", userAddress);

    //             visits.put(visitData, ipStats);
    //         }
    //         return visits;
    //     } catch (Exception error) {
    //         return null;
    //     }
    // }

    public Map<String, Long> getTrafficSources() {
        try {
            Map<String, Long> trafficSources = new HashMap<>();
            // categorization
            // search engine
            long google = trafficDataRepository.countByRefererContaining("google");
            long bing = trafficDataRepository.countByRefererContaining("bing");
            long yahoo = trafficDataRepository.countByRefererContaining("yahoo");

            long searchEngines = google + bing + yahoo;

            // social media
            long facebook = trafficDataRepository.countByRefererContaining("facebook");
            long instagram = trafficDataRepository.countByRefererContaining("instagram");
            long x = trafficDataRepository.countByRefererContaining("x");
            long linkedin = trafficDataRepository.countByRefererContaining("linkedin");
            long threads = trafficDataRepository.countByRefererContaining("threads");

            long socialMedia = facebook + instagram + x + linkedin + threads;

            // direct link
            // long directLink = trafficDataRepository.countByRefererContaining("mysite");

            // marketing campains
            long googleAds = trafficDataRepository.countByRefererContaining("googleads");
            long marketingCampains = googleAds;

            long direct = trafficDataRepository.countByRefererContaining("site");

            trafficSources.put("Search Engine", searchEngines);
            trafficSources.put("Search Engine: google", google);
            trafficSources.put("Search Engine: bing", bing);
            trafficSources.put("Search Engine: yahoo", yahoo);
            trafficSources.put("Social Media", socialMedia);
            trafficSources.put("Social Media: facebook", facebook);
            trafficSources.put("Social Media: instagram", instagram);
            trafficSources.put("Social Media: Threads", threads);
            trafficSources.put("Marketing Campains", marketingCampains);

            trafficSources.put("Direct", direct);

            return trafficSources;
        } catch (Exception error) {
            return null;
        }
    }

    // ---------------------search-keywords---------------------------
    public Map<String, Long> getSearchKeywords() {
        try {
            Map<String, Long> keywords = new HashMap<>();
            Pattern pattern = Pattern.compile("[?&](q|query|p|search|serchterm)=([^&]+)");

            for (TrafficData data : trafficDataRepository.findAll()) {
                Matcher matcher = pattern.matcher(data.getReferer());
                if (matcher.find()) {
                    String keyword = URLDecoder.decode(matcher.group(2), StandardCharsets.UTF_8);
                    keywords.put(keyword, keywords.getOrDefault(keywords, 0L) + 1);
                }
            }
            return keywords;
        } catch (Exception error) {
            return null;
        }
    }

    // USER BEHAVIOUR
    public void trackPageDuration(Long id, Long duration) {
        try {
            TrafficData trafficData = trafficDataRepository.findById(id).orElse(null);
            if (trafficData != null) {
                trafficData.setDuration(duration);
                trafficDataRepository.save(trafficData);

                // Exemplo de rastreamento de meta para tempo de permanencia
                this.goalService.trackGoalProgress(2, duration);
            }
            return;
        } catch (Exception error) {

        }
    }

    public Map<String, Object> getPageStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();
            List<Object[]> pageData = trafficDataRepository.getPageStatics();

            for (Object[] data : pageData) {
                String pageUrl = (String) data[0];
                long visitCount = (Long) data[1];
                Double avgDuration = (Double) data[2];

                Map<String, Object> pageStats = new HashMap<>();
                pageStats.put("visit count", visitCount);
                pageStats.put("avarage duration(s)", avgDuration);

                stats.put(pageUrl, pageStats);
            }
            return stats;
        } catch (Exception error) {
            return null;
        }
    }

    // --bounce rate--\\
    /*
     * using one visualization(using pages that have not duration time(null)) of
     * page before user get out of the site and calculate the
     * rejection tax using amount of pages with null duration
     */
    public double calculateBounceRate() {
        try {
            long totalVisits = trafficDataRepository.count();
            long singlePageVisits = trafficDataRepository.countByDurationIsNull();// countDurationIsNull()

            return (double) singlePageVisits / totalVisits * 100;
        } catch (Exception error) {
            return 0;
        }
    }

    // targets: - follow progress direction to a specifics objectives, like form
    // fill, boughts, or permanence in site

    // --------------SEGMENTATION---------------\\
    public Map<String, Long> getVisitorSegmentsByAgeGroup() {
        try {
            Map<String, Long> ageGroupSegments = new HashMap<>();
            List<Object[]> ageGroupData = trafficDataRepository.countByAgeGroup();

            for (Object[] data : ageGroupData) {
                String ageGroup = (String) data[0];
                Long count = (Long) data[1];
                ageGroupSegments.put(ageGroup, count);
            }
            return ageGroupSegments;
        } catch (Exception error) {
            System.out.println("seems like an error occured: " + error);
            return null;
        }
    }

    public Map<String, Long> getVisitorSegmentsByLocation() {
        try {
            Map<String, Long> locationSegments = new HashMap<>();
            List<Object[]> locationData = trafficDataRepository.countByLocation();

            for (Object[] data : locationData) {
                String location = (String) data[0];
                Long count = (Long) data[1];
                locationSegments.put(location, count);
            }

            return locationSegments;
        } catch (Exception error) {
            System.out.println("Error segmenting by location: " + error);
            return null;
        }
    }

    public Map<String, Long> getVisitorSegmentsByPurchaseBehavior() {
        try {
            Map<String, Long> behaviorSegments = new HashMap<>();
            List<Object[]> behaviorData = trafficDataRepository.countByPurchaseBehavior();

            for (Object[] data : behaviorData) {
                String behavior = (String) data[0];
                Long count = (Long) data[1];
                behaviorSegments.put(behavior, count);
            }

            return behaviorSegments;

        } catch (Exception error) {
            return null;
        }
    }

}
