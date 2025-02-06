package org.example.services;

import org.example.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class LoanServices {

    // Metod för att låna en bok
    public static boolean borrowBook(int user_id, int book_id ) {

        // Kontrollerar utgångna reservationer
        ReservationServices.handleExpiredReservations();

        // Kallar på metoderna för att få denna metod mer läsbar

        if (!userExists(user_id)) {
            System.out.println("User ID not found");
            return false;
        }
        if (!bookIsAvailable(book_id)) {
            System.out.println("Book is not available");
            return false;
        }
        if (isBookReserved(book_id, user_id)) {
            System.out.println("This book is reserved by another user.");
            return false;
        }
        return executeLoan(user_id, book_id);
    }


    // Metod för att returnera en bok
    public static boolean returnBook( int user_id, int book_id) {
        String returnSql = "UPDATE Loans SET return_date = ? WHERE book_id = ? AND user_id = ?";
        String updateSql = "UPDATE Books SET available = true WHERE book_id = ?";



        try (Connection conn = Database.getConnection();
             PreparedStatement returnPs = conn.prepareStatement(returnSql);
             PreparedStatement updatePs = conn.prepareStatement(updateSql);){

            // Uppdatera returdatum för lånet
            returnPs.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            returnPs.setInt(2, book_id);
            returnPs.setInt(3, user_id);

            if ( returnPs.executeUpdate() > 0)  {
                // Uppdatera bokens tillgänglighetsstatus
                updatePs.setInt(1, book_id);
                return updatePs.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            Database.SQLExceptionPrint(e,true);
        }
        System.out.println("Failed to return book");
        return false;
    }

    // Metod för att visa lånehistorik för en användare
    public static void viewLoanHistory (int userId) {
        String sql =
                "SELECT l.loan_id, l.book_id, l.loan_date, l.return_date, b.title " +
                        "FROM Loans l " +
                        "JOIN Books b ON l.book_id = b.book_id " +
                        "WHERE l.user_id = ? " +
                        "ORDER BY l.loan_date DESC ";

        try (Connection conn = Database.getConnection();
             PreparedStatement loanPs = conn.prepareStatement(sql);){

            // Sätt användarens ID som parametrar
            loanPs.setLong(1, userId);
            ResultSet loanRs = loanPs.executeQuery();

            System.out.println("\nLoan History:\n");
            System.out.printf(" | %-10s | %-45s | %-15s | %-15s%n ", "Loan ID", "Title", "Loan Date", "Return Date");

            boolean hasLoan = false;
            while (loanRs.next()) {
                hasLoan = true;
                int loanId = loanRs.getInt("loan_id");
                String title = loanRs.getString("title");
                String loanDate = loanRs.getDate("loan_date").toString();
                String returnDate = loanRs.getDate("return_date") != null
                        ? loanRs.getDate("return_date").toString() : "Not Returned";

                System.out.printf("| %-10d | %-45s | %-15s | %-15s%n ", loanId, title, loanDate, returnDate);
            }
            if (!hasLoan) {
                System.out.println("No Loan history found");
            }
        } catch (SQLException e) {
            Database.SQLExceptionPrint(e,true);
        }
    }

    // Metod för att visa aktuella lån för en användare
    public static void currentLoanStatus(int user_id) {
        String sql = "SELECT * FROM Loans WHERE user_id = ? AND (return_date IS NULL OR return_date > ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setInt(1, user_id);
            ps.setDate(2, java.sql.Date.valueOf(LocalDate.now()));

            System.out.println("\nActive Loans:\n");
            System.out.printf(" | %-10s | %-10s | %-10s | %-15s | %-15s%n ", "Loan ID", "User ID", "Book ID", "Loan Date", "Return Date");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int loanId = rs.getInt("loan_id");
                    int userId = rs.getInt("user_id");
                    int bookId = rs.getInt("book_id");
                    String loanDate = rs.getDate("loan_date").toLocalDate().toString();
                    String returnDate = rs.getString("return_date") != null
                            ? rs.getDate("return_date").toLocalDate().toString() : "N/A";

                    System.out.printf("| %-10d | %-10s | %-10s | %-15s | %-15s%n ", loanId, userId, bookId, loanDate, returnDate);
                }
            }
        } catch (SQLException e) {
            Database.SQLExceptionPrint(e,true);
        }
    }




    //För mindre upprepning av kod och för läsbarhetens skull

    // Kontrollera om användaren existerar i databasen
    private static boolean userExists(int user_id) {
        String sql = "SELECT user_id FROM Users WHERE user_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user_id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            Database.SQLExceptionPrint(e,true);
        }
        return false;
    }

    // Kontrollera om boken är tillgänglig
    private static boolean bookIsAvailable(int book_id) {
        String sql = "SELECT available FROM Books WHERE book_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, book_id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getBoolean("available");
            }
        } catch (SQLException e) {
            Database.SQLExceptionPrint(e,true);
        }
        return false;
    }

    // Kontrollera om boken är reserverad av en annan användare
    private static boolean isBookReserved(int book_id, int user_id) {
        String sql = "SELECT * FROM Reservations WHERE book_id = ? AND user_id <> ? AND expiry_date >= ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, book_id);
            ps.setInt(2, user_id);
            ps.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            Database.SQLExceptionPrint(e,true);
        }
        return false;
    }

    // Utför lånetransaktionen och uppdaterar bokens tillgänglighet
    private static boolean executeLoan (int user_id, int book_id) {
        String loanSql = "INSERT INTO Loans(user_id, book_id, loan_date, return_date) VALUES (?, ?, ?, ?)";
        String updateSql = "UPDATE Books SET available = false WHERE book_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement loanPs = conn.prepareStatement(loanSql);
             PreparedStatement updatePs = conn.prepareStatement(updateSql)) {

            // Sätt parametrarna för lånetransaktionen
            loanPs.setInt(1, user_id);
            loanPs.setInt(2, book_id);
            loanPs.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            loanPs.setDate(4, java.sql.Date.valueOf(LocalDate.now().plusDays(30)));
            if (loanPs.executeUpdate() > 0) {
                // Uppdatera bokens tillgänglighet
                updatePs.setInt(1, book_id);
                return updatePs.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            Database.SQLExceptionPrint(e,true);
        }
        return false;
    }



}
