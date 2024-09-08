package main.java.ui;

import main.java.business.Student;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class ConsoleUI {
    private Connection connection;
    public static final String RESET = "\033[0m";
    public static final String RED = "\033[0;31m";
    public static final String BLUE = "\033[0;34m";
    public static final String MAGENTA = "\033[0;35m";
    public static final String CYAN = "\033[0;36m";
    public static final String PINK = "\033[38;5;13m";
    public static final String GREEN = "\u001b[92m";
    Scanner scanner = new Scanner(System.in);

    public enum UserRole {
        STUDENT, PROFESSOR, ADMIN
    }

    public ConsoleUI(Connection connection) throws SQLException {
        this.connection = connection;
        menu();
    }

    public void menu() throws SQLException {
        boolean running = true;

        while (running) {
            System.out.println(MAGENTA + "+++++++++++++" + RESET + " Pick Your Role " + MAGENTA + "+++++++++++++" + RESET);
            System.out.println(MAGENTA + "+ 1. " + RESET + "I am a student                      " + MAGENTA + "+" + RESET);
            System.out.println(MAGENTA + "+ 2. " + RESET + "I am a professor                    " + MAGENTA + "+" + RESET);
            System.out.println(MAGENTA + "+ 3. " + RESET + "Quit                                " + MAGENTA + "+" + RESET);
            System.out.println(MAGENTA + "++++++++++++++++++++++++++++++++++++++++++" + RESET);

            System.out.print(PINK + "Please select an option (1-3): " + RESET);

            while (!scanner.hasNextInt()) {
                System.out.println(RED + "+ Invalid input." + RESET + " Please enter a valid number: ");
                scanner.next();
            }
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println(MAGENTA + "+" + RESET + " Please enter your email:");
                    String email = scanner.nextLine();

                    Student student = new Student(connection);
                    if (student.studentExists(email)) {
                        System.out.println((BLUE + "+ Welcome back, " + RESET + student.getName() + BLUE + "!" + RESET));
                        studentMenu(scanner);
                    } else {
                        running = false;
                    }
                    break;
                case 2:
                    break;
                case 3:
                    System.out.println(BLUE + "See you soon..." + RESET);
                    running = false;
                    break;
                case 2020:
                    adminMenu(scanner);
                    break;
                default:
                    System.out.println(RED + "+ Error: Invalid choice. Please select a number between 1 and 3 +" + RESET);
            }
        }

        scanner.close();
    }

    public static void adminMenu(Scanner scanner) {
        System.out.println(BLUE + "++++++++++" + RESET + " Admin Menu " + BLUE + "++++++++++" + RESET);
        System.out.println(BLUE + "+ 1. " + RESET + "Manage Documents          " + BLUE + "+" + RESET);
        System.out.println(BLUE + "+ 2. " + RESET + "Manage Users              " + BLUE + "+" + RESET);
        System.out.println(BLUE + "+ 3. " + RESET + "Statistics                " + BLUE + "+" + RESET);
        System.out.println(BLUE + "+ 4. " + RESET + "Quit                      " + BLUE + "+" + RESET);
        System.out.println(BLUE + "++++++++++++++++++++++++++++++++" + RESET);

        System.out.print(BLUE + "+" + RESET + " Please select an option (1-4): ");
        int adminChoice = scanner.nextInt();
        scanner.nextLine();

        switch (adminChoice) {
            case 1:
                handleMiniMenu(scanner, UserRole.ADMIN);
                break;
            case 2:
                handleMiniMenu(scanner, UserRole.ADMIN);
                break;
            case 3:
                handleMiniMenu(scanner, UserRole.ADMIN);
                break;
            case 4:
                System.out.println(BLUE + "See you soon..." + RESET);
                System.exit(0);
                break;
            default:
                System.out.println(RED + "+ Error: Invalid choice. Please select a number between 1 and 4 +" + RESET);
        }
    }


    public static void studentMenu(Scanner scanner) {
        System.out.println(BLUE + "+++++++++++++" + RESET + " Student Menu " + BLUE + "+++++++++++++" + RESET);
        System.out.println(BLUE + "+ 1. " + RESET + "View Documents                     " + BLUE + "+" + RESET);
        System.out.println(BLUE + "+ 2. " + RESET + "Search Documents                   " + BLUE + "+" + RESET);
        System.out.println(BLUE + "+ 3. " + RESET + "View Profile                       " + BLUE + "+" + RESET);
        System.out.println(BLUE + "+ 4. " + RESET + "Edit Profile                       " + BLUE + "+" + RESET);
        System.out.println(BLUE + "+ 5. " + RESET + "Log Out                            " + BLUE + "+" + RESET);
        System.out.println(BLUE + "+++++++++++++++++++++++++++++++++++++++++" + RESET);

        System.out.print(CYAN + "Please select an option (1-5): " + RESET);

        switch (scanner.nextInt()) {
            case 1:
                handleMiniMenu(scanner, UserRole.STUDENT);
                break;
            case 2:
                handleMiniMenu(scanner, UserRole.STUDENT);
                break;
            case 3:
                handleMiniMenu(scanner, UserRole.STUDENT);
                break;
            case 4:
                handleMiniMenu(scanner, UserRole.STUDENT);
                break;
            case 5:
                System.out.println(BLUE + "See you soon..." + RESET);
                System.exit(0);
                break;
            default:
                System.out.println(RED + "+ Error: Invalid choice. Please select a number between 1 and 5 +" + RESET);
        }
    }

    private static void handleMiniMenu(Scanner scanner, UserRole role) {
        boolean backToMainMenu = true;

        while (backToMainMenu) {
            System.out.println(MAGENTA + "++++++++++++++++++++++++++++" + RESET);
            System.out.println(MAGENTA + "+ 1. " + RESET + "Back to Main Menu     " + MAGENTA + "+" + RESET);
            System.out.println(MAGENTA + "+ 2. " + RESET + "Exit                  " + MAGENTA + "+" + RESET);
            System.out.println(MAGENTA + "++++++++++++++++++++++++++++" + RESET);

            System.out.print(PINK+"Please select an option (1-2): "+RESET);
            int miniChoice = scanner.nextInt();
            scanner.nextLine();

            switch (miniChoice) {
                case 1:
                    backToMainMenu = false;
                    if (role == UserRole.STUDENT) {
                        ConsoleUI.studentMenu(scanner);
                    } else if (role == UserRole.ADMIN) {

                        ConsoleUI.adminMenu(scanner);
                    } else if (role == UserRole.PROFESSOR) {

                    }
                    break;
                case 2:
                    System.out.println(BLUE + "See you soon..." + RESET);
                    System.exit(0);
                    break;
                default:
                    System.out.println(RED + "+ Error: Invalid choice. Please select 1 or 2 +" + RESET);
            }
        }
    }
}
