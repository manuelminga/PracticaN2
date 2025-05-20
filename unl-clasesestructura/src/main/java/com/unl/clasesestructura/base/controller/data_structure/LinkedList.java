package com.unl.clasesestructura.base.controller.data_structure;

import com.unl.clasesestructura.base.controller.excepcion.ListEmptyException;

public class LinkedList<E> {
    private Node<E> head;
    private Node<E> last;
    private Integer length;

    public Integer getLength() {
        return this.length;
    }

    public LinkedList() {
        head = null;
        last = null;
        length = 0;
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
            int cont = 0;
            while (cont < pos) {
                cont++;
                search = search.getNext();
            }
            return search;
        }
    }

    private E getDataFirst() {
        if (isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("List empty");
        } else {
            return head.getData();
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
        } else {
            Node<E> aux = new Node<>(data, head);
            head = aux;
        }
        length++;
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
        if (pos == 0) {
            addFirst(data);
        } else if (pos.equals(length)) {
            addLast(data);
        } else {
            Node<E> previous = getNode(pos - 1);
            Node<E> current = getNode(pos);
            Node<E> aux = new Node<>(data, current);
            previous.setNext(aux);
            length++;
        }
    }

    public void add(E data) {
        addLast(data);
    }

    public String print() {
        if (isEmpty()) {
            return "La lista está vacía";
        }

        StringBuilder sb = new StringBuilder();
        Node<E> current = head;
        while (current != null) {
            sb.append(current.getData()).append(" -> ");
            current = current.getNext();
        }
        sb.append("\n");
        return sb.toString();
    }

    public void update(E data, Integer pos) throws ListEmptyException {
        getNode(pos).setData(data);
    }

    public void clear() {
        head = null;
        last = null;
        length = 0;
    }

    public E deleteFirst() throws ListEmptyException {
        if (isEmpty()) {
            throw new ListEmptyException("No se puede eliminar de una lista vacía");
        }

        E element = head.getData();
        head = head.getNext();
        if (length == 1) {
            last = null;
        }
        length--;
        return element;
    }

    public E deleteLast() throws ListEmptyException {
        if (isEmpty()) {
            throw new ListEmptyException("No se puede eliminar de una lista vacía");
        } else if (length == 1) {
            return deleteFirst();
        } else {
            Node<E> previous = getNode(length - 2);
            E element = last.getData();
            previous.setNext(null);
            last = previous;
            length--;
            return element;
        }
    }

    public E delete(Integer pos) throws ListEmptyException {
        if (isEmpty()) {
            throw new ListEmptyException("La lista está vacía");
        } else if (pos < 0 || pos >= length) {
            throw new ListEmptyException("Índice fuera de rango");
        } else if (pos == 0) {
            return deleteFirst();
        } else if (pos == length - 1) {
            return deleteLast();
        } else {
            Node<E> previous = getNode(pos - 1);
            Node<E> current = previous.getNext();
            E element = current.getData();
            previous.setNext(current.getNext());
            length--;
            return element;
        }
    }

    public E[] toArray() {
        if (isEmpty()) {
            return null;
        }

        @SuppressWarnings("unchecked")
        E[] array = (E[]) java.lang.reflect.Array.newInstance(
                head.getData().getClass(), length);

        Node<E> current = head;
        for (int i = 0; i < length; i++) {
            array[i] = current.getData();
            current = current.getNext();
        }
        return array;
    }

    public LinkedList<E> toList(E[] array) {
        clear();
        if (array != null) {
            for (E element : array) {
                add(element);
            }
        }
        return this;
    }
}