package ru.minipay.minipay.model;

public enum Gender {
    MALE(0), FEMALE(1);

    private final int id;

    Gender(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Gender getById(int id) {
        if(id == 0) return Gender.MALE;
        if(id == 1) return Gender.FEMALE;
        return null;
    }
}
