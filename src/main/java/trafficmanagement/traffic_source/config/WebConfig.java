package trafficmanagement.traffic_source.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// import trafficmanagement.traffic_source.RateLimit.RateLimitFilter;
import trafficmanagement.traffic_source.timeout.TimeoutFilter;

@Configuration
public class WebConfig {
    
    @Bean
    public FilterRegistrationBean<TimeoutFilter> loggingFilter() {
        FilterRegistrationBean<TimeoutFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TimeoutFilter());
        System.out.println("timeout config| web config");
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    // @Bean
    // public FilterRegistrationBean<RateLimitFilter> rateLimit() {
    //     FilterRegistrationBean<RateLimitFilter> registrationBean = new FilterRegistrationBean<>();
    //     registrationBean.setFilter(new RateLimitFilter());
    //     registrationBean.addUrlPatterns("/*");
    //     return registrationBean;
    // }
}
