package genericclasses;

import java.io.Serializable;

/**
 * Created by mariobalca on 25-10-2015.
 */
public class Extra implements Serializable {
    private int id;
    private double minValue;
    private String name;
    private String description;

    public Extra(double min, String n, String d){
        this.minValue = min;
        this.name = n;
        this.description = d;
    }

    public Extra(){

    }

    public int getId() {
        return id;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Extra Id: " + id + " Name: " + name + " Description: " + description + " Value: " + minValue;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }
}
