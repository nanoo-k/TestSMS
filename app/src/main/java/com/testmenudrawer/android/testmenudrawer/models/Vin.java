package com.testmenudrawer.android.testmenudrawer.models;

/**
 * Created by mvalencia on 10/17/17.
 */


public class Vin {
    public int id;
    public String date;
    public String vin;
    public String year;
    public String make;
    public String model;
    public String engine;

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getVin() {
        return vin;
    }

    public String getYear() {
        return year;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getEngine() {
        return engine;
    }


    public Vin(int id, String date, String vin, String year, String make, String model, String engine) {
        this.id = id;
        this.date = date;
        this.vin = vin;
        this.year = year;
        this.make = make;
        this.model = model;
        this.engine = engine;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }



    @Override
    public String toString() {
        return "Vin{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", vin='" + vin + '\'' +
                ", date='" + year + '\'' +
                ", vin='" + make + '\'' +
                ", date='" + model + '\'' +
                ", vin='" + engine + '\'' +
                '}';
    }
}