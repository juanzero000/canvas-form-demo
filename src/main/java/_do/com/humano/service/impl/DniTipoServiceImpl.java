package _do.com.humano.service.impl;

import _do.com.humano.model.DniTipo;
import _do.com.humano.service.DniTipoService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jjmendoza on 27/6/2018.
 */
@Service
public class DniTipoServiceImpl implements DniTipoService {
    @Override
    public List<DniTipo> findAll() {
        return Arrays.asList(new DniTipo("Cédula"), new DniTipo("Pasaporte"));
    }
}
