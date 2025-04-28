package com.unl.estructura.base.controller.DataStruc.List;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            String filePath = "data.txt";
            
            // 1. Estimar tamaño del archivo
            int estimatedSize = estimateFileSize(filePath);
            System.out.println("Tamaño estimado del archivo: " + estimatedSize + " líneas");
            
            // 2. Crear estructuras de datos
            String[] array = new String[estimatedSize];
            LinkedList<String> linkedList = new LinkedList<>();
            
            // 3. Cargar datos
            System.out.println("\n----- CARGANDO DATOS -----");
            long startTime = System.nanoTime();
            linkedList.loadDataFromFile(filePath, array, s -> s);
            long endTime = System.nanoTime();
            long durationMs = (endTime - startTime) / 1_000_000;
            System.out.println("Tiempo para cargar datos: " + durationMs + " ms");
            System.out.println("Elementos cargados: " + linkedList.getLength());
            
            // 4. Buscar duplicados en array
            System.out.println("\n----- DUPLICADOS EN ARRAY -----");
            startTime = System.nanoTime();
            List<String> duplicatesArray = findDuplicatesInArray(array);
            endTime = System.nanoTime();
            durationMs = (endTime - startTime) / 1_000_000;
            System.out.println("Tiempo para encontrar duplicados en array: " + durationMs + " ms");
            System.out.println("Duplicados encontrados: " + duplicatesArray.size());
            
            // 5. Buscar duplicados en lista enlazada
            System.out.println("\n----- DUPLICADOS EN LISTA ENLAZADA -----");
            startTime = System.nanoTime();
            List<String> duplicatesList = linkedList.findDuplicates();
            endTime = System.nanoTime();
            durationMs = (endTime - startTime) / 1_000_000;
            System.out.println("Tiempo para encontrar duplicados en lista enlazada: " + durationMs + " ms");
            System.out.println("Duplicados encontrados: " + duplicatesList.size());
            
            // 6. Mostrar algunos duplicados (hasta 10)
            System.out.println("\n----- ELEMENTOS DUPLICADOS ENCONTRADOS -----");
            int count = 0;
            for (String item : duplicatesArray) {
                if (count++ < 10) {
                    System.out.println(" - " + item);
                } else {
                    System.out.println(" - ... (y " + (duplicatesArray.size() - 10) + " más)");
                    break;
                }
            }
            
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        } catch (ListEmptyException e) {
            System.out.println("Error con la lista enlazada: " + e.getMessage());
        }
    }
    
    private static int estimateFileSize(String filePath) {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while (reader.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            System.out.println("Error al estimar tamaño: " + e.getMessage());
            return 1000; // Valor por defecto
        }
        return count;
    }
    
    private static <T> List<T> findDuplicatesInArray(T[] array) {
        List<T> duplicates = new ArrayList<>();
        List<T> seen = new ArrayList<>();
        
        for (T item : array) {
            if (seen.contains(item)) {
                if (!duplicates.contains(item)) {
                    duplicates.add(item);
                }
            } else {
                seen.add(item);
            }
        }
        return duplicates;
    }
}