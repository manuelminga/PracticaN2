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
     * Método para cargar datos desde un archivo de texto
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
     * Método para encontrar valores duplicados en el arreglo
     * 
     * @return Arreglo de Strings con los valores duplicados encontrados
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
     * Método para encontrar valores duplicados en la lista enlazada
     * 
     * @return Lista enlazada con los valores duplicados encontrados
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
     * Método principal para ejecutar la práctica
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
        System.out.println("Elementos leídos: " + practica.matriz.length);
        System.out.println("Tiempo para cargar datos: " + (endLoad - startLoad) / 1_000_000 + " ms");

        // 2. Medir tiempo para llenar lista enlazada
        long startList = System.nanoTime();
        LinkedList<Integer> listaCopia = new LinkedList<>();
        for (Integer val : practica.matriz) {
            listaCopia.add(val);
        }
        long endList = System.nanoTime();
        System.out.println("Tiempo para llenar lista enlazada: " + (endList - startList) / 1_000_000 + " ms");

        // 3. Detectar duplicados en arreglo y medir tiempo
        long startArray = System.nanoTime();
        String[] duplicadosArreglo = practica.verificar_arreglo();
        long endArray = System.nanoTime();
        System.out
                .println("Tiempo para detectar duplicados en arreglo: " + (endArray - startArray) / 1_000_000 + " ms");

        // 4. Detectar duplicados en lista y medir tiempo
        long startListDup = System.nanoTime();
        LinkedList<Integer> duplicadosLista = practica.verificar_lista();
        long endListDup = System.nanoTime();
        System.out.println("Tiempo para detectar duplicados en lista enlazada: "
                + (endListDup - startListDup) / 1_000_000 + " ms");

        // Mostrar resultados finales
        System.out.println("\nDuplicados en arreglo: " + duplicadosArreglo.length);
        System.out.println("Duplicados en lista: " + duplicadosLista.getLength());

        // Mostrar los primeros 10 duplicados encontrados en el arreglo
        System.out.println("\nPrimeros duplicados encontrados en arreglo:");
        for (int i = 0; i < Math.min(10, duplicadosArreglo.length); i++) {
            System.out.println(" - " + duplicadosArreglo[i]);
        }
    }
}