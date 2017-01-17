package adfctrl.utils;

public class Observable<T> extends ObservableCore<T> implements IValueReceiver<T> {
    
    private T value;
    
    public Observable() {
        super();
        value = null;
    }
    
    public Observable(T val) {
        super();
        value = val;
    }

    @Override
    public void setValue(T val) {
        value = val;
        notifyObservers(value);            
    }

    @Override
    public T getValue() {
        return value;
    }    
}
