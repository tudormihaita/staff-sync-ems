package ro.iss.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.iss.gui.events.DeveloperTimeEvent;
import ro.iss.gui.events.StaffEvent;
import ro.iss.gui.events.TimeTrackingEventType;
import ro.iss.gui.observer.Observable;
import ro.iss.gui.observer.Observer;
import ro.iss.model.Department;
import ro.iss.model.Developer;
import ro.iss.model.Employee;
import ro.iss.model.ProjectManager;
import ro.iss.repository.DeveloperRepository;
import ro.iss.repository.EmployeeRepository;
import ro.iss.repository.ProjectManagerRepository;
import ro.iss.service.utils.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeManagementService implements Observable<StaffEvent> {
    private final EmployeeRepository employeeRepository;
    private final DeveloperRepository developerRepository;
    private final ProjectManagerRepository projectManagerRepository;

    private static final Logger log = LogManager.getLogger(EmployeeManagementService.class);

    private final List<Observer<StaffEvent>> observers = new ArrayList<>();

    public EmployeeManagementService(EmployeeRepository employeeRepository, DeveloperRepository developerRepository, ProjectManagerRepository projectManagerRepository) {
        log.info("Initializing Service with repository dependencies...");
        this.employeeRepository = employeeRepository;
        this.developerRepository = developerRepository;
        this.projectManagerRepository = projectManagerRepository;
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

        Employee employee = employeeRepository.findByLoginCredentials(username, hashedPassword).orElseThrow(() ->
            new IllegalArgumentException("Invalid login credentials!"));

        Optional<Developer> developer = developerRepository.findOne(employee.getId());
        if(developer.isPresent()) {
            log.traceExit("Logged in as developer: {}", developer.get());
            return developer.get();
        }

        Optional<ProjectManager> projectManager = projectManagerRepository.findOne(employee.getId());
        if(projectManager.isPresent()) {
            log.traceExit("Logged in as project manager: {}", projectManager.get());
            return projectManager.get();
        }

        log.error("Invalid employee provided");
        throw new IllegalArgumentException("Invalid employee provided, could not resolve login attempt!");
    }

    public Developer clockIn(Developer developer) {
        log.traceEntry("Developer {} is clocking in...", developer);
        developer.setLastLogin(LocalDateTime.now());
        Optional<Developer> activeDeveloper = developerRepository.update(developer);

        if (activeDeveloper.isEmpty()) {
            log.error("Could not clock in developer {}", developer);
            throw new IllegalArgumentException("Could not clock in developer!");
        }

        log.traceExit("Developer {} has clocked in", developer);
        notify(new DeveloperTimeEvent(TimeTrackingEventType.CLOCK_IN, developer, null));
        return developer;
    }

    public Developer clockOut(Developer developer) {
        log.traceEntry("Developer {} is clocking out...", developer);
        developer.setLastLogout(LocalDateTime.now());
        Optional<Developer> inactiveDeveloper = developerRepository.update(developer);

        if (inactiveDeveloper.isEmpty()) {
            log.error("Could not clock out developer {}", developer);
            throw new IllegalArgumentException("Could not clock out developer!");
        }

        log.traceExit("Developer {} has clocked out", developer);
        notify(new DeveloperTimeEvent(TimeTrackingEventType.CLOCK_OUT, developer, null));
        return developer;
    }

    public List<Developer> findActiveTeamDevelopers(Department team) {
        log.traceEntry("Retrieving active developers for team: {}", team);
        return developerRepository.findActiveTeamDevelopers(team);
    }

    public List<Developer> findTeamDevelopers(Department team) {
        log.traceEntry("Retrieving developers at time {} for team: {}", LocalDateTime.now(), team);
        return developerRepository.findTeamDevelopers(team);
    }

}
