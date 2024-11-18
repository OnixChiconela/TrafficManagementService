package trafficmanagement.traffic_source.report.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.sf.jasperreports.engine.JRException;
import trafficmanagement.traffic_source.report.service.ReportService;

@RestController
@RequestMapping("api/reports")
public class ReportController {
    private ReportService reportService;
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    //relatorios e suas personalizacoes
    @GetMapping("/traffic")
    public ResponseEntity<byte[]> generateTrafficReport(
        @RequestParam String format,
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate,
        @RequestParam(required = false) String page,
        @RequestParam(required = false) String location
    ) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            if (startDate != null) parameters.put("START_DATE", startDate);
            if (endDate != null) parameters.put("END_DATE", endDate);
            if (startDate != null) parameters.put("PAGE", startDate);
            if (startDate != null) parameters.put("LOCATION", startDate);
            //add param if needed
            byte[] reportByte = reportService.generateReport(format, parameters);

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=traffic_report." + format);

            return ResponseEntity.ok()
                .headers(headers)
                .contentType("pdf".equalsIgnoreCase(format) ? MediaType.APPLICATION_PDF : MediaType.APPLICATION_OCTET_STREAM)
                .body(reportByte);
        } catch (JRException error) {
            return ResponseEntity.status(500).body(null);
        }
    }    
}
