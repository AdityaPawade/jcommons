package com.adtsw.jcommons.ds;

import java.util.ArrayList;
import java.util.Collections;

public class SortedArrayList<T> extends ArrayList<T> {

    private final int maxSize;

    public SortedArrayList(int maxSize) {
        this.maxSize = maxSize;
    }

    public SortedArrayList() {
        this.maxSize = -1;
    }

    @SuppressWarnings("unchecked")
    public void insertSorted(T value) {
        add(value);
        Comparable<T> cmp = (Comparable<T>) value;
        for (int i = size()-1; i > 0 && cmp.compareTo(get(i-1)) < 0; i--) {
            Collections.swap(this, i, i - 1);
        }
        checkMaxSize();
    }

    private void checkMaxSize() {
        if(maxSize != -1 && size() > maxSize) {
            removeFirst();
        }
    }

    public T removeFirst() {
        T firstKey = size() > 0 ? get(0) : null;
        if(firstKey != null) {
            remove(0);
        }
        return firstKey;
    }
}