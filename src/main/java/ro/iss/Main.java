package ro.iss;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ro.iss.gui.controller.LoginController;
import ro.iss.repository.DeveloperRepository;
import ro.iss.repository.EmployeeRepository;
import ro.iss.repository.ProjectManagerRepository;
import ro.iss.repository.database.DeveloperDBRepository;
import ro.iss.repository.database.EmployeeDBRepository;
import ro.iss.repository.database.ProjectManagerDBRepository;
import ro.iss.service.EmployeeManagementService;

import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        initView(stage);
        stage.setTitle("Staff Sync");
        stage.show();
    }

    private void initView(Stage stage) throws IOException {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("db.config"));
        } catch (IOException exception) {
            System.err.println("Cannot find db.config: " + exception);
        }

        EmployeeRepository employeeRepository = new EmployeeDBRepository(properties);
        DeveloperRepository developerRepository = new DeveloperDBRepository(properties);
        ProjectManagerRepository projectManagerRepository = new ProjectManagerDBRepository(properties);

        EmployeeManagementService employeeManagementService = new EmployeeManagementService(employeeRepository, developerRepository, projectManagerRepository);

        FXMLLoader fxmlLoader = new FXMLLoader(
                Main.class.getResource("views/login-view.fxml"));
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.getIcons().add(new Image(Objects.requireNonNull(
                Main.class.getResourceAsStream("media/staff-sync-icon.png"))));
        LoginController loginController = fxmlLoader.getController();
        loginController.initController(employeeManagementService);
    }

    public static void main(String[] args) {
        launch(args);
    }
}