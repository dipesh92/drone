package com.example.drone.api.model;

public class Medication {
    private String name;
    private double weight;
    private String code;
    private String image;

    public Medication(String name, double weight, String code,String image){
        this.name=name;
        this.weight=weight;
        this.code= code;
        this.image =image;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public double getWeight() {
        return weight;
    }
    public void setWeight(){
        this.weight=weight;
    }
    public String  getCode() {
        return code;
    }
    public void setCode(){
        this.code=code;
    }
    public String  getImage() {
        return image;
    }
    public void setImage(){
        this.image=image;
    }
}
