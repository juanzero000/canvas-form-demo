package _do.com.humano.controller;

import _do.com.humano.model.Asegurado;
import _do.com.humano.service.DniTipoService;
import _do.com.humano.service.EncryptionService;
import _do.com.humano.service.ImageService;
import _do.com.humano.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * Created by jjmendoza on 26/6/2018.
 */
@Controller
public class MainController {

    @Autowired
    private DniTipoService dniTipoService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private EncryptionService encryptionService;

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("asegurado", new Asegurado());
        populateLists(model);
        return "index";
    }

    @PostMapping("/save")
    public ResponseEntity<Resource> save(@Valid Asegurado asegurado, @RequestParam(name = "firma") String firma) {
        String encryptedData = encryptionService.encrypt(asegurado.getNombre() + " " + asegurado.getApellido(), asegurado.getDocumento());

        Resource file = new ByteArrayResource(reportService.generatePDF(asegurado,
                imageService.base64ImageToImage(firma),
                imageService.getQRCodeImage(encryptedData, 100, 100)));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + asegurado.getNombre() + ".pdf\"").body(file);
    }

    private void populateLists(Model model) {
        model.addAttribute("dniTipoList", dniTipoService.findAll());
    }
}
