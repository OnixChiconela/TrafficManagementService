package trafficmanagement.traffic_source.report.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import trafficmanagement.traffic_source.traffic.repository.TrafficDataRepository;

import trafficmanagement.traffic_source.traffic.model.TrafficData;;;;

@Service
public class ReportService {
    private TrafficDataRepository trafficDataRepository;

    public ReportService(TrafficDataRepository trafficDataRepository) {
        this.trafficDataRepository = trafficDataRepository;
    }

    public byte[] generateReport(String reportFormat, Map<String, Object> parameter) throws JRException {
        try {
            // upload report file .jrxml
            InputStream reportStream = getClass().getResourceAsStream("/reports/traffic_report.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // repository data
            List<TrafficData> trafficData = trafficDataRepository.findAll();
            JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(trafficData);

            // fill report wiht data and parameters
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, datasource);

            // export report in solicitaded format
            byte[] reportBytes;
            if ("pdf".equalsIgnoreCase(reportFormat)) {
                reportBytes = JasperExportManager.exportReportToPdf(jasperPrint);
            } else {
                throw new UnsupportedOperationException("Fromat is not supported: " + reportFormat);
            }

            return reportBytes;
        } catch (Exception error) {
            return null;
        }
    }
}
