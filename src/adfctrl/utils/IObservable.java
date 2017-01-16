package adfctrl.utils;

public interface IObservable<T> {

    boolean addObserver(IObserver<T> obs);

    boolean removeObserver(IObserver<T> obs);

    T getValue();

}