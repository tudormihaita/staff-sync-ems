package ro.iss.service;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.iss.gui.events.*;
import ro.iss.gui.observer.Observable;
import ro.iss.gui.observer.Observer;
import ro.iss.model.*;
import ro.iss.repository.*;
import ro.iss.service.utils.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeManagementService implements Observable<StaffEvent> {
    private final EmployeeRepository employeeRepository;
    private final DeveloperRepository developerRepository;
    private final ProjectManagerRepository projectManagerRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;


    private static final Logger log = LogManager.getLogger(EmployeeManagementService.class);

    private final List<Observer<StaffEvent>> observers = new ArrayList<>();

    @Autowired
    public EmployeeManagementService(EmployeeRepository employeeRepository, DeveloperRepository developerRepository,
                                     ProjectManagerRepository projectManagerRepository, ProjectRepository projectRepository, TaskRepository taskRepository) {
        log.info("Initializing Service with repository dependencies...");
        this.employeeRepository = employeeRepository;
        this.developerRepository = developerRepository;
        this.projectManagerRepository = projectManagerRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public void addObserver(Observer<StaffEvent> o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer<StaffEvent> o) {
        observers.remove(o);
    }

    @Override
    public void notify(StaffEvent t) {
        observers.forEach(observer -> observer.update(t));
    }

    public Employee logIn(String username, String password) {
        log.traceEntry("Attempting to log in user: {}", username);
        String hashedPassword = PasswordEncoder.hashPassword(password);

        Employee employee = employeeRepository.findByLoginCredentials(username, hashedPassword);
        if (employee != null) {
            Developer developer = developerRepository.findById(employee.getId()).orElse(null);
            if (developer != null) {
                log.traceExit("Developer {} has logged in", developer);
                return developer;
            }

            ProjectManager projectManager = projectManagerRepository.findById(employee.getId()).orElse(null);
            if (projectManager != null) {
                log.traceExit("Project Manager {} has logged in", projectManager);
                return projectManager;
            }
        }

        log.error("Invalid login credentials");
        throw new IllegalArgumentException("Invalid login credentials provided!");
    }

    @Transactional
    public Developer clockIn(Developer developer) {
        log.traceEntry("Developer {} is clocking in...", developer);
        developer.setLastLogin(LocalDateTime.now());

        try {
            Developer activeDeveloper = developerRepository.save(developer);
            log.traceExit("Developer {} has clocked in", developer);
            notify(new DeveloperTimeEvent(DeveloperTimeEventType.CLOCK_IN, developer, null));
            return activeDeveloper;
        }
        catch (Exception e) {
            log.error("Could not clock in developer {}", developer);
            throw new IllegalArgumentException("Could not clock in developer!");
        }
    }

    @Transactional
    public Developer clockOut(Developer developer) {
        log.traceEntry("Developer {} is clocking out...", developer);
        developer.setLastLogout(LocalDateTime.now());

        try {
            Developer inactiveDeveloper = developerRepository.save(developer);
            log.traceExit("Developer {} has clocked out", developer);
            notify(new DeveloperTimeEvent(DeveloperTimeEventType.CLOCK_OUT, developer, null));
            return inactiveDeveloper;
        }
        catch (Exception e) {
            log.error("Could not clock out developer {}", developer);
            throw new IllegalArgumentException("Could not clock out developer!");
        }
    }

    @Transactional
    public void addProject(ProjectManager manager, String tile, String client, LocalDate deadline, String requirements, String details) {
        log.traceEntry("Adding new project with title: {}", tile);
        Project project = new Project(manager, tile, client, deadline, ProjectStatus.IN_PROGRESS,  requirements, details);
        projectRepository.save(project);
        log.traceExit("Project {} has been added", project);

        notify(new ProjectManagementEvent(ProjectManagementEventType.ADD, project, null));
    }

    @Transactional
    public void updateProject(Project project) {
        log.traceEntry("Updating project: {}", project);
        projectRepository.save(project);
        log.traceExit("Project {} has been updated", project);

        notify(new ProjectManagementEvent(ProjectManagementEventType.UPDATE, project, null));
    }

    @Transactional
    public void deployProject(Project project) {
        log.traceEntry("Deploying project: {}", project);
        project.setStatus(ProjectStatus.DEPLOYED);
        projectRepository.save(project);
        log.traceExit("Project {} has been deployed", project);

        notify(new ProjectManagementEvent(ProjectManagementEventType.DEPLOY, null, project));
    }

    @Transactional
    public void addTask(Project project, String title, LocalDate deadline, PriorityLevel priority, TaskStatus status, String details) {
        log.traceEntry("Adding new task with title: {}", title);
        Task task = new Task(project, title, deadline, priority, status, details);
        taskRepository.save(task);
        log.traceExit("Task {} has been added", task);

        notify(new TaskManagementEvent(TaskManagementEventType.ADD, task, null));
    }

    @Transactional
    public void updateTask(Task task) {
        log.traceEntry("Updating task: {}", task);
        taskRepository.save(task);
        log.traceExit("Task {} has been updated", task);

        notify(new TaskManagementEvent(TaskManagementEventType.UPDATE, task, null));
    }

    @Transactional
    public void finishTask(Task task) {
        log.traceEntry("Finishing task: {}", task);
        task.setStatus(TaskStatus.DONE);
        taskRepository.save(task);
        log.traceExit("Task {} has been finished", task);

        notify(new TaskManagementEvent(TaskManagementEventType.FINISH, null, task));
    }

    @Transactional
    public void assignTask(Developer developer, Task task) {
        log.traceEntry("Assigning task {} to developer {}", task, developer);

        if (task.getDevelopers().contains(developer)) {
            log.error("Task {} is already assigned to developer {}", task, developer);
            throw new IllegalArgumentException("Task is already assigned to developer!");
        }

        task.getDevelopers().add(developer);
        developer.getTasks().add(task);
        developerRepository.save(developer);
        taskRepository.save(task);
        log.traceExit("Task has been assigned successfully assigned.");

        notify(new TaskManagementEvent(TaskManagementEventType.ASSIGN, task, null));
    }

    public List<Developer> findActiveTeamDevelopers(Department team) {
        log.traceEntry("Retrieving active developers for team: {}", team);
        return developerRepository.findActiveTeamDevelopers(team);
    }

    public List<Developer> findTeamDevelopers(Department team) {
        log.traceEntry("Retrieving developers at time {} for team: {}", LocalDateTime.now(), team);
        return developerRepository.findTeamDevelopers(team);
    }

    public List<Project> findActiveProjectsForManager(ProjectManager manager) {
        log.traceEntry("Retrieving projects for manager: {}", manager);
        return projectRepository.findActiveByManagerId(manager.getId());
    }

    public List<Project> findAllProjectsForManager(ProjectManager manager) {
        log.traceEntry("Retrieving projects for manager: {}", manager);
        return projectRepository.findAllByManagerId(manager.getId());
    }

    public List<Task> findTasksForProject(Project project) {
        log.traceEntry("Retrieving tasks for project: {}", project);
        return taskRepository.findByProjectId(project.getId());
    }

    public List<Task> findTasksManagedBy(ProjectManager manager) {
        log.traceEntry("Retrieving tasks managed by project manager: {}", manager);
        return taskRepository.findByManagerId(manager.getId());
    }

    public List<Task> findTasksAssignedTo(Developer developer) {
        log.traceEntry("Retrieving tasks assigned to developer: {}", developer);
        return taskRepository.findAssignedTasks(developer.getId());
    }
}
