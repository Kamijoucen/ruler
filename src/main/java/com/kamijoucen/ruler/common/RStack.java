package com.kamijoucen.ruler.common;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class RStack<T> {

    private final Deque<T> queue;

    public RStack() {
        queue = new ArrayDeque<>();
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

    public T peek(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        Iterator<T> iterator = queue.iterator();
        T result = null;
        for (int i = 0; i <= index; i++) {
            if (iterator.hasNext()) {
                result = iterator.next();
            } else {
                throw new IndexOutOfBoundsException("Index: " + index);
            }
        }
        return result;
    }

    public Iterator<T> iterator() {
        return queue.iterator();
    }

}
