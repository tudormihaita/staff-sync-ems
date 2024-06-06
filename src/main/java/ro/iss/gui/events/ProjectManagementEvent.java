package ro.iss.gui.events;

import ro.iss.model.Project;

public class ProjectManagementEvent implements StaffEvent {
    private final ProjectManagementEventType type;

    private Project newData, oldData;

    public ProjectManagementEvent(ProjectManagementEventType type, Project newData, Project oldData) {
        this.type = type;
        this.newData = newData;
        this.oldData = oldData;
    }

    public ProjectManagementEventType getType() {
        return type;
    }

    public Project getNewData() {
        return newData;
    }

    public Project getOldData() {
        return oldData;
    }
}
