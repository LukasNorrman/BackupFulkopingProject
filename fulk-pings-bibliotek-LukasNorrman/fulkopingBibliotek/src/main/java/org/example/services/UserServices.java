package org.example.services;

import org.example.database.Database;

import org.example.models.Users;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UserServices {
    private static Users currentUser; // Ny statiskt variabel

    // Login metod för att skapa kontrollera användare med användarnamn och lösenord. -- Ändrade returtyp till Users
    public static Users login() {

        String sql = "SELECT * FROM Users WHERE username = ?";

        Scanner scanner = new Scanner(System.in);

        // Inmatning av inloggningsuppgifter
        System.out.println("Enter username: ");
        String username = scanner.nextLine();
        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        try(Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()){

                // Om användare finns, validera lösenord
                if (rs.next()) {
                    String storedHashedPassword = rs.getString("password_hash");

                    // Kontrollera att det inskrivna lösenordet matchar det lagrade hashet
                    if (validatePassword(password, storedHashedPassword)) {
                        // Skapar Users-objekt med data från databasen
                        Users user = new Users();
                        user.setUser_id(rs.getInt("user_id"));
                        user.setUsername(rs.getString("username"));
                        user.setEmail(rs.getString("email"));
                        user.setHashedPassword(storedHashedPassword);
                        currentUser = user; // Sparar den inloggade användaren
                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            Database.SQLExceptionPrint(e,true);
        }
        return null; // Returnera null om inloggningen misslyckas
    }
    // Metod för att registrera en ny användare -- Ändrade returtyp till Users
    public static Users registerUser() {
        Scanner scanner = new Scanner(System.in);

        // Inmatning av registreringsuppgifter
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.println("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();


        // Hasha användarens lösenord för säker lagring
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String sql = "INSERT INTO Users (username, email, password_hash) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Sätt användarens data i SQL-frågan
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, hashedPassword);

            if (ps.executeUpdate() > 0) {
                // hämtar den nyligen skapade datan från databasen
                System.out.println("Great! Now login to enter!");
                // efter användaren har registrerat sig, behöver hen logga in för att komma vidare
                return login();
            }
        } catch (SQLException e) {
            Database.SQLExceptionPrint(e,true);
        }
        return null; // Returnera null om registreringen misslyckas
    }

    // Metod för att validera lösenord genom att jämföra input och lagrat hash
    public static boolean validatePassword(String inputPassword, String storedHashedPassword) {
        return BCrypt.checkpw(inputPassword, storedHashedPassword);
    }
    // Metod för att uppdatera användarinformation
    public static boolean updateUser(int userId, String newUsername, String newEmail, String newPassword) {
        String sql = "UPDATE Users SET username = ?, email = ?, password_hash = ? WHERE user_id = ?";

        try (Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

            // Hasha det nya lösenordet om det anges, annars lämna det null
            String hashedPassword = newPassword != null && !newPassword.isEmpty()
                    ? BCrypt.hashpw(newPassword, BCrypt.gensalt())
                    : null;

            // Sätt parametrar för uppdateringen
            ps.setString(1, newUsername);
            ps.setString(2, newEmail);
            ps.setString(3, hashedPassword);
            ps.setInt(4, userId);

            // Kör, returnera true om den lyckas
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Database.SQLExceptionPrint(e,true);
        }
        return false; // Returnera false om uppdateringen misslyckas
    }

}
