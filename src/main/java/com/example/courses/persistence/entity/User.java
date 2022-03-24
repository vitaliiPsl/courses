package com.example.courses.persistence.entity;

import com.example.courses.service.AwsImageService;

import java.util.Objects;

public class User {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean isBlocked;
    private String imageName = AwsImageService.DEFAULT_IMAGE;
    private Role role;

    public User(){}

    public User(long id, String firstName, String lastName, String email, String password, boolean isBlocked, String imageName, Role role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isBlocked = isBlocked;
        this.imageName = imageName;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFullName(){
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isBlocked=" + isBlocked +
                ", imageName='" + imageName + '\'' +
                ", role=" + role +
                '}';
    }

    public static class Builder{
        long id;
        String firstName;
        String lastName;
        String email;
        String password;
        boolean isBlocked;
        String imageName = AwsImageService.DEFAULT_IMAGE;
        Role role;

        public Builder setId(long id){
            this.id = id;
            return this;
        }

        public Builder setFirstName(String firstName){
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName){
            this.lastName = lastName;
            return this;
        }

        public Builder setEmail(String email){
            this.email = email;
            return this;
        }

        public Builder setPassword(String password){
            this.password = password;
            return this;
        }

        public Builder setBlocked(boolean isBlocked){
            this.isBlocked = isBlocked;
            return this;
        }

        public Builder setImageName(String imageName){
            this.imageName = imageName;
            return this;
        }

        public Builder setRole(Role role){
            this.role = role;
            return this;
        }

        public User build(){
            return new User(id, firstName, lastName, email, password, isBlocked, imageName, role);
        }
    }
}
