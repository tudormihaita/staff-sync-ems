package ro.iss.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ro.iss.gui.events.DeveloperTimeEvent;
import ro.iss.gui.events.StaffEvent;
import ro.iss.gui.events.TimeTrackingEventType;
import ro.iss.gui.observer.Observer;
import ro.iss.model.Department;
import ro.iss.model.Developer;
import ro.iss.model.ProjectManager;
import ro.iss.service.EmployeeManagementService;

import java.time.LocalDate;
import java.util.List;


public class ProjectManagerController implements Observer<StaffEvent> {
    private ProjectManager loggedManager;
    private EmployeeManagementService employeeService;

    @FXML
    private Label managerWelcomeLabel;
    @FXML
    private Button logOutButton;
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

    private ObservableList<Developer> developersModel = FXCollections.observableArrayList();


    public void initController(EmployeeManagementService service, ProjectManager loggedManager) {
        setLoggedManager(loggedManager);
        setEmployeeService(service);
        initWelcomeLabel();
        initDevelopersModel(false);
        employeeService.addObserver(this);
    }

    public void setLoggedManager(ProjectManager loggedManager) {
        this.loggedManager = loggedManager;
    }

    public void setEmployeeService(EmployeeManagementService employeeService) {
        this.employeeService = employeeService;
    }

    private void initWelcomeLabel() {
        managerWelcomeLabel.setFont(new Font("System", 34));
        managerWelcomeLabel.setText("Welcome, " + loggedManager.getName() + "!");
    }

    @Override
    public void update(StaffEvent staffEvent) {
        if(staffEvent instanceof DeveloperTimeEvent timeEvent) {
            if (activeUsersFilterCheckbox.isSelected()) {
                if (timeEvent.getType() == TimeTrackingEventType.CLOCK_IN ||
                        timeEvent.getType() == TimeTrackingEventType.CLOCK_OUT) {
                    initDevelopersModel(activeUsersFilterCheckbox.isSelected());
                }
            }
        }
    }

    @FXML
    public void initialize() {
        developerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        developerBadgeColumn.setCellValueFactory(new PropertyValueFactory<>("badgeNo"));
        developerDepartmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        developersTableView.setItems(developersModel);


        activeUsersFilterCheckbox.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            initDevelopersModel(newValue);
        });

    }

    private void initDevelopersModel(Boolean activeFilter) {
        List<Developer> developers;
        if (activeFilter) {
            developers = employeeService.findActiveTeamDevelopers(loggedManager.getDepartment());
        }
        else {
            developers = employeeService.findTeamDevelopers(loggedManager.getDepartment());
        }
        developersModel.setAll(developers);
    }

    public void handleLogOut(ActionEvent actionEvent) {
        Stage currentStage = (Stage) logOutButton.getScene().getWindow();
        currentStage.close();
    }
}
