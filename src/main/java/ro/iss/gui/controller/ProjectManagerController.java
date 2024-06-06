package ro.iss.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ro.iss.gui.events.*;
import ro.iss.gui.observer.Observer;
import ro.iss.model.*;
import ro.iss.service.EmployeeManagementService;

import java.time.LocalDate;
import java.util.List;


public class ProjectManagerController implements Observer<StaffEvent> {
    private ProjectManager loggedManager;
    private EmployeeManagementService employeeService;

    // Dashboard Tab
    @FXML
    private Label managerWelcomeLabel;
    @FXML
    private Button logOutButton;
    @FXML
    private Button assignTaskButton;
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
    private TableView<Task> taskAssignmentTableView;
    @FXML
    private TableColumn<Task, String> taskAssignmentTitleColumn;
    @FXML
    private TableColumn<Task, LocalDate> taskAssignmentDeadlineColumn;
    @FXML
    private TableColumn<Task, PriorityLevel> taskAssignmentPriorityColumn;
    @FXML
    private TableColumn<Task, TaskStatus> taskAssignmentStatusColumn;

    // Projects Tab
    @FXML
    private TableView<Project> projectsTableView;
    @FXML
    private TableColumn<Project, String> projectsTitleColumn;
    @FXML
    private TableColumn<Project, String> projectsClientColumn;
    @FXML
    private TableColumn<Project, LocalDate> projectsDeadlineColumn;
    @FXML
    private TableColumn<Project, ProjectStatus> projectsStatusColumn;
    @FXML
    private CheckBox activeProjectsFilterCheckbox;
    @FXML
    private TextArea projectDetailsArea;
    @FXML
    private TextArea projectReqArea;
    @FXML
    private TextField projectClientField;
    @FXML
    private TextField projectTitleField;
    @FXML
    private DatePicker projectDeadlinePicker;
    @FXML
    private ComboBox<ProjectStatus> projectStatusCombo;
    @FXML
    private Button addProjectButton;
    @FXML
    private Button updateProjectButton;
    @FXML
    private Button deployProjectButton;

    // Tasks Tab
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
    private TextArea taskDetailsArea;
    @FXML
    private TextField taskTitleField;
    @FXML
    private DatePicker taskDeadlinePicker;
    @FXML
    private ComboBox<Project> projectsCombo;
    @FXML
    private ComboBox<PriorityLevel> taskPriorityCombo;
    @FXML
    private ComboBox<TaskStatus> taskStatusCombo;
    @FXML
    private Button addTaskButton;
    @FXML
    private Button updateTaskButton;


    private ObservableList<Developer> developersModel = FXCollections.observableArrayList();
    private ObservableList<Project> projectsModel = FXCollections.observableArrayList();
    private ObservableList<Task> tasksModel = FXCollections.observableArrayList();



    public void initController(EmployeeManagementService service, ProjectManager loggedManager) {
        setLoggedManager(loggedManager);
        setEmployeeService(service);
        initWelcomeLabel();
        initDevelopersModel(false);
        initProjectsModel(false);
        initTasksModel();
        initTaskAssignmentModel();
        employeeService.addObserver(this);
    }

    public void setLoggedManager(ProjectManager loggedManager) {
        this.loggedManager = loggedManager;
    }

    public void setEmployeeService(EmployeeManagementService employeeService) {
        this.employeeService = employeeService;
    }

    private void initWelcomeLabel() {
        managerWelcomeLabel.setFont(new Font("System", 36));
        managerWelcomeLabel.setText("Welcome, " + loggedManager.getName() + "!");
    }

    @Override
    public void update(StaffEvent staffEvent) {
        if(staffEvent instanceof DeveloperTimeEvent timeEvent) {
            if (activeUsersFilterCheckbox.isSelected()) {
                if (timeEvent.getType() == DeveloperTimeEventType.CLOCK_IN ||
                        timeEvent.getType() == DeveloperTimeEventType.CLOCK_OUT) {
                    initDevelopersModel(activeUsersFilterCheckbox.isSelected());
                }
            }
        }
        else if(staffEvent instanceof ProjectManagementEvent projectEvent) {
            if (projectEvent.getType() == ProjectManagementEventType.ADD ||
                    projectEvent.getType() == ProjectManagementEventType.UPDATE ||
                    projectEvent.getType() == ProjectManagementEventType.DEPLOY) {
                initProjectsModel(activeProjectsFilterCheckbox.isSelected());
            }
        }
        else if(staffEvent instanceof TaskManagementEvent taskEvent) {
            if (taskEvent.getType() == TaskManagementEventType.ADD || taskEvent.getType() == TaskManagementEventType.FINISH ||
                    taskEvent.getType() == TaskManagementEventType.UPDATE || taskEvent.getType() == TaskManagementEventType.ASSIGN) {
                initTasksModel();
                initTaskAssignmentModel();
            }
        }
    }

    @FXML
    public void initialize() {
        developerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        developerBadgeColumn.setCellValueFactory(new PropertyValueFactory<>("badgeNo"));
        developerDepartmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        developersTableView.setItems(developersModel);

        projectsTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        projectsClientColumn.setCellValueFactory(new PropertyValueFactory<>("client"));
        projectsDeadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        projectsStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        projectsTableView.setItems(projectsModel);

        tasksTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        tasksDeadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        tasksPriorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        tasksStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        tasksTableView.setItems(tasksModel);

        taskAssignmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        taskAssignmentDeadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        taskAssignmentPriorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        taskAssignmentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        taskAssignmentTableView.setItems(tasksModel);

        activeUsersFilterCheckbox.setSelected(false);
        activeUsersFilterCheckbox.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            initDevelopersModel(newValue);
        });

        activeProjectsFilterCheckbox.setSelected(false);
        activeProjectsFilterCheckbox.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            initProjectsModel(newValue);
        });

        projectsCombo.setItems(projectsModel);
        taskPriorityCombo.setItems(FXCollections.observableArrayList(PriorityLevel.values()));
        taskStatusCombo.setItems(FXCollections.observableArrayList(TaskStatus.values()));
        projectStatusCombo.setItems(FXCollections.observableArrayList(ProjectStatus.values()));

        projectsTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                projectClientField.setText(newValue.getClient());
                projectTitleField.setText(newValue.getTitle());
                projectDeadlinePicker.setValue(newValue.getDeadline());
                projectStatusCombo.setValue(newValue.getStatus());
                projectReqArea.setText(newValue.getRequirements());
                projectDetailsArea.setText(newValue.getDetails());
            }
        });

        tasksTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                taskTitleField.setText(newValue.getTitle());
                taskDeadlinePicker.setValue(newValue.getDeadline());
                taskPriorityCombo.setValue(newValue.getPriority());
                taskStatusCombo.setValue(newValue.getStatus());
                taskDetailsArea.setText(newValue.getDetails());
            }
        });


        projectsCombo.setItems(projectsModel);
        projectsCombo.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                initTasksModel();
            }
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

    private void initProjectsModel(Boolean activeFilter) {
        List<Project> projects;
        if (activeFilter) {
            projects = employeeService.findActiveProjectsForManager(loggedManager);
        }
        else {
            projects = employeeService.findAllProjectsForManager(loggedManager);
        }
        projectsModel.setAll(projects);
    }

    private void initTasksModel() {
        Project selectedProject = projectsCombo.getSelectionModel().getSelectedItem();
        if (selectedProject != null) {
            List<Task> tasks = employeeService.findTasksForProject(selectedProject);
            tasksModel.setAll(tasks);
        }
    }

    private void initTaskAssignmentModel() {
      List<Task> tasks = employeeService.findTasksManagedBy(loggedManager);
      tasksModel.setAll(tasks);
    }

    public void handleLogOut(ActionEvent actionEvent) {
        Stage currentStage = (Stage) logOutButton.getScene().getWindow();
        currentStage.close();
    }

    public void handleAssignTask(ActionEvent actionEvent) {
        Developer selectedDeveloper = developersTableView.getSelectionModel().getSelectedItem();
        Task selectedTask = taskAssignmentTableView.getSelectionModel().getSelectedItem();

        if (selectedDeveloper == null || selectedTask == null) {
            AlertEvent.showMessage(null, Alert.AlertType.ERROR, "Assignment failure",
                    "Please select a developer and a task to assign!");
            return;
        }

        try {
            employeeService.assignTask(selectedDeveloper, selectedTask);
            AlertEvent.showMessage(null, Alert.AlertType.CONFIRMATION, "Assignment success",
                    "Task assigned successfully!");
        }
        catch (RuntimeException exception) {
            AlertEvent.showMessage(null, Alert.AlertType.ERROR, "Assignment failure",
                    exception.getMessage());
        }
    }

    public void handleAddProject(ActionEvent actionEvent) {
        String projectRequirements = projectReqArea.getText();
        String projectDetails = projectDetailsArea.getText();
        String projectTitle = projectTitleField.getText();
        String projectClient = projectClientField.getText();
        LocalDate projectDeadline = projectDeadlinePicker.getValue();

        if(projectRequirements == null || projectDetails == null || projectClient == null || projectDeadline == null || projectTitle == null ||
        projectDeadline.isBefore(LocalDate.now())) {
            AlertEvent.showMessage(null, Alert.AlertType.ERROR, "Add failure",
                    "Invalid project details provided!");
            return;
        }

        try {
            employeeService.addProject(loggedManager, projectTitle, projectClient, projectDeadline, projectRequirements, projectDetails);
            AlertEvent.showMessage(null, Alert.AlertType.CONFIRMATION, "Add success",
                    "Project added successfully!");
        }
        catch (RuntimeException exception) {
            AlertEvent.showMessage(null, Alert.AlertType.ERROR, "Add failure",
                    exception.getMessage());
        }
    }

    public void handleUpdateProject(ActionEvent actionEvent) {
        String projectRequirements = projectReqArea.getText();
        String projectDetails = projectDetailsArea.getText();
        String projectTitle = projectTitleField.getText();
        String projectClient = projectClientField.getText();
        LocalDate projectDeadline = projectDeadlinePicker.getValue();
        ProjectStatus projectStatus = projectStatusCombo.getValue();

        if(projectRequirements == null || projectDetails == null || projectClient == null || projectDeadline == null || projectTitle == null ||
                projectStatus == null || projectDeadline.isBefore(LocalDate.now())) {
            AlertEvent.showMessage(null, Alert.AlertType.ERROR, "Add failure",
                    "Invalid project details provided!");
            return;
        }

        Project selectedProject = projectsTableView.getSelectionModel().getSelectedItem();
        if (selectedProject == null) {
            AlertEvent.showMessage(null, Alert.AlertType.ERROR, "Update failure",
                    "Please select a project to update!");
            return;
        }

        try {
            selectedProject.setClient(projectClient);
            selectedProject.setTitle(projectTitle);
            selectedProject.setDeadline(projectDeadline);
            selectedProject.setStatus(projectStatus);
            selectedProject.setRequirements(projectRequirements);
            selectedProject.setDetails(projectDetails);
            employeeService.updateProject(selectedProject);
            AlertEvent.showMessage(null, Alert.AlertType.CONFIRMATION, "Update success",
                    "Project updated successfully!");
        }
        catch (RuntimeException exception) {
            AlertEvent.showMessage(null, Alert.AlertType.ERROR, "Update failure",
                    exception.getMessage());
        }
    }

    public void handleDeployProject(ActionEvent actionEvent) {
        Project selectedProject = projectsTableView.getSelectionModel().getSelectedItem();
        if (selectedProject == null) {
            AlertEvent.showMessage(null, Alert.AlertType.ERROR, "Deployment failure",
                    "Please select a project to deploy!");
            return;
        }

        try {
            employeeService.deployProject(selectedProject);
            AlertEvent.showMessage(null, Alert.AlertType.CONFIRMATION, "Deployment success",
                    "Project deployed successfully!");
        }
        catch (RuntimeException exception) {
            AlertEvent.showMessage(null, Alert.AlertType.ERROR, "Deployment failure",
                    exception.getMessage());
        }

    }

    public void handleAddTask(ActionEvent actionEvent) {
        String taskDetails = taskDetailsArea.getText();
        String taskTitle = taskTitleField.getText();
        LocalDate taskDeadline = taskDeadlinePicker.getValue();
        PriorityLevel taskPriority = taskPriorityCombo.getValue();
        TaskStatus taskStatus = taskStatusCombo.getValue();
        Project selectedProject = projectsCombo.getSelectionModel().getSelectedItem();

        if(taskDetails == null || taskTitle == null || taskDeadline == null || taskPriority == null || taskStatus == null ||
                selectedProject == null || taskDeadline.isBefore(LocalDate.now()) || taskStatus == TaskStatus.DONE) {
            AlertEvent.showMessage(null, Alert.AlertType.ERROR, "Add failure",
                    "Invalid task details provided!");
            return;
        }

        try {
            employeeService.addTask(selectedProject, taskTitle, taskDeadline, taskPriority, taskStatus, taskDetails);
            AlertEvent.showMessage(null, Alert.AlertType.CONFIRMATION, "Add success",
                    "Task added successfully!");
        }
        catch (RuntimeException exception) {
            AlertEvent.showMessage(null, Alert.AlertType.ERROR, "Add failure",
                    exception.getMessage());
        }
    }

    public void handleUpdateTask(ActionEvent actionEvent) {
        String taskDetails = taskDetailsArea.getText();
        String taskTitle = taskTitleField.getText();
        LocalDate taskDeadline = taskDeadlinePicker.getValue();
        PriorityLevel taskPriority = taskPriorityCombo.getValue();
        TaskStatus taskStatus = taskStatusCombo.getValue();
        Project selectedProject = projectsCombo.getSelectionModel().getSelectedItem();

        if(taskDetails == null || taskTitle == null || taskDeadline == null || taskPriority == null || taskStatus == null ||
                selectedProject == null || taskDeadline.isBefore(LocalDate.now()) || taskStatus == TaskStatus.DONE) {
            AlertEvent.showMessage(null, Alert.AlertType.ERROR, "Update failure",
                    "Invalid task details provided!");
            return;
        }

        Task selectedTask = tasksTableView.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            AlertEvent.showMessage(null, Alert.AlertType.ERROR, "Update failure",
                    "Please select a task to update!");
            return;
        }

        try {
            selectedTask.setTitle(taskTitle);
            selectedTask.setDeadline(taskDeadline);
            selectedTask.setPriority(taskPriority);
            selectedTask.setStatus(taskStatus);
            selectedTask.setDetails(taskDetails);
            employeeService.updateTask(selectedTask);
            AlertEvent.showMessage(null, Alert.AlertType.CONFIRMATION, "Update success",
                    "Task updated successfully!");
        }
        catch (RuntimeException exception) {
            AlertEvent.showMessage(null, Alert.AlertType.ERROR, "Update failure",
                    exception.getMessage());
        }
    }
}
