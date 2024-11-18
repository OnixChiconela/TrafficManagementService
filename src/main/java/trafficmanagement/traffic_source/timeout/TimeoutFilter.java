package trafficmanagement.traffic_source.timeout;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse; 

public class TimeoutFilter implements Filter {
    private static final long TIMEOUT_SECONDS = 20;

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
    throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (!"keep-alive".equalsIgnoreCase(httpRequest.getHeader("Connection"))) {
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

            Runnable timeoutTask = () -> {
                try {
                    httpResponse.sendError(HttpServletResponse.SC_GATEWAY_TIMEOUT);
                } catch (IOException error) {
                    error.printStackTrace();
                }
            };

            scheduler.schedule(timeoutTask, TIMEOUT_SECONDS, TimeUnit.SECONDS);
            System.out.println("timeout filter was touched: " + TIMEOUT_SECONDS);

            try {
                chain.doFilter(request, response);
                System.out.println("from timeout filter: " + chain + " now the response " + response);
            } finally {
                scheduler.shutdown();
                System.out.println("the shutdown ");
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {}
    
}