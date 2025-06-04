package com.unl.clasesestructura.base.controller.data_structure.stack;

import com.unl.clasesestructura.base.controller.data_structure.LinkedList;

public class StackImplementation<E> extends LinkedList<E> {
    private Integer top;

    // getter
    public Integer getTop() {
        return top;
    }

    public StackImplementation(Integer top) {
        this.top = top;
    }

    protected Boolean isFullStack() {
        return getLength() > this.top;
    }

    protected void push(E info) throws Exception {
        if (!isFullStack()) {
            add(info, 0);
        } else {
            throw new ArrayIndexOutOfBoundsException("Stack is full");
        }
    }

    protected E pop() throws Exception {
        return deleteFirst();
    }

}