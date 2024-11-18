package trafficmanagement.traffic_source.benchmarking.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


//comparacao de desempenho do (meu site) com os competidores
@Service
public class BenchmarkingService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String PAGESPEED_API_URL = "https://www.googleapis.com/pagespeedonline/v5/runPagespeed";

    public Map<String, Object> getSitePerformece(String url) {
        try {
            String apiUrl = UriComponentsBuilder.fromHttpUrl(PAGESPEED_API_URL)
                    .queryParam("url", url)
                    .queryParam("strategy", "mobile")
                    .toUriString();

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);
            return parsePerformanceData(response);

        } catch (Exception error) {
            return null;
        }
    }

    public Map<String, Object> parsePerformanceData(Map<String, Object> response) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> lighthouseResult = (Map<String, Object>) response.get("lighthouseResult");
            @SuppressWarnings("unchecked")
            Map<String, Object> categories = (Map<String, Object>) lighthouseResult.get("categories");
            @SuppressWarnings("unchecked")
            Map<String, Object> performance = (Map<String, Object>) categories.get("performance");

            Map<String, Object> result = new HashMap<>();
            result.put("performanceScore", performance.get("score"));
            return result;
        } catch (Exception error) {
            return null;
        }
    }

    public Map<String, Map<String, Object>> benchmarkSites(Map<String, String> sites) {
        try {
            Map<String, Map<String, Object>> benchmarkResults = new HashMap<>();

            for (Map.Entry<String, String> entry : sites.entrySet()) {
                String siteName = entry.getKey();
                String siteUrl = entry.getValue();
                Map<String, Object> performanceData = getSitePerformece(siteUrl);
                benchmarkResults.put(siteName, performanceData);
            }

            return benchmarkResults;
        } catch (Exception error) {
            return null;
        }
    }
}
