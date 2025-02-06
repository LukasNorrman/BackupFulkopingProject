package org.example.models;

import java.time.LocalDate;

public class Reservations {

    private int reservation_ID;
    private int user_id;
    private int book_id;
    private LocalDate reservation_date;
    private LocalDate expiry_date;

    public Reservations() { }

    public int getReservation_ID() {
        return reservation_ID;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getBook_id() {
        return book_id;
    }

    public LocalDate getReservation_date() {
        return reservation_date;
    }

    public LocalDate getExpiry_date() {
        return expiry_date;
    }

}
