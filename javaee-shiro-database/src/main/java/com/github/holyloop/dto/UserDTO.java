package com.github.holyloop.dto;

public class UserDTO {

    private String username;
    private String password;

    public UserDTO() {
        super();
    }

    @Override
    public String toString() {
        return "UserDTO [username=" + username + ", password=" + password + "]";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
