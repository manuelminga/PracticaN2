package com.unl.clasesestructura.base.controller.dao.dao_models;

import com.unl.clasesestructura.base.controller.dao.AdapterDao;
import com.unl.clasesestructura.base.models.Persona;

public class DaoPersona extends AdapterDao<Persona> {
    private Persona obj;

    public DaoPersona() {
        super(Persona.class);
        // TODO Auto-generated constructor stub
    }

    public Persona getObj() {
        if (obj == null)
            this.obj = new Persona();
        return this.obj;
    }

    public void setObj(Persona obj) {
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

        DaoPersona da = new DaoPersona();
        da.getObj().setId(da.listAll().getLength() + 1);
        da.getObj().setUsuario("benito");
        da.getObj().setEdad(20);

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
