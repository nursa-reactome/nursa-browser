package org.reactome.web.nursa.client.details.tabs.dataset;

import java.util.function.Function;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 *
 * @param <T> the inner type
 * @param <R> the return type
 * @param <S> the outer type
 */
public class NullSafeCurry<S, T, R> implements Function<S, R> {

    private Function<S, R> function;

    /**
     * Curries <code>outer(inner(s))</code> into the functional equivalent of
     * <code>s == null ? null : (inner(s) == null ? null : outer(inner(s)))</code>.
     * 
     * @param inner the first function to apply to the input
     * @param outer the second function to apply to the inner result
     */
    public NullSafeCurry(Function<S, T> inner, Function<T, R> outer) {
       this.function = new Function<S, R>() {

            @Override
            public R apply(S s) {
                T t = s == null ? null : inner.apply(s);
                return t == null ? null : outer.apply(t);
            }
            
        };

    }

    @Override
    public R apply(S s) {
        return this.function.apply(s);
    }
 
}
