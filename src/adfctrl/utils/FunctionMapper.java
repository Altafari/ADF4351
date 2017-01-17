package adfctrl.utils;

import java.util.function.Function;

public class FunctionMapper<T, R> extends ObservableCore<R> {

    private final IObservable<T> x;
    private final Function<T, R> func;
    
    public FunctionMapper(IObservable<T> x, Function<T, R> func) {
        this.x = x;
        this.func = func;
        x.addObserver((s) -> notifyObservers(func.apply(s)));
    }
    
    @Override
    public R getValue() {
        return func.apply(x.getValue());
    }

}
