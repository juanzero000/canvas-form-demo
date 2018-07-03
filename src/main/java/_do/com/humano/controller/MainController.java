package _do.com.humano.controller;

import _do.com.humano.model.Asegurado;
import _do.com.humano.service.DniTipoService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jjmendoza on 26/6/2018.
 */
@Controller
public class MainController {

    @Autowired
    private DniTipoService dniTipoService;

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("asegurado", new Asegurado());
        populateLists(model);
        return "index";
    }

    @PostMapping("/save")
    public ResponseEntity<Resource> save(@Valid Asegurado asegurado, BindingResult bindingResult, @RequestParam(name = "firma") String firma, Model model) {
        Resource file = generatePDF(asegurado, base64ImageToImage(firma));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + asegurado.getNombre() + ".pdf\"").body(file);
    }

    private void base64ImageToFile(String base64Img) {
        byte[] imageByte = Base64Utils.decodeFromString(base64Img.replace("data:image/png;base64,", ""));
        File file = new File("C:/temp/canvasdemo/firma_" + System.currentTimeMillis() + ".png");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            file.createNewFile();
            fos.write(imageByte);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Image base64ImageToImage(String base64Img) {
        byte[] imageByte = Base64Utils.decodeFromString(base64Img.replace("data:image/png;base64,", ""));
        Image image = null;
        try {
            image = ImageIO.read(new ByteArrayInputStream(imageByte));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    private Resource generatePDF(Asegurado asegurado, Image firma) {

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

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new ClassPathResource("/static/report/CanvasDemoReport.jasper").getFile());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource(1));
            byte[] pdfReport = JasperExportManager.exportReportToPdf(jasperPrint);

            return new ByteArrayResource(pdfReport);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void populateLists(Model model) {
        model.addAttribute("dniTipoList", dniTipoService.findAll());
    }
}
