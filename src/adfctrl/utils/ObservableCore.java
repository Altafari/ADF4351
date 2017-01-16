package adfctrl.utils;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class ObservableCore<T> implements IObservable<T> {

    protected final Set<IObserver<T>> observers;    
    
    public ObservableCore() {
        observers = new LinkedHashSet<IObserver<T>>();
    }

    @Override
    public boolean addObserver(IObserver<T> obs) {
        return observers.add(obs);
    }

    @Override
    public boolean removeObserver(IObserver<T> obs) {
        return observers.remove(obs);
    }

    @Override
    public abstract T getValue();
    
    protected void notifyObservers(T value) {
        for(IObserver<T> obs : observers) {
            obs.notifyChanged(value);
        }
    }
}
