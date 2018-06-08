package org.reactome.web.nursa.client.details.tabs.dataset;

import java.util.Comparator;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 *
 * @param <T> the inner type
 * @param <R> the return type
 */
public abstract class NullSafeComparator<T, R extends Comparable<R>> implements Comparator<T> {
    
    /**
     * Compares two values in a null-safe manner.
     * 
     * @param t1 the (possibly null) first comparand
     * @param t2 the (possibly null) second comparand
     * @return the null-safe comparison result
     */
    @Override
    public int compare(T t1, T t2) {
        R v1 = t1 == null ? null : getValue(t1);
        R v2 = t2 == null ? null : getValue(t2);
        if (v1 == null) {
            return v2 == null ? 0 : -1;
        } else {
            return v2 == null ? 1 : v1.compareTo(v2);
        }
    }
    
    abstract protected R getValue(T t);

}
