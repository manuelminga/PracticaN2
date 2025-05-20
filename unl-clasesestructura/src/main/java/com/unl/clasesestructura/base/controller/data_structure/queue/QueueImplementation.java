package com.unl.clasesestructura.base.controller.data_structure.queue;

import com.unl.clasesestructura.base.controller.data_structure.LinkedList;

public class QueueImplementation<E> extends LinkedList<E> {
    private Integer top;

    // getter
    public Integer getTop() {
        return top;
    }

    public QueueImplementation(Integer top) {
        this.top = top;
    }

    protected boolean isFullQueue() {
        return this.top >= getLength();
    }

    protected void queue(E info) throws Exception {
        if (!isFullQueue()) {
            add(info, 0);
        } else {
            throw new ArrayIndexOutOfBoundsException("Queque is full");
        }
    }

    protected E dequeue() throws Exception {
        return deleteFirst();
    }

}