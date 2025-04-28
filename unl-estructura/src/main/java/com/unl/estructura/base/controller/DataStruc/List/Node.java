package com.unl.estructura.base.controller.DataStruc.List;

/**
 * Clase Node para representar un nodo en la lista enlazada.
 * @param <E> Tipo de dato que almacenará el nodo
 */
public class Node<E> {
    private E data;
    private Node<E> next;
    
    /**
     * Constructor para un nodo sin siguiente nodo
     * @param data Datos a almacenar
     */
    public Node(E data) {
        this.data = data;
        this.next = null;
    }
    
    /**
     * Constructor para un nodo con siguiente nodo
     * @param data Datos a almacenar
     * @param next Referencia al siguiente nodo
     */
    public Node(E data, Node<E> next) {
        this.data = data;
        this.next = next;
    }
    
    /**
     * Obtiene los datos almacenados en el nodo
     * @return Datos almacenados
     */
    public E getData() {
        return data;
    }
    
    /**
     * Establece los datos del nodo
     * @param data Nuevos datos a almacenar
     */
    public void setData(E data) {
        this.data = data;
    }
    
    /**
     * Obtiene el siguiente nodo
     * @return Siguiente nodo
     */
    public Node<E> getNext() {
        return next;
    }
    
    /**
     * Establece el siguiente nodo
     * @param next Nuevo siguiente nodo
     */
    public void setNext(Node<E> next) {
        this.next = next;
    }
}