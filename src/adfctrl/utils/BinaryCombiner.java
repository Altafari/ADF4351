package adfctrl.utils;

import java.util.function.BiFunction;

public class BinaryCombiner<U, V, R> extends ObservableCore<R> {

    private final IObservable<U> x;
    private final IObservable<V> y;
    private final BiFunction<U, V, R> func;
    
    public BinaryCombiner(IObservable<U> x, IObservable<V> y, BiFunction<U, V, R> func) {
        super();
        this.x = x;
        this.y = y;
        this.func = func;
        x.addObserver((s) -> notifyObservers(func.apply(s, y.getValue())));
        y.addObserver((s) -> notifyObservers(func.apply(x.getValue(), s)));
    }
    
    @Override
    public R getValue() {
        return func.apply(x.getValue(), y.getValue());
    }
}
