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

    public NullSafeCurry(Function<S, T> outer, Function<T, R> inner) {
       this.function = new Function<S, R>() {

            @Override
            public R apply(S s) {
                T t = s == null ? null : outer.apply(s);
                return t == null ? null : inner.apply(t);
            }
            
        };

    }

    @Override
    public R apply(S s) {
        return this.function.apply(s);
    }
 
}
