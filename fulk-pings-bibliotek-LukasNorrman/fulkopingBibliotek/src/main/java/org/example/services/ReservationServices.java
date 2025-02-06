package org.example.services;

import org.example.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ReservationServices {

    // Metod för att reservera en lånad bok
    public static boolean reserveBook( int user_id, int book_id) {

        String checkReservationSql = "SELECT * FROM Reservations WHERE book_id = ? AND expiry_date >= ?";
        String insertReservationSql = "INSERT INTO Reservations (user_id, book_id, reservation_date, expiry_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement checkReservationPs = conn.prepareStatement(checkReservationSql);
             PreparedStatement insertReservationPs = conn.prepareStatement(insertReservationSql);) {

            // Kontrollerar om boken redan är reserverad
            checkReservationPs.setInt(1, book_id);
            checkReservationPs.setDate(2, java.sql.Date.valueOf(LocalDate.now()));

            try (ResultSet rs = checkReservationPs.executeQuery()) {
                if (rs.next()) {
                    System.out.println("This book is already reserved by another user.");
                    return false;
                }
            }
            // Lägger till reservation
            insertReservationPs.setInt(1, user_id);
            insertReservationPs.setInt(2, book_id);
            insertReservationPs.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            insertReservationPs.setDate(4, java.sql.Date.valueOf(LocalDate.now().plusDays(30)));

            return insertReservationPs.executeUpdate() > 0;

        } catch (SQLException e) {
            Database.SQLExceptionPrint(e,true);
        }
        return false;
    }

    // Metod för att ta bort utgångna reservationer
    public static void handleExpiredReservations() {
        String sql = "DELETE FROM Reservations WHERE expiry_date < ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Sätter dagens datum som gräns
            ps.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            ps.executeUpdate();

        } catch (SQLException e) {
            Database.SQLExceptionPrint(e,true);
        }
    }
}
