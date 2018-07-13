package _do.com.humano.service.impl;

import _do.com.humano.model.Asegurado;
import _do.com.humano.service.ReportService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jjmendoza on 12/7/2018.
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Override
    public byte[] generatePDF(Asegurado asegurado, Image firma, Image qrCode) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("pNombre", asegurado.getNombre());
            params.put("pApellido", asegurado.getApellido());
            params.put("pTipoDocumento", asegurado.getTipoDocumento());
            params.put("pDocumento", asegurado.getDocumento());
            params.put("pNumeroAfiliado", asegurado.getNumeroAfiliado());
            params.put("pTelefono", asegurado.getTelefono());
            params.put("pEmail", asegurado.getEmail());
            params.put("pFirma", firma);
            params.put("pCodigoQR", qrCode);

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new ClassPathResource("/static/report/CanvasDemoReport.jasper").getInputStream());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource(1));
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
