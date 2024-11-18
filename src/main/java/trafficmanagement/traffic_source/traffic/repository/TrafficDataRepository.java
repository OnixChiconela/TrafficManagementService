package trafficmanagement.traffic_source.traffic.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import trafficmanagement.traffic_source.traffic.model.TrafficData;

@Repository
public interface TrafficDataRepository extends JpaRepository<TrafficData, Long> {
    long countDistinctByIpAddress(String IpAddress);

    long countByReferer(String referer);

    long countByRefererContaining(String keyword);

    long countByDurationIsNull();

    //
    /*
     * SELECT * FROM `traffic_data`;
     * 
     * SELECT ALL ip_address, page_url, referer, duration COUNT FROM `traffic_data`
     * WHERE ip_address GROUP BY page_url ORDER BY `traffic_data`.`page_url`
     */
    /*
     * SELECT ALL ip_address, page_url, referer, duration, COUNT(ip_address) FROM
     * `traffic_data` WHERE ip_address GROUP BY ip_address ORDER BY
     * `traffic_data`.`page_url`
     */

    //  @Query("SELECT t.pageUrl, COUNT(t.ipAddress) FROM TrafficData t WHERE t.ipAddress GROUP BY t.ipAddress ORDER BY TrafficData t.t.pageUrl")

    //  List<Object[]> countByIpAddress();
    @Query("SELECT t FROM TrafficData t WHERE t.pageUrl = :pageUrl AND t.timeStamp >= :startTime AND t.timeStamp <= :endTime")
    List<TrafficData> findTrafficInTimeRange(@Param("pageUrl") String pageUrl, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    // user behaviour
    @Query("SELECT t.pageUrl, COUNT(t), AVG(t.duration) FROM TrafficData t WHERE t.duration IS NOT NULL GROUP BY t.pageUrl")

    List<Object[]> getPageStatics();

    // methods for personalized segmentation consults
    List<TrafficData> findByAgeGroup(String ageGroup);

    List<TrafficData> findByLocation(String location);

    List<TrafficData> findByPurchaseBehavior(String purchaseBehavior);

    @Query("SELECT  t.ageGroup, COUNT(t) FROM TrafficData t GROUP BY t.ageGroup")
    List<Object[]> countByAgeGroup();

    @Query("SELECT t.location, COUNT(t) FROM TrafficData t GROUP BY t.location")
    List<Object[]> countByLocation();

    @Query("SELECT t.purchaseBehavior, COUNT(t) FROM TrafficData t GROUP BY t.purchaseBehavior")
    List<Object[]> countByPurchaseBehavior();

}
