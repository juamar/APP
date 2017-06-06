package models;

import java.io.Serializable;

/**
 * Created by JuanIgnacio on 23/05/2017.
 */

public class User implements Serializable {

    private int Id;

    private String name;

    private String lastName;

    private String email;

    private String password;

    private String phoneNumber;

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return Id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getSurname()
    {
            return name.split(" ")[0];
    }

    public void setId(int id) {
        Id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString()
    {
        return Id + ", " + name + " " + lastName + ", " + email + ", " + phoneNumber;
    }

    public User(int id, String name, String lastName, String email, String phoneNumber) {
        Id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    public User() {
    }
}
