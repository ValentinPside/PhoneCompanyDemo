package ru.avalon.javapp.devj120.avalontelecom.models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Person extends ClientInfo{

    private LocalDate birthDate;
    private int age;
    private String birthDay;

    public Person(PhoneNumber phoneNumber, String name, String address, String birthDay) {
        super(phoneNumber, name, address);
        this.setBirthDate(birthDay);
        this.calculateAge();

    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public void setBirthDate(String birthDate) {
        String[] numbers = birthDate.split("/");

        int day = Integer.parseInt(numbers[0]);

        int month = Integer.parseInt(numbers[1]);

        int year = Integer.parseInt(numbers[2]);

        if (day > 31 || day < 1) {
            throw new IllegalArgumentException("day of the month must be between 1 and 31");
        }
        if (month > 12 || month < 1) {
            throw new IllegalArgumentException("month number myst be between 1 and 12.");
        }
        if (year < 1900 || year > LocalDate.now().getYear()) {
            throw new IllegalArgumentException("year of birth can't be less than 1900, and can't be future.");
        }
        this.birthDate = LocalDate.of(year, month, day);
    }

    public void calculateAge() {
        age = (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());
    }

    public int getAge() {
        return age;
    }

    @Override
    public String getExtraInformation() {
        return "Age: " + String.valueOf(age);
    }
}
