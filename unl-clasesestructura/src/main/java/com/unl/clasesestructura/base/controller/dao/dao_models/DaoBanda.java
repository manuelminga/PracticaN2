package com.unl.clasesestructura.base.controller.dao.dao_models;

import com.unl.clasesestructura.base.controller.dao.AdapterDao;
import com.unl.clasesestructura.base.controller.data_structure.LinkedList;
import com.unl.clasesestructura.base.models.Banda;

public class DaoBanda extends AdapterDao<Banda> {
    private Banda obj;

    private LinkedList<Banda> aux;

    public DaoBanda() {
        super(Banda.class);
        // TODO Auto-generated constructor stub
    }

    public Banda getObj() {
        if (obj == null)
            this.obj = new Banda();
        return this.obj;
    }

    public void setObj(Banda obj) {
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
            // System.out.println(e);
            // Log error
            return false;
        }
    }

}
