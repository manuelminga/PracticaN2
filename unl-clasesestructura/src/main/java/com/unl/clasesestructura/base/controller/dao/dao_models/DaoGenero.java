package com.unl.clasesestructura.base.controller.dao.dao_models;

import com.unl.clasesestructura.base.controller.dao.AdapterDao;
import com.unl.clasesestructura.base.controller.data_structure.LinkedList;
import com.unl.clasesestructura.base.models.Genero;

public class DaoGenero extends AdapterDao<Genero> {
    private Genero obj;

    private LinkedList<Genero> aux;

    public DaoGenero() {
        super(Genero.class);
        // TODO Auto-generated constructor stub
    }

    // getter and setter
    public Genero getObj() {
        if (obj == null) {
            this.obj = new Genero();

        }
        return this.obj;
    }

    public void setObj(Genero obj) {
        this.obj = obj;
    }

    public Boolean save() {
        try {
            obj.setId(listAll().getLength() + 1);
            this.persist(obj);
            return true;
        } catch (Exception e) {

            return false;
            // TODO: handle exception
        }
    }

    public Boolean update(Integer pos) {
        try {
            this.update(obj, pos);
            return true;
        } catch (Exception e) {

            return false;
            // TODO: handle exception
        }
    }

    public static void main(String[] args) {
        DaoGenero da = new DaoGenero();

        // Primer género (Rock)
        da.getObj().setId(da.listAll().getLength() + 1);
        da.getObj().setNombre("Rock");

        if (da.save()) {
            System.out.println("Rock - Guardado");
        } else {
            System.out.println("Error al guardar Rock");
        }

        // Segundo género (Pop)
        da.getObj().setId(da.listAll().getLength() + 1);
        da.getObj().setNombre("Pop");

        if (da.save()) {
            System.out.println("Pop - Guardado");
        } else {
            System.out.println("Error al guardar Pop");
        }

        // Tercer género (Jazz)
        da.getObj().setId(da.listAll().getLength() + 1);
        da.getObj().setNombre("Jazz");

        if (da.save()) {
            System.out.println("Jazz - Guardado");
        } else {
            System.out.println("Error al guardar Jazz");
        }
    }
}
