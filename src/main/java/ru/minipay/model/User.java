package ru.minipay.model;

import java.time.LocalDate;
import java.util.Objects;

public class User {
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate birthday;

    public User(String firstName, String lastName, Gender gender, LocalDate birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                gender == user.gender &&
                Objects.equals(birthday, user.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, gender, birthday);
    }

    @Override
    public String toString() {
        String genderStr = (gender == Gender.MALE) ? "Мужской" : "Женский";
        return firstName + ' ' + lastName + ", Пол: " + genderStr + ", Дата рождения: " + birthday;
    }
}
