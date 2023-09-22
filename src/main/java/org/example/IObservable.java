package org.example;

/**
 * Observable pattern interface.
 */
public interface IObservable {

 void registerObserver(Observer o);

 void unregisterObserver(Observer o);

 void notifyObservers();

}
