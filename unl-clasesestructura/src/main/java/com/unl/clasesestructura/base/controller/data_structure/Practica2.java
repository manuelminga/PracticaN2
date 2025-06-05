package com.unl.clasesestructura.base.controller.data_structure;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Practica2 {
    // Declaración de variables de clase
    private Integer[] matriz; // Arreglo para almacenar los datos numéricos
    private LinkedList<Integer> lista; // Lista enlazada para almacenar los mismos datos

    /**
     * Método para cargar datos desde un archivo de texto.
     * Lee cada línea del archivo, convierte a entero y almacena en la lista y el
     * arreglo.
     * 
     * @param filePath Ruta del archivo a leer
     */
    public void cargar(String filePath) {
        LinkedList<Integer> datos = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String linea;
            // Leer el archivo línea por línea
            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                // Ignorar líneas vacías
                if (!linea.isEmpty()) {
                    try {
                        // Convertir línea a entero y agregar a la lista temporal
                        datos.add(Integer.parseInt(linea));
                    } catch (NumberFormatException e) {
                        System.err.println("Valor numérico inválido en línea: " + linea);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer archivo: " + e.getMessage());
        }

        // Convertir la lista temporal a arreglo y asignar a matriz
        this.matriz = datos.toArray();
        // Crear nueva lista enlazada y copiar los valores del arreglo
        this.lista = new LinkedList<>();
        for (Integer val : matriz) {
            lista.add(val);
        }
    }

    /**
     * Busca valores duplicados en el arreglo 'matriz'.
     * 
     * @return Arreglo de Strings con los valores duplicados encontrados.
     */
    public String[] verificar_arreglo() {
        // Verificar si el arreglo está vacío
        if (matriz == null || matriz.length == 0) {
            return new String[0];
        }

        // Crear copia ordenada del arreglo original
        Integer[] copiaOrdenada = Arrays.copyOf(matriz, matriz.length);
        Arrays.sort(copiaOrdenada);

        StringBuilder duplicados = new StringBuilder();
        boolean duplicadoAnterior = false;

        // Buscar duplicados en el arreglo ordenado
        for (int i = 1; i < copiaOrdenada.length; i++) {
            if (copiaOrdenada[i].equals(copiaOrdenada[i - 1])) {
                if (!duplicadoAnterior) {
                    // Solo agregar el duplicado la primera vez que aparece
                    duplicados.append(copiaOrdenada[i]).append("-");
                    duplicadoAnterior = true;
                }
            } else {
                duplicadoAnterior = false;
            }
        }

        // Retornar los duplicados encontrados (si hay)
        return duplicados.length() > 0 ? duplicados.toString().split("-") : new String[0];
    }

    /**
     * Busca valores duplicados en la lista enlazada 'lista'.
     * 
     * @return Lista enlazada con los valores duplicados encontrados.
     */
    public LinkedList<Integer> verificar_lista() {
        LinkedList<Integer> duplicados = new LinkedList<>();

        try {
            // Convertir lista a arreglo para ordenar
            Integer[] arregloLista = lista.toArray();
            Arrays.sort(arregloLista);

            // Buscar duplicados en el arreglo ordenado
            boolean duplicadoAnterior = false;
            for (int i = 1; i < arregloLista.length; i++) {
                if (arregloLista[i].equals(arregloLista[i - 1])) {
                    if (!duplicadoAnterior) {
                        // Solo agregar el duplicado la primera vez que aparece
                        duplicados.add(arregloLista[i]);
                        duplicadoAnterior = true;
                    }
                } else {
                    duplicadoAnterior = false;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al procesar lista: " + e.getMessage());
        }

        return duplicados;
    }

    /**
     * Algoritmo QuickSort para ordenar un arreglo de enteros.
     * 
     * @param arr   Arreglo a ordenar
     * @param begin Índice inicial
     * @param end   Índice final
     */
    private void quickSort(Integer arr[], int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end);

            quickSort(arr, begin, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, end);
        }
    }

    /**
     * Función auxiliar para QuickSort: particiona el arreglo.
     * 
     * @param arr   Arreglo a particionar
     * @param begin Índice inicial
     * @param end   Índice final
     * @return Índice de partición
     */
    private int partition(Integer arr[], int begin, int end) {
        int pivot = arr[end];
        int i = (begin - 1);

        for (int j = begin; j < end; j++) {
            if (arr[j] <= pivot) {
                i++;

                int swapTemp = arr[i];
                arr[i] = arr[j];
                arr[j] = swapTemp;
            }
        }
        int swapTemp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = swapTemp;

        return i + 1;
    }

    /**
     * Ordena la lista usando QuickSort y mide el tiempo de ejecución.
     * 
     * @param filePath Ruta del archivo a cargar y ordenar
     */
    public void quick_order(String filePath) {
        cargar(filePath);
        if (!lista.isEmpty()) {
            Integer arr[] = lista.toArray();
            Integer cont = 0;
            long startTime = System.currentTimeMillis();
            quickSort(arr, 0, arr.length - 1);
            long endTime = System.currentTimeMillis() - startTime;
            System.out.println("Tiempo que se ha tardado QuickSort: " + endTime + " ms");
            lista.toList(arr); // Actualiza la lista con los datos ordenados
        }
    }

    /**
     * Ordena la lista usando ShellSort y mide el tiempo de ejecución.
     * 
     * @param filePath Ruta del archivo a cargar y ordenar
     */
    public void shell_order(String filePath) {
        cargar(filePath);
        if (!lista.isEmpty()) {
            Integer arr[] = lista.toArray();
            Integer cont = 0;
            long startTime = System.currentTimeMillis();
            shell_sort(arr);
            long endTime = System.currentTimeMillis() - startTime;
            System.out.println("Tiempo que se ha tardado ShellSort: " + endTime + " ms");
            lista.toList(arr); // Actualiza la lista con los datos ordenados
        }
    }

    /**
     * Algoritmo ShellSort para ordenar un arreglo de enteros.
     * 
     * @param arrayToSort Arreglo a ordenar
     */
    public void shell_sort(Integer arrayToSort[]) {
        int n = arrayToSort.length;

        // gap es el salto entre elementos a comparar
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                int key = arrayToSort[i];
                int j = i;
                // Mover elementos mayores que key hacia adelante
                while (j >= gap && arrayToSort[j - gap] > key) {
                    arrayToSort[j] = arrayToSort[j - gap];
                    j -= gap;
                }
                arrayToSort[j] = key;
            }
        }
    }

    /**
     * Método principal para ejecutar la práctica.
     * Realiza la carga de datos, muestra estadísticas y ejecuta los algoritmos de
     * ordenamiento.
     * 
     * @param args Argumentos de línea de comandos (no se usan)
     */
    public static void main(String[] args) {
        Practica2 practica = new Practica2();
        String filePath = "data.txt";

        // 1. Cargar datos desde archivo y medir tiempo
        long startLoad = System.nanoTime();
        practica.cargar(filePath);
        long endLoad = System.nanoTime();
        System.out.println("-------------------------------------------");
        System.out.println("Elementos leídos: " + practica.matriz.length);
        System.out.println("Tiempo para cargar datos: " + (endLoad - startLoad) / 1_000_000 + " ms");
        /*
         * // 2. Medir tiempo para llenar lista enlazada
         * long startList = System.nanoTime();
         * LinkedList<Integer> listaCopia = new LinkedList<>();
         * for (Integer val : practica.matriz) {
         * listaCopia.add(val);
         * }
         * long endList = System.nanoTime();
         * System.out.println("Tiempo para llenar lista enlazada: " + (endList -
         * startList) / 1_000_000 + " ms");
         * 
         * // 3. Detectar duplicados en arreglo y medir tiempo
         * long startArray = System.nanoTime();
         * String[] duplicadosArreglo = practica.verificar_arreglo();
         * long endArray = System.nanoTime();
         * System.out.println("Tiempo para detectar duplicados en arreglo: " + (endArray
         * - startArray) / 1_000_000 + " ms");
         * 
         * // 4. Detectar duplicados en lista y medir tiempo
         * long startListDup = System.nanoTime();
         * LinkedList<Integer> duplicadosLista = practica.verificar_lista();
         * long endListDup = System.nanoTime();
         * System.out.println("Tiempo para detectar duplicados en lista enlazada: " +
         * (endListDup - startListDup) / 1_000_000 + " ms");
         * 
         * // Mostrar resultados finales
         * System.out.println("\nDuplicados en arreglo: " + duplicadosArreglo.length);
         * System.out.println("Duplicados en lista: " + duplicadosLista.getLength());
         * 
         * // Mostrar los primeros 10 duplicados encontrados en el arreglo
         * System.out.println("\nPrimeros duplicados encontrados en arreglo:");
         * for (int i = 0; i < Math.min(10, duplicadosArreglo.length); i++) {
         * System.out.println(" - " + duplicadosArreglo[i]);
         * }
         */

        // Crear nueva instancia para pruebas de ordenamiento
        Practica2 p = new Practica2();
        System.out.println("-------------------------------------------");
        // Imprimir título en azul usando código ANSI
        System.out.println("\u001B[34mQuickSort\u001B[0m");
        p.quick_order("data.txt");
        System.out.println("-------------------------------------------");
        // Imprimir título en magenta usando código ANSI
        System.out.println("\u001B[35mShellSort\u001B[0m");
        p.shell_order("data.txt");
        System.out.println("-------------------------------------------");
    }
}