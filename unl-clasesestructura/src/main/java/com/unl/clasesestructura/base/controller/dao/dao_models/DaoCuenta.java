package com.unl.clasesestructura.base.controller.dao.dao_models;

import com.unl.clasesestructura.base.controller.dao.AdapterDao;
import com.unl.clasesestructura.base.models.Cuenta;

public class DaoCuenta extends AdapterDao<Cuenta>{
    private Cuenta obj;

    public DaoCuenta() {
        super(Cuenta.class);
        //TODO Auto-generated constructor stub
    }
    
	public Cuenta getObj() {
        if (obj == null)
            this.obj = new Cuenta();
		return this.obj;
	}

	public void setObj(Cuenta obj) {
		this.obj = obj;
	}

    public Boolean save(){
        try {
            obj.setId(listAll().getLength() + 1);
            this.persist(obj);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            //Log error
            return false;
        }
    }

    public Boolean update(Integer pos){
        try {
            this.update(obj, pos);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            //Log error
            return false;
        }
    }

        

    public static void main(String[] args) {
        
        DaoCuenta da = new DaoCuenta();
        da.getObj().setId(da.listAll().getLength()+1);
        da.getObj().setEmail("@.Ecuatoriana");
        da.getObj().setClave("HorasDeComer");
        da.getObj().setEstado(true);
        da.getObj().setId_persona(1);

        if(da.save()){
            System.out.println("GUARDADO");
        }else{
            System.out.println("ERROR AL GUARDAR");
        }

       /*da.setObj(null);

        da.getObj().setId(da.listAll().getLength()+1);
        da.getObj().setNacionalidad("Ecuatoriana");
        da.getObj().setNombre("Soy otro Minga");

        if(da.save()){
            System.out.println("GUARDADO");
        }else{
            System.out.println("ERROR AL GUARDAR"); */

      //  }
    } 

    
}
