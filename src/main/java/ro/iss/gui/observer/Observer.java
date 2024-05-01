package ro.iss.gui.observer;

import ro.iss.gui.events.StaffEvent;

public interface Observer<E extends StaffEvent> {
    void update(E e);
}
