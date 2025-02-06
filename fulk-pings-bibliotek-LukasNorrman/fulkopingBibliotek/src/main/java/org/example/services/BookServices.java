package org.example.services;

import org.example.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookServices {

    // Metod för att få ut alla böcker i biblioteket
    public static void getAllBooks() {
        String sql = "SELECT * FROM Books";

        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);) {
            ResultSet rs = ps.executeQuery();

            // Detta är en variant som Isac har visat för mig, detta får konsolen att se mycket mer behaglig ut.
            System.out.println("\n ALL BOOKS \n");
            System.out.printf(" | %-2s | %-45s | %-20s | %-5s%n " ,"ID", "Title", "Author","Available");

            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                boolean available = rs.getBoolean("available");

                System.out.printf("| %-2s | %-45s | %-20s | %-5s%n ", bookId, title, author, available);
            }
        } catch (SQLException e) {
            Database.SQLExceptionPrint(e,true);
        }
    }

    // Metod för att söka efter böcker med specifik titel
    public static void searchBooksByTitle(String title) {

        String sql = "SELECT * FROM Books WHERE title LIKE ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setString(1, "%" + title + "%");

            System.out.println("\nBOOKS\n");
            System.out.printf(" | %-2s | %-45s | %-20s | %-5s%n " ,"ID", "Title", "Author","Available");


            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    int bookId = rs.getInt("book_id");
                    String title2 = rs.getString("title");
                    String author2 = rs.getString("author");
                    boolean available2 = rs.getBoolean("available");

                    System.out.printf("| %-2s | %-45s | %-20s | %-5s%n ", bookId, title2, author2, available2);
                }
            }
        } catch (SQLException e) {
            Database.SQLExceptionPrint(e,true);
        }


    }

    // Metod för att söka efter böcker med en specifik författare
    public static void searchBooksByAuthor(String author) {
        String sql = "SELECT * FROM Books WHERE author LIKE ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setString(1, "%" + author + "%");
            ResultSet rs = ps.executeQuery();

            // Samma här med konsolen.
            System.out.println("\nBOOKS\n");
            System.out.printf(" | %-2s | %-45s | %-20s | %-5s%n " ,"ID", "Title", "Author","Available");

            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String title2 = rs.getString("title");
                String author2 = rs.getString("author");
                boolean available2 = rs.getBoolean("available");

                System.out.printf("| %-2s | %-45s | %-20s | %-5s%n ", bookId, title2, author2, available2);
            }
        } catch (SQLException e) {
            Database.SQLExceptionPrint(e,true);
        }
    }
}
