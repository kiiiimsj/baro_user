package com.example.wantchu.HelperDatabase;

public class RegisterUser {
    private String phone;
    private String email;
    private String nick;
    private String pass;

    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getNick() { return nick; }
    public String getPass() { return pass; }

    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setNick(String nick) { this.nick = nick; }
    public void setPass(String pass) { this.pass = pass; }

    public RegisterUser(String phone, String email, String nick, String pass) {
        this.phone = phone;
        this.email = email;
        this.nick = nick;
        this.pass = pass;
    }

    public RegisterUser() {}
}
