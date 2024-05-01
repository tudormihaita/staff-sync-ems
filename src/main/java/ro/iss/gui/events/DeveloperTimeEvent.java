package ro.iss.gui.events;

import ro.iss.model.Developer;

public class DeveloperTimeEvent implements StaffEvent {
    private final TimeTrackingEventType type;
    private Developer newData, oldData;

    public DeveloperTimeEvent(TimeTrackingEventType type, Developer newData, Developer oldData) {
        this.type = type;
        this.newData = newData;
        this.oldData = oldData;
    }

    public DeveloperTimeEvent(TimeTrackingEventType type, Developer newData) {
        this.type = type;
        this.newData = newData;
    }


    public TimeTrackingEventType getType() {
        return type;
    }

    public Developer getNewData() {
        return newData;
    }

    public Developer getOldData() {
        return oldData;
    }
}
