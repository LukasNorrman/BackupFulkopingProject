package org.example;

import org.example.models.Users;
import org.example.services.BookServices;
import org.example.services.LoanServices;
import org.example.services.ReservationServices;
import org.example.services.UserServices;

import java.util.Scanner;

import static org.example.services.UserServices.*;

public class Menu {
    private static final Scanner scanner = new Scanner(System.in);
    // Meny för att kontrollera användare
    public static void start(){
        boolean running = true;

        while (running) {
            System.out.println("\nMenu:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    Users newUser = UserServices.registerUser();
                    if (newUser != null) {
                        System.out.println("Registration successful! You are now logged in.");
                        userMenu(newUser);
                    } else {
                        System.out.println("Registration failed. Please try again.");
                    }
                    break;
                case 2:
                    Users loggedInUser = UserServices.login();
                    if (loggedInUser != null) {
                        System.out.println("Login successful!");
                        userMenu(loggedInUser);
                    } else {
                        System.out.println("\nInvalid username or password.");
                    }
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option, Please try again.");
            }
        }
    }



    // Menyn för användare
    // Här i menyn kallar jag på metoderna som behövs
    public static void userMenu(Users currentUser){

        boolean running = true;

        while (running) {
            System.out.println("\n------Fulköping Bibliotek-----\n");
            System.out.println("Logged in as: " + currentUser.getUsername() + "\n");
            System.out.println("1. Search Books");
            System.out.println("2. Borrow Books");
            System.out.println("3. Return Books");
            System.out.println("4. View Loan History");
            System.out.println("5. View Current Loan Status");
            System.out.println("6. Reserve Book");
            System.out.println("7. Update Profile");
            System.out.println("8. Logout");
            System.out.print("\nChoose an option: \n ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("\n1. View all Books");
                    System.out.println("2. Search Books by Title");
                    System.out.println("3. Search Books by Author");

                    int choice2 = scanner.nextInt();
                    scanner.nextLine();

                    switch (choice2) {
                        case 1:
                            BookServices.getAllBooks();
                            System.out.println("Press enter to continue...");
                            scanner.nextLine();
                            break;
                        case 2:
                            System.out.println("Select title: ");
                            String title = scanner.nextLine();
                            BookServices.searchBooksByTitle(title);
                            System.out.println("Press enter to continue...");
                            scanner.nextLine();
                            break;
                        case 3:
                            System.out.println("Search for author: ");
                            String author = scanner.nextLine();
                            BookServices.searchBooksByAuthor(author);
                            System.out.println("Press enter to continue...");
                            scanner.nextLine();
                            break;
                        default:
                            System.out.println("Invalid option, Please try again.");
                            break;
                    }
                    break;

                case 2:
                    BookServices.getAllBooks();
                    System.out.println("\n Enter book ID to borrow: ");
                    int bookID = scanner.nextInt();
                    scanner.nextLine();
                    if (LoanServices.borrowBook(currentUser.getUser_id(), bookID)) {
                        System.out.println("Book Borrowed Successfully!");
                    } else {
                        System.out.println("Failed to borrow book!");
                    }
                    System.out.println("\nPress enter to continue...");
                    scanner.nextLine();
                    break;

                case 3:
                    System.out.println("Enter book ID to return: ");
                    int bookId = scanner.nextInt();
                    scanner.nextLine();
                    if (LoanServices.returnBook(currentUser.getUser_id(), bookId)) {
                        System.out.println("Book Returned Successfully!");
                    } else {
                        System.out.println("Failed to Return Book!");
                        }
                    System.out.println("\nPress enter to continue...");
                    scanner.nextLine();
                    break;

                case 4:
                    LoanServices.viewLoanHistory(currentUser.getUser_id());
                    System.out.println("\nPress enter to continue...");
                    scanner.nextLine();
                    break;

                case 5:
                    LoanServices.currentLoanStatus(currentUser.getUser_id());
                    System.out.println("\nPress enter to continue...");
                    scanner.nextLine();
                    break;

                case 6:
                    System.out.println("Enter book ID to reserve: ");
                    int reserveID = scanner.nextInt();
                    scanner.nextLine();
                    if (ReservationServices.reserveBook(currentUser.getUser_id(), reserveID)) {
                        System.out.println("Book Reserved Successfully!");
                    } else {
                        System.out.println("Failed to reserve book!");
                    }
                    System.out.println("\nPress enter to continue...");
                    scanner.nextLine();
                    break;

                case 7:
                    System.out.println("Enter new Username: ");
                    String newUsername = scanner.nextLine();
                    System.out.println("Enter new Password: ");
                    String newPassword = scanner.nextLine();
                    System.out.println("Enter new Email: ");
                    String newEmail = scanner.nextLine();

                    if (UserServices.updateUser(currentUser.getUser_id(), newUsername, newPassword, newEmail)) {
                        System.out.println("User Updated Successfully!");
                    } else {
                        System.out.println("Failed to Update User!");
                    }
                    break;

                case 8:
                    System.out.println("Logging out...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option, Please try again.");
                    break;
            }
        }
    }

}
