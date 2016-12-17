package adfctrl.utils;

import java.util.LinkedHashSet;
import java.util.Set;

public class Observable<T> {
    
    private T value;
    private final Set<IObserver<T>> observers;    
    
    public Observable() {
        observers = new LinkedHashSet<IObserver<T>>();
        value = null;
    }
    
    public Observable(T val) {
        observers = new LinkedHashSet<IObserver<T>>();
        value = val;
    }
    
    public boolean addObserver(IObserver<T> obs) {
        return observers.add(obs);
    }
    
    public boolean removeObserver(IObserver<T> obs) {
        return observers.remove(obs);
    }
    
    public boolean setValue(T val) {
        if (value != val) {
            value = val;
            notifyObservers();
            return true;
        }
        return false;
    }
    
    public void updateValueAndNotify(T val) {
        if (val != null) {
            value = val;
        }
        notifyObservers();
    }
    
    public T getValue() {
        return value;
    }
    
    private void notifyObservers() {
        for(IObserver<T> obs : observers) {
            obs.notifyChanged(value);
        }
    }
}