package ro.iss.gui.events;

import ro.iss.model.Developer;

public class DeveloperTimeEvent implements StaffEvent {
    private final DeveloperTimeEventType type;
    private Developer newData, oldData;

    public DeveloperTimeEvent(DeveloperTimeEventType type, Developer newData, Developer oldData) {
        this.type = type;
        this.newData = newData;
        this.oldData = oldData;
    }


    public DeveloperTimeEventType getType() {
        return type;
    }

    public Developer getNewData() {
        return newData;
    }

    public Developer getOldData() {
        return oldData;
    }
}
