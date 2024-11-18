package com.example.tp7;

public class ServerResponse {
    private String status;
    private String message;
    private  User user;

    public String getStatus(){
        return status;
    }

    public String GetMessage(){
        return  message;
    }

    public User getUser() {
        return user;
    }
}
