package org.example.models;

import java.time.LocalDate;

public class Loans {

    private int loan_id;
    private int user_id;
    private int book_id;
    private LocalDate loan_date;
    private LocalDate return_date;

    public Loans() { }


    public int getLoan_id() {
        return loan_id;
    }
    public int getUser_id() {
        return user_id;
    }
    public int getBook_id() {
        return book_id;
    }
    public LocalDate getLoan_date() {
        return loan_date;
    }
    public LocalDate getReturn_date() {
        return return_date;
    }


}
