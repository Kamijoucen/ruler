package com.kamijoucen.ruler.common;

import java.util.ArrayDeque;
import java.util.Iterator;

public class RStack<T> implements Iterable<T> {

    private ArrayDeque<T> queue;

    public RStack() {
        queue = new ArrayDeque<T>();
    }

    public int size() {
        return queue.size();
    }

    public void push(T item) {
        queue.push(item);
    }

    public T pop() {
        return queue.pop();
    }

    public T peek() {
        return queue.peek();
    }

    @Override
    public Iterator<T> iterator() {
        return queue.iterator();
    }

    public RStack<T> copy() {
        RStack<T> newStack = new RStack<T>();
        for (T t : this) {
            newStack.queue.add(t);
        }
        return newStack;
    }
}
