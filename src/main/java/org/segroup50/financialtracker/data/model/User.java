package org.segroup50.financialtracker.data.model;

public class User {
    private String id;
    private String username;
    private String email;
    private String phone;
    private String pwd;
    private String totpsecret;

    public User() {
    }

    public User(String id, String username, String email, String phone, String pwd, String totpsecret) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.pwd = pwd;
        this.totpsecret = totpsecret;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getTotpsecret() {
        return totpsecret;
    }

    public void setTotpsecret(String totpsecret) {
        this.totpsecret = totpsecret;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", pwd='" + pwd + '\'' +
                ", totpsecret='" + totpsecret + '\'' +
                '}';
    }
}