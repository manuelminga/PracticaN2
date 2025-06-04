package com.unl.clasesestructura.base.controller.dao.dao_models;

import java.util.Date;

import com.unl.clasesestructura.base.controller.dao.AdapterDao;
import com.unl.clasesestructura.base.models.Album;

public class DaoAlbum extends AdapterDao<Album> {
    private Album obj;

    public DaoAlbum() {
        super(Album.class);
        // TODO Auto-generated constructor stub
    }

    public Album getObj() {
        if (obj == null)
            this.obj = new Album();
        return this.obj;
    }

    public void setObj(Album obj) {
        this.obj = obj;
    }

    public Boolean save() {
        try {
            obj.setId(listAll().getLength() + 1);
            this.persist(obj);
            return true;
        } catch (Exception e) {
            // System.out.println(e);
            // Log error
            return false;
        }
    }

    public Boolean update(Integer pos) {
        try {
            this.update(obj, pos);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            // Log error
            return false;
        }
    }

    public static void main(String[] args) {
        DaoAlbum da = new DaoAlbum();
        Date fechaActual = new Date();

        // Primer álbum (Rock)
        da.getObj().setId(da.listAll().getLength() + 1);
        da.getObj().setNombre("Thriller");
        da.getObj().setFecha(fechaActual);
        da.getObj().setId_banda(1);

        if (da.save()) {
            System.out.println("Álbum 'Thriller' guardado con éxito");
        } else {
            System.out.println("Error al guardar 'Thriller'");
        }

        // Segundo álbum (Pop)
        da.getObj().setId(da.listAll().getLength() + 1);
        da.getObj().setNombre("Back in Black");
        da.getObj().setFecha(fechaActual);
        da.getObj().setId_banda(2);

        if (da.save()) {
            System.out.println("Álbum 'Back in Black' guardado con éxito");
        } else {
            System.out.println("Error al guardar 'Back in Black'");
        }

        // Tercer álbum (Alternativo)
        da.getObj().setId(da.listAll().getLength() + 1);
        da.getObj().setNombre("The Dark Side of the Moon");
        da.getObj().setFecha(fechaActual);
        da.getObj().setId_banda(3);

        if (da.save()) {
            System.out.println("Álbum 'The Dark Side of the Moon' guardado con éxito");
        } else {
            System.out.println("Error al guardar 'The Dark Side of the Moon'");
        }
    }

}
