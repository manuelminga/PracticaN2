package com.unl.clasesestructura.base.controller.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.unl.clasesestructura.base.controller.dao.dao_models.DaoGenero;
import com.unl.clasesestructura.base.models.Genero;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

import jakarta.validation.constraints.NotEmpty;

@BrowserCallable
@Transactional(propagation = Propagation.REQUIRES_NEW)
@AnonymousAllowed

public class GeneroService {

    private DaoGenero db;

    public GeneroService() {
        db = new DaoGenero();
    }

    public void createGenero(@NotEmpty String nombre) throws Exception {
        if (nombre.trim().length() > 0) {
            db.getObj().setNombre(nombre);
            if (!db.save()) {
                throw new Exception("Error al guardar el género");
            }
        }
    }

    public void updateGenero(Integer id, @NotEmpty String nombre)
            throws Exception {
        if (id != null && id > 0 && nombre.trim().length() > 0) {
            db.setObj(db.listAll().get(id - 1));
            db.getObj().setNombre(nombre);
            if (!db.update(id - 1)) {
                throw new Exception("Error no se pudo modificar los datos del Género");
            }
        }
    }

    public List<Genero> listAllGenero() {
        return Arrays.asList(db.listAll().toArray());

    }

}
