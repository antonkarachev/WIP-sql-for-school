package ru.karachev.sqlforschool.view;

import ru.karachev.sqlforschool.controller.Controller;
import ru.karachev.sqlforschool.entity.Student;

import java.util.ResourceBundle;
import java.util.Scanner;

public class ViewProvider {

    private static final String COMMAND_FORMAT = "%-10s%s";
    private static final ResourceBundle menuResource = ResourceBundle.getBundle("menu");
    private static final String FIND_GROUPS_WITH_STUDENTS_COUNT_COMMAND =
            menuResource.getString("findGroupsWithStudentCount");
    private static final String FIND_STUDENTS_TO_COURSE_NAME_COMMAND =
            menuResource.getString("findStudentsToCourseName");
    private static final String ADD_NEW_STUDENT_COMMAND =
            menuResource.getString("addNewStudent");
    private static final String DELETE_STUDENT_BY_ID_COMMAND =
            menuResource.getString("deleteStudentById");
    private static final String ADD_STUDENT_TO_COURSE =
            menuResource.getString("addStudentToCourseById");
    private static final String REMOVE_STUDENT_FROM_COURSE_COMMAND =
            menuResource.getString("removeStudentFromCourse");
    private static final String EXIT_COMMAND = menuResource.getString("exit");

    private final Controller controller;

    public ViewProvider(Controller controller) {
        this.controller = controller;
    }

    public void provideView() {

        System.out.println(createGreeting());

        String command;
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter command: ");
            command = scanner.nextLine();

            if (FIND_GROUPS_WITH_STUDENTS_COUNT_COMMAND.equals(command)) {
                provideFindGroupsWithStudentCount(scanner);
            } else if (FIND_STUDENTS_TO_COURSE_NAME_COMMAND.equals(command)) {
                provideFindAllStudentsToCourseName(scanner);
            } else if (ADD_NEW_STUDENT_COMMAND.equals(command)) {
                provideAddNewStudent(scanner);
            } else if (ADD_STUDENT_TO_COURSE.equals(command)) {
                provideAddStudentToCourse(scanner);
            } else if (DELETE_STUDENT_BY_ID_COMMAND.equals(command)) {
                provideDeleteStudentById(scanner);
            } else if (REMOVE_STUDENT_FROM_COURSE_COMMAND.equals(command)) {
                provideRemoveStudentFromCourse(scanner);
            } else if (EXIT_COMMAND.equals(command)) {
                System.out.println("Bye, see you next time");
                break;
            } else {
                System.out.println("There are no such command");
            }
        }
    }

    private String createGreeting() {
        StringBuilder greeting = new StringBuilder();
        greeting.append(menuResource.getString("greeting"))
                .append(String.format(COMMAND_FORMAT, FIND_GROUPS_WITH_STUDENTS_COUNT_COMMAND,
                        menuResource.getString(FIND_GROUPS_WITH_STUDENTS_COUNT_COMMAND)))
                .append(String.format(COMMAND_FORMAT, FIND_STUDENTS_TO_COURSE_NAME_COMMAND,
                        menuResource.getString(FIND_STUDENTS_TO_COURSE_NAME_COMMAND)))
                .append(String.format(COMMAND_FORMAT, ADD_NEW_STUDENT_COMMAND,
                        menuResource.getString(ADD_NEW_STUDENT_COMMAND)))
                .append(String.format(COMMAND_FORMAT, DELETE_STUDENT_BY_ID_COMMAND,
                        menuResource.getString(DELETE_STUDENT_BY_ID_COMMAND)))
                .append(String.format(COMMAND_FORMAT, ADD_STUDENT_TO_COURSE,
                        menuResource.getString(ADD_STUDENT_TO_COURSE)))
                .append(String.format(COMMAND_FORMAT, REMOVE_STUDENT_FROM_COURSE_COMMAND,
                        menuResource.getString(REMOVE_STUDENT_FROM_COURSE_COMMAND)))
                .append(String.format(COMMAND_FORMAT, EXIT_COMMAND,
                        menuResource.getString(EXIT_COMMAND)));

        return greeting.toString();
    }

    private void provideFindGroupsWithStudentCount(Scanner scanner) {
        System.out.print("Enter counter: ");
        int counter = scanner.nextInt();
        controller.findAllGroupsWithLessOrEqualsStudentCountCommand(counter)
                .forEach(group -> {
                    System.out.println(group.toString());
                });
    }

    private void provideFindAllStudentsToCourseName(Scanner scanner) {
        System.out.println("Here are all courses which we have:");
        controller.getAllCoursesNames().forEach(System.out::println);
        System.out.print("Enter course name: ");
        String courseName = scanner.nextLine();
        controller.findAllStudentsRelatedToCourseName(courseName)
                .forEach(student -> {
                    System.out.println(student.toString());
                });
    }

    private void provideAddNewStudent(Scanner scanner) {
        System.out.print("Enter student_id: ");
        int studentId = scanner.nextInt();
        System.out.print("Enter group_id: ");
        int groupId = scanner.nextInt();
        System.out.print("Enter student_name: ");
        String studentName = scanner.next();
        System.out.print("Enter student_last_name: ");
        String studentLastName = scanner.next();
        Student student = Student.builder()
                .withStudentId(studentId)
                .withGroupId(groupId)
                .withName(studentName)
                .withLastName(studentLastName)
                .build();
        controller.addNewStudent(student);
        System.out.println("Student " + student.toString() + " added to database");
    }

    private void provideAddStudentToCourse(Scanner scanner) {
        System.out.print("Enter id of student you want add: ");
        int studentId = scanner.nextInt();
        System.out.print("Enter id of the course you want add student: ");
        int courseId = scanner.nextInt();
        controller.addStudentToSpecifiedCourse(studentId, courseId);
        System.out.println("Student with id " + studentId + " added to course with id " + courseId);
    }

    private void provideDeleteStudentById(Scanner scanner) {
        System.out.print("Enter id of the student you want to delete: ");
        int studentId = scanner.nextInt();
        controller.deleteById(studentId);
        System.out.println("Student with id " + studentId + " deleted");
    }

    private void provideRemoveStudentFromCourse(Scanner scanner) {
        System.out.print("Enter student id you want ot remove from course: ");
        int studentId = scanner.nextInt();
        System.out.print("Enter course id from which you want to remove student: ");
        int courseId = scanner.nextInt();
        controller.deleteStudentFromOneCourse(studentId, courseId);
        System.out.println("Student with id " + studentId + " removed from course with id " + courseId);
    }

}
