package ro.iss;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import ro.iss.gui.controller.LoginController;
import ro.iss.service.EmployeeManagementService;

import java.util.Objects;

public class EMSApplication extends Application {
    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);
        this.applicationContext = new SpringApplicationBuilder().sources(Main.class).run(args);
    }

    @Override
    public void stop() {
        this.applicationContext.close();
        Platform.exit();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(EMSApplication.class.getResource("views/login-view.fxml"));
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.getIcons().add(new Image(Objects.requireNonNull(
                Main.class.getResourceAsStream("media/staff-sync-icon.png"))));

        LoginController loginController = fxmlLoader.getController();
        loginController.initController(applicationContext.getBean(EmployeeManagementService.class));

        stage.setTitle("Staff Sync");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
