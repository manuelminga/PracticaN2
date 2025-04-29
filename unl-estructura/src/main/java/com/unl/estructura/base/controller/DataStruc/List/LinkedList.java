package com.unl.estructura.base.controller.DataStruc.List;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de una lista enlazada simple con capacidades de análisis de rendimiento
 * @param <E> Tipo de elementos que almacenará la lista
 */
public class LinkedList<E> {

    // Atributos de LinkedList
    private Node<E> head;
    private Node<E> last;
    private Integer length;

    // Constructor
    public LinkedList() {
        this.head = null;
        this.last = null;
        this.length = 0;
    }

    // Métodos básicos de la lista
    public Integer getLength() {
        return length;
    }

    public Boolean isEmpty() {
        return head == null || length == 0;
    }

    private Node<E> getNode(Integer pos) throws ListEmptyException {
        if (isEmpty()) {
            throw new ListEmptyException("La lista está vacía");
        } else if (pos < 0 || pos >= length) {
            throw new ListEmptyException("Índice fuera de rango");
        } else if (pos == 0) {
            return head;
        } else if ((length - 1) == pos) {
            return last;
        } else {
            Node<E> search = head;
            Integer cont = 0;
            while (cont < pos) {
                search = search.getNext();
                cont++;
            }
            return search;
        }
    }

    public E get(Integer pos) throws ListEmptyException {
        return getNode(pos).getData();
    }

    private void addFirst(E data) {
        if (isEmpty()) {
            Node<E> aux = new Node<>(data);
            head = aux;
            last = aux;
            length++;
        } else {
            Node<E> head_old = head;
            Node<E> aux = new Node<>(data, head_old);
            head = aux;
            length++;
        }
    }

    private void addLast(E data) {
        if (isEmpty()) {
            addFirst(data);
        } else {
            Node<E> aux = new Node<>(data);
            last.setNext(aux);
            last = aux;
            length++;
        }
    }

    public void add(E data, Integer pos) throws ListEmptyException {
        if (pos < 0 || pos > length) {
            throw new ListEmptyException("Índice fuera de rango");
        } else if (pos == 0) {
            addFirst(data);
        } else if (length == pos) {
            addLast(data);
        } else {
            Node<E> search_preview = getNode(pos - 1);
            Node<E> search = getNode(pos);
            Node<E> aux = new Node<>(data, search);
            search_preview.setNext(aux);
            length++;
        }
    }

    public void add(E data) {
        addLast(data);
    }

    public String print() {
        if (isEmpty()) {
            return "La lista está vacía";
        } else {
            StringBuilder resp = new StringBuilder();
            Node<E> help = head;
            while (help != null) {
                resp.append(help.getData()).append(" -> ");
                help = help.getNext();
            }
            resp.append("\n");
            return resp.toString();
        }
    }

    public void update(E data, Integer pos) throws ListEmptyException {
        if (pos < 0 || pos >= length) {
            throw new ListEmptyException("Índice fuera de rango");
        }
        getNode(pos).setData(data);
    }

    public E deleteFirst() throws ListEmptyException {
        if (isEmpty()) {
            throw new ListEmptyException("La lista está vacía");
        } else {
            E data = head.getData();
            Node<E> aux = head.getNext();
            head = aux;
            if (length == 1) {
                last = null;
            }
            length--;
            return data;
        }
    }

    public E deleteLast() throws ListEmptyException {
        if (isEmpty()) {
            throw new ListEmptyException("La lista está vacía");
        } else if (length == 1) {
            E data = last.getData();
            head = null;
            last = null;
            length--;
            return data;
        } else {
            E data = last.getData();
            Node<E> newLast = getNode(length - 2);
            newLast.setNext(null);
            last = newLast;
            length--;
            return data;
        }
    }

    public E delete(Integer pos) throws ListEmptyException {
        if (isEmpty()) {
            throw new ListEmptyException("La lista está vacía");
        } else if (pos < 0 || pos >= length) {
            throw new ListEmptyException("Índice fuera de rango");
        } else if (pos == 0) {
            return deleteFirst();
        } else if ((length - 1) == pos) {
            return deleteLast();
        } else {
            Node<E> search_preview = getNode(pos - 1);
            Node<E> search = getNode(pos);
            E data = search.getData();
            search_preview.setNext(search.getNext());
            length--;
            return data;
        }
    }

    public void clear() {
        head = null;
        last = null;
        length = 0;
    }
    
    public boolean contains(E data) {
        if (isEmpty()) {
            return false;
        }
        
        Node<E> current = head;
        while (current != null) {
            if (current.getData().equals(data)) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    // Métodos para análisis de rendimiento
    public interface StringConverter<T> {
        T convert(String str);
    }

    /**
     * Carga datos desde un archivo a esta lista y a un array
     * @param filePath Ruta del archivo
     * @param array Array donde cargar los datos
     * @param converter Conversor de String al tipo E
     * @throws IOException Si hay un error leyendo el archivo
     */
    public void loadDataFromFile(String filePath, E[] array, StringConverter<E> converter) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int index = 0;
            
            while ((line = reader.readLine()) != null && index < array.length) {
                E item = converter.convert(line);
                array[index++] = item;
                this.add(item);
            }
        }
    }

    /**
     * Encuentra elementos duplicados en esta lista
     * @return Lista de elementos duplicados
     * @throws ListEmptyException Si hay un error con la lista
     */
    public List<E> findDuplicates() throws ListEmptyException {
        List<E> duplicates = new ArrayList<>();
        List<E> seen = new ArrayList<>();
        
        for (int i = 0; i < this.getLength(); i++) {
            E item = this.get(i);
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

// Excepción personalizada
class ListEmptyException extends Exception {
    public ListEmptyException(String message) {
        super(message);
    }
}