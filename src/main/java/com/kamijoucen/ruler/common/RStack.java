package com.kamijoucen.ruler.common;

import java.util.ArrayDeque;
import java.util.Deque;

public class RStack<T> {

    private final Deque<T> queue;

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

}
