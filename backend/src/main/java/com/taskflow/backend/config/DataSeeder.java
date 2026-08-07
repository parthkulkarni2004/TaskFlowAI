package com.taskflow.backend.config;

import com.taskflow.backend.entity.*;
import com.taskflow.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Seeds the database with sample data matching the TaskFlow frontend's
 * dummy data (PROJECTS / TASKS / NOTIFICATIONS / CAL_EVENTS in script.js),
 * so the UI has real data to render on first run.
 */
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final NotificationRepository notificationRepository;
    private final CalendarEventRepository calendarEventRepository;
    private final ActivityLogRepository activityLogRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return; // already seeded
        }

        User john = userRepository.save(User.builder()
                .fullName("John Doe").email("john.doe@example.com")
                .password(passwordEncoder.encode("password123"))
                .role("Project Manager").phone("+1 (555) 123-4567").location("New York, USA")
                .bio("Passionate project manager with 5+ years of experience.")
                .avatarUrl("https://i.pravatar.cc/64?img=12").build());

        User jane = userRepository.save(User.builder()
                .fullName("Jane Smith").email("jane.smith@example.com")
                .password(passwordEncoder.encode("password123"))
                .role("Designer").avatarUrl("https://i.pravatar.cc/64?img=12").build());

        User mike = userRepository.save(User.builder()
                .fullName("Mike Johnson").email("mike.johnson@example.com")
                .password(passwordEncoder.encode("password123"))
                .role("Backend Developer").avatarUrl("https://i.pravatar.cc/64?img=32").build());

        User emily = userRepository.save(User.builder()
                .fullName("Emily Davis").email("emily.davis@example.com")
                .password(passwordEncoder.encode("password123"))
                .role("Frontend Developer").avatarUrl("https://i.pravatar.cc/64?img=47").build());

        User chris = userRepository.save(User.builder()
                .fullName("Chris Brown").email("chris.brown@example.com")
                .password(passwordEncoder.encode("password123"))
                .role("QA Engineer").avatarUrl("https://i.pravatar.cc/64?img=5").build());

        Project website = projectRepository.save(Project.builder()
                .name("TaskFlow Website").description("Official website redesign and development")
                .progress(75).dueDate(LocalDate.of(2024, 6, 15))
                .status(Project.ProjectStatus.ACTIVE).createdBy(john)
                .team(setOf(john, jane, mike)).build());

        Project mobile = projectRepository.save(Project.builder()
                .name("Mobile App").description("Cross-platform mobile application")
                .progress(40).dueDate(LocalDate.of(2024, 7, 30))
                .status(Project.ProjectStatus.ACTIVE).createdBy(john)
                .team(setOf(jane, mike, emily)).build());

        Project marketing = projectRepository.save(Project.builder()
                .name("Marketing Campaign").description("Q2 marketing and branding campaign")
                .progress(60).dueDate(LocalDate.of(2024, 5, 30))
                .status(Project.ProjectStatus.ACTIVE).createdBy(john)
                .team(setOf(john, chris)).build());

        Project designSystem = projectRepository.save(Project.builder()
                .name("Design System").description("Create reusable design components")
                .progress(80).dueDate(LocalDate.of(2024, 6, 10))
                .status(Project.ProjectStatus.ACTIVE).createdBy(john)
                .team(setOf(emily, chris)).build());

        Project adminDashboard = projectRepository.save(Project.builder()
                .name("Admin Dashboard").description("Internal admin dashboard")
                .progress(60).dueDate(LocalDate.of(2024, 6, 25))
                .status(Project.ProjectStatus.ON_HOLD).createdBy(john)
                .team(setOf(john, jane)).build());

        projectRepository.save(Project.builder()
                .name("SEO Optimization").description("Improve website SEO and performance")
                .progress(30).dueDate(LocalDate.of(2024, 7, 5))
                .status(Project.ProjectStatus.ACTIVE).createdBy(john)
                .team(setOf(chris, emily)).build());

        taskRepository.save(task("Design Login Page", website, jane, LocalDate.of(2024,5,26), Task.TaskStatus.IN_PROGRESS, Task.TaskPriority.HIGH, john));
        taskRepository.save(task("Setup Database", mobile, mike, LocalDate.of(2024,5,30), Task.TaskStatus.TODO, Task.TaskPriority.MEDIUM, john));
        taskRepository.save(task("Create Dashboard", adminDashboard, emily, LocalDate.of(2024,5,25), Task.TaskStatus.REVIEW, Task.TaskPriority.HIGH, john));
        taskRepository.save(task("Write Documentation", website, chris, LocalDate.of(2024,6,1), Task.TaskStatus.TODO, Task.TaskPriority.LOW, john));
        taskRepository.save(task("Fix UI Bugs", mobile, jane, LocalDate.of(2024,5,22), Task.TaskStatus.COMPLETED, Task.TaskPriority.HIGH, john));
        taskRepository.save(task("Build API Endpoints", mobile, mike, LocalDate.of(2024,5,28), Task.TaskStatus.IN_PROGRESS, Task.TaskPriority.HIGH, john));
        taskRepository.save(task("Design Wireframes", website, emily, LocalDate.of(2024,5,27), Task.TaskStatus.TODO, Task.TaskPriority.LOW, john));
        taskRepository.save(task("User Authentication", mobile, chris, LocalDate.of(2024,6,2), Task.TaskStatus.TODO, Task.TaskPriority.HIGH, john));
        taskRepository.save(task("UI/UX Review", website, jane, LocalDate.of(2024,5,29), Task.TaskStatus.REVIEW, Task.TaskPriority.MEDIUM, john));
        taskRepository.save(task("Setup CI/CD", adminDashboard, mike, LocalDate.of(2024,6,3), Task.TaskStatus.REVIEW, Task.TaskPriority.LOW, john));

        notificationRepository.save(Notification.builder().user(john).icon("\uD83D\uDCDD").text("Task \"Design Login Page\" has been assigned to you.").unread(true).build());
        notificationRepository.save(Notification.builder().user(john).icon("\u23F0").text("Task \"Setup Database\" is due tomorrow.").unread(true).build());
        notificationRepository.save(Notification.builder().user(john).icon("\uD83D\uDCAC").text("Mike Johnson mentioned you in a comment.").unread(true).build());
        notificationRepository.save(Notification.builder().user(john).icon("\uD83D\uDCC1").text("Project \"Mobile App\" has been updated.").unread(false).build());
        notificationRepository.save(Notification.builder().user(john).icon("\u2705").text("Task \"Fix UI Bugs\" has been completed.").unread(false).build());
        notificationRepository.save(Notification.builder().user(john).icon("\uD83D\uDCAC").text("Emily Davis commented on your task.").unread(false).build());

        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        calendarEventRepository.save(CalendarEvent.builder().label("Design Review").color("blue").eventDate(LocalDate.of(year, month, Math.min(3, LocalDate.now().lengthOfMonth()))).createdBy(john).build());
        calendarEventRepository.save(CalendarEvent.builder().label("Project Deadline").color("red").eventDate(LocalDate.of(year, month, Math.min(7, LocalDate.now().lengthOfMonth()))).createdBy(john).build());
        calendarEventRepository.save(CalendarEvent.builder().label("Team Meeting").color("purple").eventDate(LocalDate.of(year, month, Math.min(12, LocalDate.now().lengthOfMonth()))).createdBy(john).build());
        calendarEventRepository.save(CalendarEvent.builder().label("Release v1.2").color("green").eventDate(LocalDate.of(year, month, Math.min(23, LocalDate.now().lengthOfMonth()))).createdBy(john).build());

        activityLogRepository.save(ActivityLog.builder().icon("\uD83D\uDD04").title("Design system updated").user(john).build());
        activityLogRepository.save(ActivityLog.builder().icon("\u2705").title("Task \"Login Page\" completed").user(john).build());
        activityLogRepository.save(ActivityLog.builder().icon("\u2795").title("New task created: Dashboard UI").user(john).build());
        activityLogRepository.save(ActivityLog.builder().icon("\uD83D\uDCC1").title("Project \"TaskFlow\" updated").user(john).build());

        System.out.println("=========================================================");
        System.out.println(" TaskFlowAI: sample data seeded.");
        System.out.println(" Login with: john.doe@example.com / password123");
        System.out.println("=========================================================");
    }

    private Task task(String name, Project project, User assignee, LocalDate due, Task.TaskStatus status, Task.TaskPriority priority, User createdBy) {
        return Task.builder()
                .name(name).project(project).assignee(assignee).dueDate(due)
                .status(status).priority(priority).createdBy(createdBy)
                .build();
    }

    private Set<User> setOf(User... users) {
        return new HashSet<>(List.of(users));
    }
}
