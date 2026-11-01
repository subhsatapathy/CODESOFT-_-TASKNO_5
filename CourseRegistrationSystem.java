
//Task 5
//------------------

package Task5;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// Course Database Model
class Course {
    private String courseCode;
    private String title;
    private String description;
    private int capacity;
    private int enrolledCount;
    private String schedule;

    public Course(String courseCode, String title, String description, int capacity, String schedule) {
        this.courseCode = courseCode;
        this.title = title;
        this.description = description;
        this.capacity = capacity;
        this.enrolledCount = 0;
        this.schedule = schedule;
    }

    public String getCourseCode() { return courseCode; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getCapacity() { return capacity; }
    public int getEnrolledCount() { return enrolledCount; }
    public String getSchedule() { return schedule; }

    public int getAvailableSlots() {
        return capacity - enrolledCount;
    }

    public boolean enrollStudent() {
        if (enrolledCount < capacity) {
            enrolledCount++;
            return true;
        }
        return false;
    }

    public boolean dropStudent() {
        if (enrolledCount > 0) {
            enrolledCount--;
            return true;
        }
        return false;
    }

    public void displayCourseDetails() {
        System.out.println("[" + courseCode + "] " + title);
        System.out.println("  Description: " + description);
        System.out.println("  Schedule:    " + schedule);
        System.out.println("  Capacity:    " + enrolledCount + " / " + capacity + " (Slots available: " + getAvailableSlots() + ")");
        System.out.println("--------------------------------------------------");
    }
}

// Student Database Model
class Student {
    private String studentId;
    private String name;
    private List<Course> registeredCourses;

    public Student(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
        this.registeredCourses = new ArrayList<>();
    }

    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public List<Course> getRegisteredCourses() { return registeredCourses; }

    public boolean registerCourse(Course course) {
        if (registeredCourses.contains(course)) {
            System.out.println("\n[Error] You are already registered for this course.");
            return false;
        }
        if (course.enrollStudent()) {
            registeredCourses.add(course);
            System.out.println("\n[Success] Successfully registered for " + course.getTitle() + "!");
            return true;
        } else {
            System.out.println("\n[Error] Course is full! No available slots left.");
            return false;
        }
    }

    public boolean dropCourse(Course course) {
        if (registeredCourses.contains(course)) {
            if (course.dropStudent()) {
                registeredCourses.remove(course);
                System.out.println("\n[Success] Successfully dropped " + course.getTitle() + "!");
                return true;
            }
        }
        System.out.println("\n[Error] You are not registered for this course.");
        return false;
    }

    public void viewRegisteredCourses() {
        System.out.println("\n--- Registered Courses for " + name + " ---");
        if (registeredCourses.isEmpty()) {
            System.out.println("You are not registered for any courses yet.");
        } else {
            for (Course c : registeredCourses) {
                System.out.println("- [" + c.getCourseCode() + "] " + c.getTitle() + " (" + c.getSchedule() + ")");
            }
        }
        System.out.println("------------------------------------------");
    }
}

// Main System Controller
public class CourseRegistrationSystem {
    private static Map<String, Course> courseDatabase = new HashMap<>();
    private static Map<String, Student> studentDatabase = new HashMap<>();

    public static void main(String[] args) {
        initializeDatabases();
        Scanner scanner = new Scanner(System.in);
        Student currentStudent = studentDatabase.get("S101"); 

        while (true) {
            System.out.println("\n==========================================");
            System.out.println("     COURSE REGISTRATION SYSTEM (CODSOFT) ");
            System.out.println("==========================================");
            System.out.println("Logged in as: " + currentStudent.getName() + " (" + currentStudent.getStudentId() + ")");
            System.out.println("1. View Available Courses");
            System.out.println("2. Register for a Course");
            System.out.println("3. Drop a Course");
            System.out.println("4. View My Registered Courses");
            System.out.println("5. Exit");
            System.out.print("Choose an option (1-5): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    displayCourseListing();
                    pressEnterToContinue(scanner);
                    break;
                case "2":
                    displayCourseListing();
                    System.out.print("Enter Course Code to register (e.g., CS101): ");
                    String regCode = scanner.nextLine().trim().toUpperCase();
                    Course regCourse = courseDatabase.get(regCode);
                    if (regCourse != null) {
                        currentStudent.registerCourse(regCourse);
                    } else {
                        System.out.println("\n[Error] Invalid course code.");
                    }
                    pressEnterToContinue(scanner);
                    break;
                case "3":
                    currentStudent.viewRegisteredCourses();
                    if (!currentStudent.getRegisteredCourses().isEmpty()) {
                        System.out.print("Enter Course Code to drop: ");
                        String dropCode = scanner.nextLine().trim().toUpperCase();
                        Course dropCourse = courseDatabase.get(dropCode);
                        if (dropCourse != null) {
                            currentStudent.dropCourse(dropCourse);
                        } else {
                            System.out.println("\n[Error] Invalid course code.");
                        }
                    }
                    pressEnterToContinue(scanner);
                    break;
                case "4":
                    currentStudent.viewRegisteredCourses();
                    pressEnterToContinue(scanner);
                    break;
                case "5":
                    System.out.println("\nExiting system. Have a great day!");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("\n[Error] Invalid choice. Please select between 1 and 5.");
                    pressEnterToContinue(scanner);
            }
        }
    }

    private static void initializeDatabases() {
        courseDatabase.put("CS101", new Course("CS101", "Introduction to Java", "Learn core OOP concepts and syntax in Java.", 2, "Mon/Wed 10:00 AM"));
        courseDatabase.put("MTH201", new Course("MTH201", "Discrete Mathematics", "Logic, sets, graphs, and combinatorial analysis.", 3, "Tue/Thu 2:00 PM"));
        courseDatabase.put("ENG102", new Course("ENG102", "Technical Communication", "Effective writing and presentation for engineers.", 1, "Fri 9:00 AM"));

        studentDatabase.put("S101", new Student("S101", "Subhrankita Satapathy"));
        studentDatabase.put("S102", new Student("S102", "Prangya Paramita Priyadarshinee"));
    }

    private static void displayCourseListing() {
        System.out.println("\n==========================================");
        System.out.println("              AVAILABLE COURSES           ");
        System.out.println("==========================================");
        for (Course course : courseDatabase.values()) {
            course.displayCourseDetails();
        }
    }

    private static void pressEnterToContinue(Scanner scanner) {
        System.out.print("\nPress Enter to return to the menu...");
        scanner.nextLine();
    }
}


















#CodSoft #Java #JavaDeveloper #TechInternship #ObjectOrientedProgramming #SoftwareEngineering #BuildInPublic */
