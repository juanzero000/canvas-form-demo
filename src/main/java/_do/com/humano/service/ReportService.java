package _do.com.humano.service;

import _do.com.humano.model.Asegurado;

import java.awt.*;

/**
 * Created by jjmendoza on 12/7/2018.
 */
public interface ReportService {

    byte[] generatePDF(Asegurado asegurado, Image firma, Image qrCode);
}
