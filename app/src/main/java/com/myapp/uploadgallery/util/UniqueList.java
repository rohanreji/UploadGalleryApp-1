package com.myapp.uploadgallery.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Very trimmed implementation of list with unique items.
 */
public class UniqueList<T> extends ArrayList<T> {
    private HashSet<T> set = new HashSet<T>();

    @Override
    public boolean add(final T o) {
        if (set.add(o)) {
            return super.add(o);
        }
        return false;
    }

    @Override
    public void add(final int index, final T element) {
        if (set.add(element)) {
            super.add(index, element);
        }
    }

    @Override
    public boolean addAll(final Collection<? extends T> c) {
        c.removeAll(set);
        return super.addAll(c);
    }
}
