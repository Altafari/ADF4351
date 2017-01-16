package adfctrl.utils;

public class Observable<T> extends ObservableCore<T> {
    
    private T value;
    
    public Observable() {
        super();
        value = null;
    }
    
    public Observable(T val) {
        super();
        value = val;
    }

    public boolean setValue(T val) {
        if (value != val) {
            value = val;
            notifyObservers(value);
            return true;
        }
        return false;
    }
    
    public void updateValueAndNotify(T val) {
        if (val != null) {
            value = val;
        }
        notifyObservers(value);
    }
    
    @Override
    public T getValue() {
        return value;
    }    
}
