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

    public LinkedList<HashMap<String, String>> all() throws Exception {
        LinkedList<HashMap<String, String>> lista = new LinkedList<>();

        if (!this.listAll().isEmpty()) {
            Cancion[] arreglo = this.listAll().toArray();
            for (int i = 0; i < arreglo.length; i++) {

                lista.add(toDict(arreglo[i]));
            }
        }
        return lista;
    }

    private HashMap<String, String> toDict(Cancion arreglo) throws Exception {
        DaoGenero dg = new DaoGenero();
        DaoAlbum da = new DaoAlbum();
        HashMap<String, String> aux = new HashMap<>();
        aux.put("id", arreglo.getId().toString());
        aux.put("nombre", arreglo.getNombre());
        aux.put("genero", dg.get(arreglo.getId_genero()).getNombre());
        aux.put("id_genero", dg.listAll().get(arreglo.getId_genero() - 1).getId().toString());
        aux.put("album", da.get(arreglo.getId_album()).getNombre());
        aux.put("id_album", da.listAll().get(arreglo.getId_album() - 1).getId().toString());
        aux.put("duracion", arreglo.getDuracion().toString());
        aux.put("url", arreglo.getUrl());
        aux.put("tipo", arreglo.getTipo().name());
        return aux;
    }

    public LinkedList<HashMap<String, String>> orderByCancion(Integer type, String atribute) throws Exception {
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
                    for (int j = i + 1; j < n; j++)
                        if (arr[j].get(atribute).toString().toLowerCase()
                                .compareTo(arr[min_idx].get(atribute).toString().toLowerCase()) > 0) {
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

    private int partition(HashMap<String, Object> arr[], int begin, int end, Integer type, String atribute) {
        // hashmap //clave - valor
        // Calendar cd = Calendar.getInstance();

        HashMap<String, Object> pivot = arr[end];
        int i = (begin - 1);
        if (type == Utiles.ASCEDENTE) {
            for (int j = begin; j < end; j++) {
                if (arr[j].get(atribute).toString().toLowerCase()
                        .compareTo(pivot.get(atribute).toString().toLowerCase()) < 0) {
                    // if (arr[j] <= pivot) {
                    i++;
                    HashMap<String, Object> swapTemp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = swapTemp;
                }
            }
        } else {
            for (int j = begin; j < end; j++) {
                if (arr[j].get(atribute).toString().toLowerCase()
                        .compareTo(pivot.get(atribute).toString().toLowerCase()) < 0) {
                    // if (arr[j] <= pivot) {
                    i++;
                    HashMap<String, Object> swapTemp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = swapTemp;
                }
            }
        }
        HashMap<String, Object> swapTemp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = swapTemp;

        return i + 1;
    }

    private void quickSort(HashMap<String, Object> arr[], int begin, int end, Integer type, String atribute) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end, type, atribute);

            quickSort(arr, begin, partitionIndex - 1, type, atribute);
            quickSort(arr, partitionIndex + 1, end, type, atribute);
        }
    }

    /*
     * public LinkedList<Cancion> orderQ(Integer type) {
     * LinkedList<Cancion> lista = new LinkedList<>();
     * if (!listAll().isEmpty()) {
     * 
     * Cancion arr[] = listAll().toArray();
     * quickSort(arr, 0, arr.length - 1, type);
     * lista.toList(arr);
     * }
     * return lista;
     * }
     */

    public HashMap<String, Object> toDicti(Cancion c) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        DaoGenero dg = new DaoGenero();
        DaoAlbum da = new DaoAlbum();
        dg.setObj(dg.get(c.getId_genero()));
        da.setObj(da.get(c.getId_album()));
        map.put("album", da.getObj().getNombre());
        map.put("id_genero", c.getId_genero());
        map.put("id", c.getId());
        map.put("nombre", c.getNombre());
        map.put("duracion", c.getDuracion());
        map.put("url", c.getUrl());
        map.put("tipo", c.getTipo());
        return map;
    }

    public LinkedList<HashMap<String, String>> buscar(String atribute, String text, Integer type) throws Exception {
        LinkedList<HashMap<String, String>> lista = all();
        LinkedList<HashMap<String, String>> resp = new LinkedList<>();
        if (!lista.isEmpty()) {
            HashMap<String, String>[] arr = lista.toArray();
            switch (type) {
                case 1:
                    for (HashMap<String, String> m : arr) {
                        if (m.get(atribute).toString().toLowerCase().startsWith(text.toLowerCase())) {
                            resp.add(m);
                        }
                    }

                    break;
                case 2:
                    for (HashMap<String, String> m : arr) {
                        if (m.get(atribute).toString().toLowerCase().endsWith(text.toLowerCase())) {
                            resp.add(m);
                        }
                    }

                default:
                    for (HashMap m : arr) {
                        if (m.get(atribute).toString().toLowerCase().contains(text.toLowerCase())) {
                            resp.add(m);
                        }
                    }
                    break;
            }
        }
        return resp;
    }

    public HashMap<String, Object> BinarySearchRecursive(HashMap<String, Object> arr[], int a, int b, String atribute,
            String value) throws Exception {
        // Base Case to Exit the Recursive Function
        if (b < 1) {
            return null;
        }
        int n = a + (b = 1) / 2;

        // If number is found at mean index of start and end
        if (arr[n].get(atribute).toString().equals(value))
            return arr[n];

        // If number to search for is greater than the arr value at index 'n'
        else if (arr[n].get(atribute).toString().compareTo(value) > 0)
            return BinarySearchRecursive(arr, a, n - 1, atribute, value);

        // If number to search for is greater than the arr value at index 'n'
        else
            return BinarySearchRecursive(arr, n + 1, b, atribute, value);
    }

    private Object getMethod(String atribute, T obj) throws Exception {
        return obj.getClass().getMethod("get" + atribute).invoke(obj);
    }
}
