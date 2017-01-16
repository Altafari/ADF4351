package adfctrl.utils;

public class SwitchCombiner<T> extends ObservableCore<T> {

    private final IObservable<Boolean> selector;
    private final IObservable<T> x;
    private final IObservable<T> y;

    public SwitchCombiner(IObservable<Boolean> selector, IObservable<T> x, IObservable<T> y) {
        this.selector = selector;
        this.x = x;
        this.y = y;
        selector.addObserver((s) -> notifyObservers(s? x.getValue() : y.getValue()));
        x.addObserver((s) -> notifyObservers(selector.getValue()? s : y.getValue()));
        y.addObserver((s) -> notifyObservers(selector.getValue()? x.getValue() : s));
    }

    @Override
    public T getValue() {
        return selector.getValue()? x.getValue() : y.getValue();
    }
    
}
