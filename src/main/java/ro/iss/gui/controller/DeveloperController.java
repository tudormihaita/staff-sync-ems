package ro.iss.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ro.iss.gui.events.AlertEvent;
import ro.iss.gui.events.StaffEvent;
import ro.iss.gui.events.TaskManagementEvent;
import ro.iss.gui.events.TaskManagementEventType;
import ro.iss.gui.observer.Observer;
import ro.iss.model.*;
import ro.iss.service.EmployeeManagementService;

import java.time.LocalDate;


public class DeveloperController implements Observer<StaffEvent> {
    private Developer loggedDeveloper;
    private EmployeeManagementService employeeService;

    @FXML
    private Label developerWelcomeLabel;
    @FXML
    private TableView<Task> tasksTableView;
    @FXML
    private TableColumn<Task, String> tasksTitleColumn;
    @FXML
    private TableColumn<Task, LocalDate> tasksDeadlineColumn;
    @FXML
    private TableColumn<Task, PriorityLevel> tasksPriorityColumn;
    @FXML
    private TableColumn<Task, TaskStatus> tasksStatusColumn;
    @FXML
    private Button logOutButton;
    @FXML
    private Button clockInButton;
    @FXML
    private Button clockOutButton;
    @FXML
    private Button finishTaskButton;
    @FXML
    private Button updateTaskButton;
    @FXML
    private ComboBox<PriorityLevel> taskPriorityCombo;
    @FXML
    private TextField taskTitleField;
    @FXML
    private DatePicker taskDeadlinePicker;
    @FXML
    private TextArea taskDetailsArea;
    @FXML
    private ComboBox<TaskStatus> taskStatusCombo;

    private ObservableList<Task> tasksModel = FXCollections.observableArrayList();

    public void initController(EmployeeManagementService service, Developer loggedDeveloper) {
        setLoggedDeveloper(loggedDeveloper);
        setEmployeeService(service);
        initWelcomeLabel();
        initTasksModel();
        initTasksModel();

        employeeService.addObserver(this);

    }

    public void setLoggedDeveloper(Developer loggedDeveloper) {
        this.loggedDeveloper = loggedDeveloper;
    }

    public void setEmployeeService(EmployeeManagementService employeeService) {
        this.employeeService = employeeService;
    }


    private void initWelcomeLabel() {
        developerWelcomeLabel.setFont(new Font("System", 36));
        developerWelcomeLabel.setText("Welcome, " + loggedDeveloper.getName() + "!");
    }

    @Override
    public void update(StaffEvent staffEvent) {
        if (staffEvent instanceof TaskManagementEvent taskEvent) {
            if (taskEvent.getType() == TaskManagementEventType.ADD || taskEvent.getType() == TaskManagementEventType.FINISH ||
                    taskEvent.getType() == TaskManagementEventType.UPDATE || taskEvent.getType() == TaskManagementEventType.ASSIGN) {
                initTasksModel();
            }
        }
    }

    @FXML
    public void initialize() {
        clockInButton.setDisable(false);
        clockOutButton.setDisable(true);

        tasksTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        tasksDeadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        tasksPriorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        tasksStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        tasksTableView.setItems(tasksModel);

        tasksTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                taskTitleField.setText(newValue.getTitle());
                taskDeadlinePicker.setValue(newValue.getDeadline());
                taskPriorityCombo.setValue(newValue.getPriority());
                taskStatusCombo.setValue(newValue.getStatus());
                taskDetailsArea.setText(newValue.getDetails());
            }
        });

        taskPriorityCombo.setItems(FXCollections.observableArrayList(PriorityLevel.values()));
        taskStatusCombo.setItems(FXCollections.observableArrayList(TaskStatus.values()));
    }

    private void initTasksModel() {
        tasksModel.setAll(employeeService.findTasksAssignedTo(loggedDeveloper));
    }

    public void handleLogOut(ActionEvent actionEvent) {
        if(!clockOutButton.isDisabled()) {
            AlertEvent.showMessage(null, Alert.AlertType.ERROR, "Log out",
                    "You must clock out before logging out!");
            return;
        }

        Stage currentStage = (Stage) logOutButton.getScene().getWindow();
        currentStage.close();
    }

    public void handleClockIn(ActionEvent actionEvent) {
        try {
            loggedDeveloper = employeeService.clockIn(loggedDeveloper);
            AlertEvent.showMessage(null, Alert.AlertType.CONFIRMATION, "Clock in",
                    "You have successfully clocked in! You will appear as active to your PM.");
            clockOutButton.setDisable(false);
            clockInButton.setDisable(true);
        }
        catch (Exception e) {
            AlertEvent.showMessage(null, Alert.AlertType.ERROR, "Clock in",
                    "You have already clocked in today. Please clock out before clocking in again: " + e.getMessage());
        }
    }

    public void handleClockOut(ActionEvent actionEvent) {
        try {
            loggedDeveloper = employeeService.clockOut(loggedDeveloper);
            AlertEvent.showMessage(null, Alert.AlertType.CONFIRMATION, "Clock out",
                    "You have successfully clocked out! " +
                            "You will no longer appear available to your co-workers for today.");
            clockOutButton.setDisable(true);
            clockInButton.setDisable(false);
        }
        catch (Exception e) {
            AlertEvent.showMessage(null, Alert.AlertType.ERROR, "Clock out",
                    "You have already clocked out today. Please clock in before clocking out again: " + e.getMessage());
        }
    }

    public void handleFinishTask(ActionEvent actionEvent) {
        Task selectedTask = tasksTableView.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            AlertEvent.showMessage(null, Alert.AlertType.ERROR, "Finish task",
                    "Please select a task to finish!");
            return;
        }

        try {
            employeeService.finishTask(selectedTask);
            AlertEvent.showMessage(null, Alert.AlertType.CONFIRMATION, "Finish task",
                    "Task " + selectedTask.getTitle() + " has been finished!");
            initTasksModel();
        }
        catch (Exception e) {
            AlertEvent.showMessage(null, Alert.AlertType.ERROR, "Finish task",
                    "Could not finish task: " + e.getMessage());
        }
    }

    public void handleUpdateTask(ActionEvent actionEvent) {
        Task selectedTask = tasksTableView.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            AlertEvent.showMessage(null, Alert.AlertType.ERROR, "Update task",
                    "Please select a task to update!");
            return;
        }

        String title = taskTitleField.getText();
        LocalDate deadline = taskDeadlinePicker.getValue();
        PriorityLevel priority = taskPriorityCombo.getValue();
        TaskStatus status = taskStatusCombo.getValue();
        String details = taskDetailsArea.getText();

        if (title.isEmpty() || deadline == null || priority == null || status == null || details.isEmpty() || status == TaskStatus.DONE) {
            AlertEvent.showMessage(null, Alert.AlertType.ERROR, "Update task",
                    "Please complete all fields before updating a task!");
            return;
        }

        try {
            selectedTask.setTitle(title);
            selectedTask.setDeadline(deadline);
            selectedTask.setPriority(priority);
            selectedTask.setStatus(status);
            selectedTask.setDetails(details);

            employeeService.updateTask(selectedTask);
            AlertEvent.showMessage(null, Alert.AlertType.CONFIRMATION, "Update task",
                    "Task " + selectedTask.getTitle() + " has been updated!");
            initTasksModel();
        }
        catch (Exception e) {
            AlertEvent.showMessage(null, Alert.AlertType.ERROR, "Update task",
                    "Could not update task: " + e.getMessage());
        }
    }
}
