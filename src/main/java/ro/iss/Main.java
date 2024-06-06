package ro.iss;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
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

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        Application.launch(EMSApplication.class, args);
    }
}