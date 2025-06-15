package com.unl.clasesestructura.base.controller.dao.dao_models;

import java.util.HashMap;

import com.unl.clasesestructura.base.controller.Utiles;
import com.unl.clasesestructura.base.controller.dao.AdapterDao;
import com.unl.clasesestructura.base.controller.data_structure.LinkedList;
import com.unl.clasesestructura.base.models.Cancion;

/**
 * DaoCancion es una clase DAO (Data Access Object) para gestionar objetos de
 * tipo Cancion.
 * Permite operaciones CRUD, ordenamiento y búsqueda sobre canciones
 * almacenadas.
 */
public class DaoCancion extends AdapterDao<Cancion> {
    /**
     * Objeto Cancion que se utiliza como referencia para operaciones de
     * persistencia.
     */
    private Cancion obj;

    /**
     * Constructor de DaoCancion.
     * Inicializa el DAO para la clase Cancion.
     */
    public DaoCancion() {
        super(Cancion.class);
        // Constructor autogenerado
    }

    /**
     * Obtiene el objeto Cancion actual.
     * Si no existe, lo instancia.
     * 
     * @return Cancion actual.
     */
    public Cancion getObj() {
        if (obj == null)
            this.obj = new Cancion();
        return this.obj;
    }

    /**
     * Asigna un objeto Cancion al DAO.
     * 
     * @param obj Objeto Cancion a asignar.
     */
    public void setObj(Cancion obj) {
        this.obj = obj;
    }

    /**
     * Guarda el objeto Cancion actual en la base de datos.
     * Asigna un ID autoincremental.
     * 
     * @return true si se guardó correctamente, false si hubo error.
     */
    public Boolean save() {
        try {
            obj.setId(this.listAll().getLength() + 1); // Asigna ID único
            this.persist(obj); // Persiste el objeto
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Actualiza el objeto Cancion en la posición indicada.
     * 
     * @param pos Posición a actualizar.
     * @return true si se actualizó correctamente, false si hubo error.
     */
    public Boolean update(Integer pos) {
        try {
            this.update(obj, pos);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtiene todas las canciones en forma de lista de HashMap (clave-valor).
     * 
     * @return Lista de canciones como HashMap.
     * @throws Exception Si ocurre un error en la conversión.
     */
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

    /**
     * Convierte un objeto Cancion a un HashMap de atributos clave-valor.
     * 
     * @param arreglo Objeto Cancion a convertir.
     * @return HashMap con los atributos de la canción.
     * @throws Exception Si ocurre un error al obtener datos relacionados.
     */
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

    /**
     * Ordena la lista de canciones por un atributo específico usando el método de
     * selección.
     * 
     * @param type     Tipo de orden (ascendente o descendente).
     * @param atribute Atributo por el cual ordenar.
     * @return Lista ordenada de canciones.
     * @throws Exception Si ocurre un error en la obtención de datos.
     */
    public LinkedList<HashMap<String, String>> orderByCancion(Integer type, String atribute) throws Exception {
        LinkedList<HashMap<String, String>> lista = all();
        if (!lista.isEmpty()) {
            HashMap arr[] = lista.toArray();
            int n = arr.length;

            if (type == Utiles.ASCEDENTE) {
                // Orden ascendente
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
                // Orden descendente
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

    /**
     * Función auxiliar para QuickSort: particiona el arreglo según el atributo y
     * tipo de orden.
     * 
     * @param arr      Arreglo de HashMap a ordenar.
     * @param begin    Índice inicial.
     * @param end      Índice final.
     * @param type     Tipo de orden (ascendente o descendente).
     * @param atribute Atributo por el cual ordenar.
     * @return Índice de partición.
     */
    private int partition(HashMap<String, String> arr[], int begin, int end, Integer type, String atribute) {
        HashMap<String, String> pivot = arr[end];
        int i = (begin - 1);
        if (type == Utiles.ASCEDENTE) {
            for (int j = begin; j < end; j++) {
                if (arr[j].get(atribute).toString().toLowerCase()
                        .compareTo(pivot.get(atribute).toString().toLowerCase()) < 0) {
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

    /**
     * Ordena un arreglo de HashMap usando el algoritmo QuickSort.
     * 
     * @param arr      Arreglo a ordenar.
     * @param begin    Índice inicial.
     * @param end      Índice final.
     * @param type     Tipo de orden.
     * @param atribute Atributo por el cual ordenar.
     */
    private void quickSort(HashMap<String, String> arr[], int begin, int end, Integer type, String atribute) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end, type, atribute);

            quickSort(arr, begin, partitionIndex - 1, type, atribute);
            quickSort(arr, partitionIndex + 1, end, type, atribute);
        }
    }

    /**
     * Ordena la lista de canciones usando QuickSort por un atributo específico.
     * 
     * @param type     Tipo de orden (ascendente o descendente).
     * @param atribute Atributo por el cual ordenar.
     * @return Lista ordenada de canciones.
     * @throws Exception Si ocurre un error en la obtención de datos.
     */
    public LinkedList<HashMap<String, String>> orderQ(Integer type, String atribute) throws Exception {
        LinkedList<HashMap<String, String>> lista = new LinkedList<>();
        if (!listAll().isEmpty()) {
            HashMap<String, String> arr[] = all().toArray();
            quickSort(arr, 0, arr.length - 1, type, atribute);
            lista.toList(arr);
        }
        return lista;
    }

    /**
     * Busca canciones por un atributo y texto, con diferentes tipos de búsqueda.
     * 
     * @param atribute Atributo por el cual buscar.
     * @param text     Texto a buscar.
     * @param type     Tipo de búsqueda (1: derecha, 2: izquierda, otro: todo).
     * @return Lista de canciones que coinciden con la búsqueda.
     * @throws Exception Si ocurre un error en la obtención de datos.
     */
    public LinkedList<HashMap<String, String>> search(String atribute, String text, Integer type) throws Exception {
        LinkedList<HashMap<String, String>> lista = all();
        LinkedList<HashMap<String, String>> resp = new LinkedList<>();

        if (!lista.isEmpty()) {
            // Ordena la lista por el atributo especificado
            lista = orderQ(Utiles.ASCEDENTE, atribute);
            // Transforma la lista a un arreglo de HashMap
            HashMap<String, String>[] arr = lista.toArray();

            // Busca la posición de la mitad del arreglo para optimizar la búsqueda
            Integer n = bynaryLineal(arr, atribute, text);
            System.out.println("La N de la mitad es: " + n);

            switch (type) {
                case 1 -> {
                    // Busca desde la mitad hacia la derecha
                    if (n > 0) {
                        for (int i = n; i < arr.length; i++) {
                            if (arr[i].get(atribute).toString().toLowerCase().contains(text.toLowerCase())) {
                                resp.add(arr[i]);
                            }
                        }
                    } else if (n < 0) {
                        // Busca desde el inicio hasta la mitad
                        n *= -1;
                        for (int i = 0; i < n; i++) {
                            if (arr[i].get(atribute).toString().toLowerCase().contains(text.toLowerCase())) {
                                resp.add(arr[i]);
                            }
                        }
                    } else {
                        // Busca en todo el arreglo
                        for (int i = 0; i < arr.length; i++) {
                            if (arr[i].get(atribute).toString().toLowerCase().contains(text.toLowerCase())) {
                                resp.add(arr[i]);
                            }
                        }
                    }
                    break;
                }
                case 2 -> {
                    // Igual que el caso 1 (posible redundancia)
                    if (n > 0) {
                        for (int i = n; i < arr.length; i++) {
                            if (arr[i].get(atribute).toString().toLowerCase().contains(text.toLowerCase())) {
                                resp.add(arr[i]);
                            }
                        }
                    } else if (n < 0) {
                        n *= -1;
                        for (int i = 0; i < n; i++) {
                            if (arr[i].get(atribute).toString().toLowerCase().contains(text.toLowerCase())) {
                                resp.add(arr[i]);
                            }
                        }
                    } else {
                        for (int i = 0; i < arr.length; i++) {
                            if (arr[i].get(atribute).toString().toLowerCase().contains(text.toLowerCase())) {
                                resp.add(arr[i]);
                            }
                        }
                    }
                    break;
                }
                default -> {
                    // Busca en todo el arreglo
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

    /**
     * Método auxiliar para búsqueda: determina la posición de la mitad del arreglo
     * según el primer carácter del texto buscado y del atributo.
     * 
     * @param array    Arreglo de HashMap.
     * @param atribute Atributo a comparar.
     * @param text     Texto a buscar.
     * @return Entero indicando la posición relativa (positivo: derecha, negativo:
     *         izquierda, 0: igual).
     */
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