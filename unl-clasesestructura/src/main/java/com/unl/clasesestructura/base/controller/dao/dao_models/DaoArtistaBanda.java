package com.unl.clasesestructura.base.controller.dao.dao_models;

import com.unl.clasesestructura.base.controller.dao.AdapterDao;
import com.unl.clasesestructura.base.models.Artista;
import com.unl.clasesestructura.base.models.ArtistaBanda;

public class DaoArtistaBanda extends AdapterDao<ArtistaBanda> {
    private ArtistaBanda obj;

    public DaoArtistaBanda() {
        super(ArtistaBanda.class);
        // TODO Auto-generated constructor stub
    }

    public ArtistaBanda getObj() {
        if (obj == null)
            this.obj = new ArtistaBanda();
        return this.obj;
    }

    public void setObj(ArtistaBanda obj) {
        this.obj = obj;
    }

    public Boolean save() {
        try {
            obj.setId(listAll().getLength() + 1);
            this.persist(obj);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            // Log error
            return false;
        }
    }

    public Boolean update(Artista artistaActualizado) {
        try {
            // Buscar el artista por ID y actualizarlo
            for (int i = 0; i < listAll().getLength(); i++) {
                Artista artista = listAll().get(i);
                if (artista.getId().equals(artistaActualizado.getId())) {
                    artista.setNombre(artistaActualizado.getNombre());
                    artista.setNacionalidad(artistaActualizado.getNacionalidad());
                    // Guardar los cambios
                    this.update(artista, i);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {

        DaoArtistaBanda da = new DaoArtistaBanda();
        da.getObj().setId(da.listAll().getLength() + 1);
        // da.getObj().setRol("Bombo");
        da.getObj().setId_artista(1);
        da.getObj().setId_banda(1);

        if (da.save()) {
            System.out.println("GUARDADO");
        } else {
            System.out.println("ERROR AL GUARDAR");
        }

        /*
         * da.setObj(null);
         * 
         * da.getObj().setId(da.listAll().getLength()+1);
         * da.getObj().setNacionalidad("Ecuatoriana");
         * da.getObj().setNombre("Soy otro Minga");
         * 
         * if(da.save()){
         * System.out.println("GUARDADO");
         * }else{
         * System.out.println("ERROR AL GUARDAR");
         * 
         * }
         */
    }

}
