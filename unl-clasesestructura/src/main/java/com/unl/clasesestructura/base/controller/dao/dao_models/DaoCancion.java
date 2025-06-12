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

    private int partition(HashMap<String, String> arr[], int begin, int end, Integer type, String atribute) {
        // hashmap //clave - valor
        // Calendar cd = Calendar.getInstance();

        HashMap<String, String> pivot = arr[end];
        int i = (begin - 1);
        if (type == Utiles.ASCEDENTE) {
            for (int j = begin; j < end; j++) {
                if (arr[j].get(atribute).toString().toLowerCase()
                        .compareTo(pivot.get(atribute).toString().toLowerCase()) < 0) {
                    // if (arr[j] <= pivot) {
                    i++;
                    HashMap<String, String> swapTemp = arr[i];
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
                    HashMap<String, String> swapTemp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = swapTemp;
                }
            }
        }
        HashMap<String, String> swapTemp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = swapTemp;

        return i + 1;
    }

    private void quickSort(HashMap<String, String> arr[], int begin, int end, Integer type, String atribute) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end, type, atribute);

            quickSort(arr, begin, partitionIndex - 1, type, atribute);
            quickSort(arr, partitionIndex + 1, end, type, atribute);
        }
    }

    public LinkedList<HashMap<String, String>> orderQ(Integer type, String atribute) throws Exception {
        LinkedList<HashMap<String, String>> lista = new LinkedList<>();
        if (!listAll().isEmpty()) {

            HashMap<String, String> arr[] = all().toArray();
            quickSort(arr, 0, arr.length - 1, type, atribute);
            lista.toList(arr);
        }
        return lista;
    }

    public LinkedList<HashMap<String, String>> search(String atribute, String text, Integer type) throws Exception {
        LinkedList<HashMap<String, String>> lista = all();
        LinkedList<HashMap<String, String>> resp = new LinkedList<>();

        if (!lista.isEmpty()) {
            // 1. Ordena la lista por el atributo especificado
            lista = orderQ(Utiles.ASCEDENTE, atribute);
            // 2. Transforma la lista a un arreglo de HashMap
            HashMap<String, String>[] arr = lista.toArray();

            // 3. Busca la posición de la mitad del arreglo para optimizar la búsqueda
            Integer n = bynaryLineal(arr, atribute, text);
            System.out.println("La N de la mitad es: " + n);

            switch (type) {
                case 1 -> {
                    // escogemos la derecha
                    if (n > 0) {
                        for (int i = n; i < arr.length; i++) {
                            if (arr[i].get(atribute).toString().toLowerCase().contains(text.toLowerCase())) {
                                resp.add(arr[i]);
                            }
                        }
                    } else if (n < 0) {
                        // escogemos la izquierda desde 0 hasta n
                        n *= -1;
                        for (int i = 0; i < n; i++) {
                            if (arr[i].get(atribute).toString().toLowerCase().contains(text.toLowerCase())) {
                                resp.add(arr[i]);
                            }
                        }
                    } else {
                        // escogemos todo el arreglo
                        for (int i = 0; i < arr.length; i++) {
                            if (arr[i].get(atribute).toString().toLowerCase().contains(text.toLowerCase())) {
                                resp.add(arr[i]);
                            }
                        }
                    }

                    break;
                }
                case 2 -> {
                    // escogemos la derecha
                    if (n > 0) {
                        for (int i = n; i < arr.length; i++) {
                            if (arr[i].get(atribute).toString().toLowerCase().contains(text.toLowerCase())) {
                                resp.add(arr[i]);
                            }
                        }
                    } else if (n < 0) {
                        // escogemos la izquierda desde 0 hasta n
                        n *= -1;
                        for (int i = 0; i < n; i++) {
                            if (arr[i].get(atribute).toString().toLowerCase().contains(text.toLowerCase())) {
                                resp.add(arr[i]);
                            }
                        }
                    } else {
                        // escogemos todo el arreglo
                        for (int i = 0; i < arr.length; i++) {
                            if (arr[i].get(atribute).toString().toLowerCase().contains(text.toLowerCase())) {
                                resp.add(arr[i]);
                            }
                        }
                    }
                    break;
                }
                default -> {
                    for (int i = 0; i < arr.length; i++) {
                        if (arr[i].get(atribute).toString().toLowerCase().contains(text.toLowerCase())) {
                            resp.add(arr[i]);
                        }
                    }
                    break;
                }
            }
        }
        return resp;
    }

    private Integer bynaryLineal(HashMap<String, String>[] array, String atribute, String text) {
        Integer half = 0;
        if (!(array.length == 0) && !text.isEmpty()) {
            half = array.length / 2;
            int aux = 0;

            if (text.trim().toLowerCase().charAt(0) > array[half].get(atribute).toString().trim().toLowerCase()
                    .charAt(0))
                aux = 1;
            else if (text.trim().toLowerCase().charAt(0) < array[half].get(atribute).toString().trim().toLowerCase()
                    .charAt(0))
                aux = -1;

            half = half * aux;
        }
        return half;
    }
}
