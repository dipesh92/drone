package com.example.drone.api.model;

import java.util.ArrayList;
import java.util.List;

public class Drone {
    private String serialNumber;
    private String model;
    private double weightLimit;
    private double batteryCapacity;
    private DroneState state;
    private List<Medication> loadedMedications;
    private  double currentWeight;

    public Drone(String serialNumber, String model, double weightLimit, double batteryCapacity){
        this.serialNumber= serialNumber;
        this.model= model;
        this.weightLimit= weightLimit;
        this.batteryCapacity = batteryCapacity;
        this.state = DroneState.IDLE;
        this.loadedMedications=new ArrayList<>();
        this.currentWeight=0.0;
    }

    public String getSerialNumber(){
        return serialNumber;
    }
    public void  setSerialNumber(String serialNumber){
        this.serialNumber=serialNumber;
    }
    public String getModel(){
        return model;
    }
    public void setModel(){
        this.model=model;
    }
    public double getWeightLimit(){
        return weightLimit;
    }
    public void setWeightLimit(){
        this.weightLimit=weightLimit;
    }
    public double getBatteryCapacity(){
        return batteryCapacity;
    }
    public void setBatteryCapacity(){
        this.batteryCapacity=batteryCapacity;
    }
    public DroneState getState() {
        return state;
    }

    public void setState(DroneState state) {
        this.state = state;
    }

    public List<Medication> getLoadedMedications() {
        return loadedMedications;
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
    }

    public boolean canCarryWeight(double weight) {
        return (currentWeight + weight) <= weightLimit;
    }

    public boolean isBatteryLevelAboveThreshold() {
        return batteryCapacity >= 25;

    }

    public void loadMedication(Medication medication) {
        if (state.equals(DroneState.LOADING) && canCarryWeight(medication.getWeight())) {
            loadedMedications.add(medication);
            currentWeight += medication.getWeight();
        }
    }

}
