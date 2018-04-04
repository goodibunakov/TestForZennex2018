package ru.goodibunakov.testforzennex2018.model;

public class Person {

    private int id;
    private String name;
    private int checkBox;
    //private String image;

    public Person() {
    }

    public Person(String name, int checkBox) {
        this.name = name;
        this.checkBox = checkBox;
        //this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(int checkBox) {
        this.checkBox = checkBox;
    }

}
