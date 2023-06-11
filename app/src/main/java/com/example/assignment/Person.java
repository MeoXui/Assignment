package com.example.assignment;

import java.io.Serializable;

public class Person implements Serializable {
    String id,name,depart;

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public Person(String id, String name, String depart) {
        this.id = id;
        this.name = name;
        this.depart = depart;
    }
}
