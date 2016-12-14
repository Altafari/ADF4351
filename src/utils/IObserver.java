package utils;

public interface IObserver<T> {    
    void notifyChanged(T newVal);    
}
