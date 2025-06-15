package com.unl.clasesestructura.base.controller.dao.dao_models;

import java.util.HashMap;

import com.unl.clasesestructura.base.controller.Utiles;
import com.unl.clasesestructura.base.controller.dao.AdapterDao;
import com.unl.clasesestructura.base.controller.data_structure.LinkedList;
import com.unl.clasesestructura.base.models.Album;

public class DaoAlbum extends AdapterDao<Album> {
    private Album obj;
    private LinkedList<Album> aux;

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

    public LinkedList<HashMap<String, String>> all() throws Exception {
        LinkedList<HashMap<String, String>> lista = new LinkedList<>();

        if (!this.listAll().isEmpty()) {
            Album[] arreglo = this.listAll().toArray();
            for (int i = 0; i < arreglo.length; i++) {

                lista.add(toDict(arreglo[i]));
            }
        }
        return lista;
    }

    private HashMap<String, String> toDict(Album arreglo) throws Exception {
        DaoBanda db = new DaoBanda();
        HashMap<String, String> aux = new HashMap<>();
        aux.put("id", arreglo.getId().toString());
        aux.put("nombre", arreglo.getNombre());
        aux.put("banda", db.get(arreglo.getId_banda()).getNombre());
        aux.put("id_banda", db.listAll().get(arreglo.getId_banda() - 1).getId().toString());
        aux.put("fecha", arreglo.getFecha() != null ? arreglo.getFecha().toString() : "");
        return aux;
    }

    public LinkedList<HashMap<String, String>> orderByAlbum(Integer type, String atribute) throws Exception {
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
                case 1:
                    // escogemos la derecha
                    if (n > 0) {
                        for (int i = n; i < arr.length; i++) {
                            if (arr[i].get(atribute).toString().toLowerCase().startsWith(text.toLowerCase())) {
                                resp.add(arr[i]);
                            }
                        }
                    } else if (n < 0) {
                        // escogemos la izquierda desde 0 hasta n
                        n *= -1;
                        for (int i = 0; i < n; i++) {
                            if (arr[i].get(atribute).toString().toLowerCase().startsWith(text.toLowerCase())) {
                                resp.add(arr[i]);
                            }
                        }
                    } else {
                        // escogemos todo el arreglo
                        for (int i = 0; i < arr.length; i++) {
                            if (arr[i].get(atribute).toString().toLowerCase().startsWith(text.toLowerCase())) {
                                resp.add(arr[i]);
                            }
                        }
                    }

                    break;
                case 2:
                    // escogemos la derecha
                    if (n > 0) {
                        for (int i = n; i < arr.length; i++) {
                            if (arr[i].get(atribute).toString().toLowerCase().startsWith(text.toLowerCase())) {
                                resp.add(arr[i]);
                            }
                        }
                    } else if (n < 0) {
                        // escogemos la izquierda desde 0 hasta n
                        n *= -1;
                        for (int i = 0; i < n; i++) {
                            if (arr[i].get(atribute).toString().toLowerCase().startsWith(text.toLowerCase())) {
                                resp.add(arr[i]);
                            }
                        }
                    } else {
                        // escogemos todo el arreglo
                        for (int i = 0; i < arr.length; i++) {
                            if (arr[i].get(atribute).toString().toLowerCase().startsWith(text.toLowerCase())) {
                                resp.add(arr[i]);
                            }
                        }
                    }

                    break;
                default:
                    for (int i = 0; i < arr.length; i++) {
                        if (arr[i].get(atribute).toString().toLowerCase().startsWith(text.toLowerCase())) {
                            resp.add(arr[i]);
                        }
                    }
                    break;
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

    public static void main(String[] args) {
        DaoAlbum da = new DaoAlbum();

        // Crear un nuevo álbum
        da.getObj().setId(da.listAll().getLength() + 1);
        da.getObj().setNombre("Nuevo Álbum");
        da.getObj().setFecha(new java.util.Date()); // Fecha actual
        da.getObj().setId_banda(1); // Asigna el id de una banda existente

        if (da.save()) {
            System.out.println("GUARDADO");
        } else {
            System.out.println("ERROR AL GUARDAR");
        }
    }
}
