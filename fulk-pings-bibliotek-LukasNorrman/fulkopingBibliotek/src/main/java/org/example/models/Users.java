package org.example.models;

public class Users {

    private int user_id;
    private String username;
    private String email;
    private String password_hash;


    public Users() { }


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return password_hash;
    }

    public void setHashedPassword(String password_hash) {
        this.password_hash = password_hash;

    }


}
