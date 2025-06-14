package com.unl.clasesestructura.base.controller.services;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.unl.clasesestructura.base.controller.dao.dao_models.DaoBanda;
import com.unl.clasesestructura.base.models.Banda;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@BrowserCallable
@AnonymousAllowed
public class BandaService {

    private DaoBanda db;

    public BandaService() {
        db = new DaoBanda();
    }

    public void createBanda(@NotEmpty @NotBlank @NonNull String nombre, @NonNull Date fecha) throws Exception {
        if (nombre.trim().length() > 0 && fecha.toString().length() > 0) {
            db.getObj().setNombre(nombre);
            db.getObj().setFecha(fecha);
            if (!db.save()) {
                throw new Exception("Error al guardar la banda");
            }
        }
    }

    public void updateBanda(Integer id, @NotEmpty @NotBlank @NonNull String nombre, @NonNull Date fecha)
            throws Exception {
        if (id != null && id > 0 && nombre.trim().length() > 0 && fecha.toString().length() > 0) {
            db.setObj(db.listAll().get(id - 1));
            db.getObj().setNombre(nombre);
            db.getObj().setFecha(fecha);
            if (!db.update(id - 1)) {
                throw new Exception("Error no se pudo modificar los datos de la Banda");
            }
        }
    }

    public List<Banda> listAllBanda() {
        return Arrays.asList(db.listAll().toArray());

    }

}
