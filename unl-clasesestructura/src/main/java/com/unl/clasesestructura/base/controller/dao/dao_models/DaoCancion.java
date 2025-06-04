package com.unl.clasesestructura.base.controller.dao.dao_models;

import java.util.HashMap;

import com.unl.clasesestructura.base.controller.Utiles;
import com.unl.clasesestructura.base.controller.dao.AdapterDao;
import com.unl.clasesestructura.base.controller.data_structure.LinkedList;
import com.unl.clasesestructura.base.models.Cancion;
import com.unl.clasesestructura.base.models.Genero;

public class DaoCancion extends AdapterDao<Cancion> {
    private Cancion obj;
    private LinkedList<Cancion> aux;

    public DaoCancion() {
        super(Cancion.class);
        // TODO Auto-generated constructor stub
    }

    // getter and setter
    public Cancion getObj() {
        if (obj == null) {
            this.obj = new Cancion();

        }
        return this.obj;
    }

    public void setObj(Cancion obj) {
        this.obj = obj;
    }

    public Boolean save() {
        try {
            obj.setId(this.listAll().getLength() + 1);
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

    public LinkedList<Cancion> getListAll() {
        if (aux == null) {
            this.aux = listAll();
        }
        return aux;
    }

    /*
     * public LinkedList<Cancion> orderLocate(Integer type){
     * LinkedList<Cancion> lista = new ListedList<>();
     * if(!listAll().isEmpty()){
     * Integer cont = 0;
     * long starTime = System.currentTimeMillis();
     * Cancion arr[] = listAll().toArray();
     * int n = arr.length;
     * }
     * return lista;
     * }
     */

    public LinkedList<Cancion> quickSortNombre(Integer type, int inicio, int fin) {
        LinkedList<Cancion> lista = new LinkedList<>();

        if (inicio >= fin)
            return lista;
        Cancion pivote = lista.get(inicio);
        int elemIzq = inicio + 1;
        int elemDer = fin;
        while (elemIzq <= elemDer) {
            while (elemIzq <= fin && lista.get(elemIzq).getNombre().compareToIgnoreCase(pivote.getNombre()) < 0) {
                elemIzq++;
            }
            while (elemDer > inicio && lista.get(elemDer).getNombre().compareToIgnoreCase(pivote.getNombre()) >= 0) {
                elemDer--;
            }
            if (elemIzq < elemDer) {
                Cancion temp = lista.get(elemIzq);
                lista.update(lista.get(elemDer), elemIzq);
                lista.update(temp, elemDer);
            }
        }

        if (elemDer > inicio) {
            Cancion temp = lista.get(inicio);
            lista.update(lista.get(elemDer), inicio);
            lista.update(temp, elemDer);
        }
        /*
         * quickSortNombre(lista, inicio, elemDer - 1);
         * quickSortNombre(lista, elemDer + 1, fin);
         */
        return lista;
    }

    public void quickSortGeneroPorCancion(LinkedList<Cancion> lista, int inicio, int fin) {
        if (inicio >= fin)
            return;
        Cancion pivote = lista.get(inicio);
        int elemIzq = inicio + 1;
        int elemDer = fin;
        while (elemIzq <= elemDer) {
            while (elemIzq <= fin &&
                    getNombreGenero(lista.get(elemIzq)).compareToIgnoreCase(getNombreGenero(pivote)) < 0) {
                elemIzq++;
            }
            while (elemDer > inicio &&
                    getNombreGenero(lista.get(elemDer)).compareToIgnoreCase(getNombreGenero(pivote)) >= 0) {
                elemDer--;
            }
            if (elemIzq < elemDer) {
                Cancion temp = lista.get(elemIzq);
                lista.update(lista.get(elemDer), elemIzq);
                lista.update(temp, elemDer);
            }
        }

        if (elemDer > inicio) {
            Cancion temp = lista.get(inicio);
            lista.update(lista.get(elemDer), inicio);
            lista.update(temp, elemDer);
        }
        quickSortGeneroPorCancion(lista, inicio, elemDer - 1);
        quickSortGeneroPorCancion(lista, elemDer + 1, fin);
    }

    // Método auxiliar para obtener el nombre del género de una canción
    private String getNombreGenero(Cancion cancion) {
        // Busca el género por id y retorna su nombre
        Genero genero = new DaoGenero().listAll().get(cancion.getId_genero() - 1);
        return genero.getNombre();
    }

    public LinkedList<HashMap<String, String>> all() {
        LinkedList<HashMap<String, String>> lista = new LinkedList<>();

        if (!this.listAll().isEmpty()) {
            Cancion[] arreglo = this.listAll().toArray();
            for (int i = 0; i < arreglo.length; i++) {

                lista.add(toDict(arreglo[i]));
            }
        }
        return lista;
    }

    private HashMap<String, String> toDict(Cancion arreglo) {
        DaoGenero dg = new DaoGenero();
        DaoAlbum da = new DaoAlbum();
        HashMap<String, String> aux = new HashMap<>();
        aux.put("id", arreglo.getId().toString());
        aux.put("nombre", arreglo.getNombre());
        aux.put("genero", dg.listAll().get(arreglo.getId_genero() - 1).getNombre());
        aux.put("id_genero", dg.listAll().get(arreglo.getId_genero() - 1).getId().toString());
        aux.put("album", da.listAll().get(arreglo.getId_album() - 1).getNombre());
        aux.put("id_album", da.listAll().get(arreglo.getId_album() - 1).getId().toString());
        aux.put("duracion", arreglo.getDuracion().toString());
        aux.put("url", arreglo.getUrl());
        aux.put("tipo", arreglo.getTipo().name());
        return aux;
    }

    public LinkedList<HashMap<String, String>> orderByCancion(Integer type, String atribute) {
        LinkedList<HashMap<String, String>> lista = all();
        if (!lista.isEmpty()) {
            HashMap arr[] = lista.toArray();
            int n = arr.length;

            if (type == Utiles.ASCEDENTE) {
                for (int i = 0; i < n - 1; i++) {
                    int min_idx = i;
                    for (int j = i + 1; j < n; j++)
                        if (arr[j].get(atribute).toString().toLowerCase()
                                .compareTo(arr[min_idx].get(atribute).toString().toLowerCase()) < 0) {
                            min_idx = j;
                        }
                    HashMap temp = arr[min_idx];
                    arr[min_idx] = arr[i];
                    arr[i] = temp;
                }
            } else {
                for (int i = 0; i < n - 1; i++) {
                    int min_idx = i;
                    for (int j = i + 1; j < n; j++) {
                        if (arr[j].get(atribute).toString().toLowerCase()
                                .compareTo(arr[min_idx].get(atribute).toString().toLowerCase()) > 0)
                            min_idx = j;
                    }
                    HashMap temp = arr[min_idx];
                    arr[min_idx] = arr[i];
                    arr[i] = temp;
                }
            }
        }
        return lista;

    }
}
