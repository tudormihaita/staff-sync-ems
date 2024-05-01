package ro.iss.gui.observer;

import ro.iss.gui.events.StaffEvent;

public interface Observable<E extends StaffEvent> {
    void addObserver(Observer<E> o);
    void removeObserver(Observer<E> o);
    void notify(E t);
}
