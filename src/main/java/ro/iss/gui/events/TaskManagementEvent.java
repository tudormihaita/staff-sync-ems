package ro.iss.gui.events;

import ro.iss.model.Task;

public class TaskManagementEvent implements StaffEvent {
    private final TaskManagementEventType type;
    private final Task newData, oldData;

    public TaskManagementEvent(TaskManagementEventType type, Task newData, Task oldData) {
        this.type = type;
        this.newData = newData;
        this.oldData = oldData;
    }

    public TaskManagementEventType getType() {
        return type;
    }

    public Task getNewData() {
        return newData;
    }

    public Task getOldData() {
        return oldData;
    }
}
