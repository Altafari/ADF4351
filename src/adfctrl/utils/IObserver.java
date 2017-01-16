package adfctrl.utils;

public interface IObserver<T> {

    void notifyChanged(T newVal);    
}
