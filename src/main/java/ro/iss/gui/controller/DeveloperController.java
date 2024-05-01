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
import ro.iss.gui.observer.Observer;
import ro.iss.model.Department;
import ro.iss.model.Developer;
import ro.iss.service.EmployeeManagementService;


public class DeveloperController implements Observer<StaffEvent> {
    private Developer loggedDeveloper;
    private EmployeeManagementService employeeService;

    @FXML
    private Label developerWelcomeLabel;
    @FXML
    private TableView<Developer> developersTableView;
    @FXML
    private TableColumn<Developer, String> developerNameColumn;
    @FXML
    private TableColumn<Developer, Long> developerBadgeColumn;
    @FXML
    private TableColumn<Developer, Department> developerDepartmentColumn;
    @FXML
    private CheckBox activeUsersFilterCheckbox;
    @FXML
    private Button logOutButton;

    public void initController(EmployeeManagementService service, Developer loggedDeveloper) {
        setLoggedDeveloper(loggedDeveloper);
        setEmployeeService(service);
        initWelcomeLabel();

        employeeService.addObserver(this);

    }

    public void setLoggedDeveloper(Developer loggedDeveloper) {
        this.loggedDeveloper = loggedDeveloper;
    }

    public void setEmployeeService(EmployeeManagementService employeeService) {
        this.employeeService = employeeService;
    }


    private void initWelcomeLabel() {
        developerWelcomeLabel.setFont(new Font("System", 34));
        developerWelcomeLabel.setText("Welcome, " + loggedDeveloper.getName() + "!");
    }

    @Override
    public void update(StaffEvent staffEvent) {

    }

    @FXML
    public void initialize() {

    }

    public void handleLogOut(ActionEvent actionEvent) {
        Stage currentStage = (Stage) logOutButton.getScene().getWindow();
        currentStage.close();
    }

    public void handleClockIn(ActionEvent actionEvent) {
        employeeService.clockIn(loggedDeveloper);
        AlertEvent.showMessage(null, Alert.AlertType.CONFIRMATION, "Clock in",
                "You have successfully clocked in! You will appear as active to your PM.");
    }

    public void handleClockOut(ActionEvent actionEvent) {
        employeeService.clockOut(loggedDeveloper);
        AlertEvent.showMessage(null, Alert.AlertType.CONFIRMATION, "Clock out",
                "You have successfully clocked out! " +
                        "You will no longer appear available to your co-workers for today.");
    }

    public void handleFinishTask(ActionEvent actionEvent) {
    }
}
