package ro.iss.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.iss.Main;
import ro.iss.gui.events.AlertEvent;
import ro.iss.gui.events.StaffEvent;
import ro.iss.gui.observer.Observer;
import ro.iss.model.Developer;
import ro.iss.model.Employee;
import ro.iss.model.ProjectManager;
import ro.iss.service.EmployeeManagementService;

import java.io.IOException;

public class LoginController {
    private EmployeeManagementService employeeManagementService;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label loginStatusLabel;

    public void initController(EmployeeManagementService service) {
      setEmployeeManagementService(service);
    }

    public void setEmployeeManagementService(EmployeeManagementService employeeManagementService) {
        this.employeeManagementService = employeeManagementService;
    }

    private void setLoginStatus(String message) {
        loginStatusLabel.setTextFill(Color.RED);
        loginStatusLabel.setText(message);
    }

    public void handleLogIn(ActionEvent actionEvent) {
        if(usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            setLoginStatus("Please complete your login credentials!");
        }

        String username = usernameField.getText();
        String password = passwordField.getText();
        try {
            Employee loggedEmployee = employeeManagementService.logIn(username, password);
            if (loggedEmployee instanceof Developer) {
                initDeveloperView((Developer) loggedEmployee);
            }
            else if (loggedEmployee instanceof ProjectManager) {
                initProjectManagerView((ProjectManager) loggedEmployee);
            }
        }
        catch (RuntimeException exception) {
            setLoginStatus(exception.getMessage());
        }
    }

    private void initDeveloperView(Developer loggedEmployee) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/developer-view.fxml"));
            Stage dialogStage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());

            dialogStage.setTitle("Developer: " + loggedEmployee.getName());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(scene);

            DeveloperController developerController = fxmlLoader.getController();
            developerController.initController(employeeManagementService, loggedEmployee);
            dialogStage.show();
        } catch (IOException ioException) {
            AlertEvent.showMessage(null, Alert.AlertType.ERROR, "Login failure",
                    ioException.getMessage());
        }
    }

    private void initProjectManagerView(ProjectManager loggedEmployee) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/manager-view.fxml"));
            Stage dialogStage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());

            dialogStage.setTitle("Project Manager: " + loggedEmployee.getName());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(scene);

            ProjectManagerController managerController = fxmlLoader.getController();
            managerController.initController(employeeManagementService, loggedEmployee);
            dialogStage.show();
        } catch (IOException ioException) {
            AlertEvent.showMessage(null, Alert.AlertType.ERROR, "Login failure",
                    ioException.getMessage());
        }
    }


}
