package entities;

import java.time.LocalDate;

public class User {    
    private String email;
    private double balance;
    private LocalDate birthDate;

    public User(String email, double balance, LocalDate birthDate) {
        this.email = email;
        this.balance = balance;
        this.birthDate = birthDate;
     }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }     
} 