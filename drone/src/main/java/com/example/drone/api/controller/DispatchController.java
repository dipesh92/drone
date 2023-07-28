package com.example.drone.api.controller;

import com.example.drone.api.model.Drone;
import com.example.drone.api.model.DroneState;
import com.example.drone.api.model.Medication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@RestController
public class DispatchController {
    private List<Drone> drones;
    private Map<String, Medication> medications;

    public DispatchController() {
        drones = new ArrayList<>();
        medications = new HashMap<>();
        // Initialize the list of drones and medications
        // Example data for drones
        drones.add(new Drone("ABC123", "Middleweight", 400, 80));
        drones.add(new Drone("XYZ456", "Lightweight", 300, 90));

        // Example data for medications
        medications.put("MED001", new Medication("Paracetamol", 10, "MED001", "paracetamol.jpg"));
        medications.put("MED002", new Medication("Aspirin", 8, "MED002", "aspirin.jpg"));
        medications.put("MED003", new Medication("Ibuprofen", 12, "MED003", "ibuprofen.jpg"));

        // Schedule a periodic task to check battery levels
        Timer timer = new Timer();
        timer.schedule(new BatteryCheckTask(), 0, 60000); // Check every 1 minute
    }

    /*@PostMapping("/register")
    public void registerDrone(@RequestBody Drone drone) {
        // Implement logic to register a drone
        //System.out.println("here");
        drones.add(drone);
        System.out.println("Drone " + drone.getSerialNumber() + " registered successfully.");
    }*/
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerDrone(@RequestBody Drone drone)  {
        drones.add(drone);
        String message = "Drone " + drone.getSerialNumber() + " registered successfully.";
        System.out.println(message);

        // Create a JSON response body map
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", message);

        // Return the response with JSON body and HTTP status 200 (OK)
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping("/{serialNumber}/load")
    public ResponseEntity<Map<String, String>> loadDrone(@PathVariable String serialNumber, @RequestBody Medication medication) {
        Drone drone = findDroneBySerialNumber(serialNumber);

        // Check if the drone exists
        if (drone == null) {
            String errorMessage = "Drone with serial number " + serialNumber + " not found.";
            return new ResponseEntity<>(createErrorResponse(errorMessage), HttpStatus.NOT_FOUND);
        }

        // Check if the drone is available for loading
        if (drone.getState() != DroneState.IDLE || !drone.isBatteryLevelAboveThreshold()) {
            String errorMessage = "Drone " + drone.getSerialNumber() + " is not available for loading.";
            return new ResponseEntity<>(createErrorResponse(errorMessage), HttpStatus.BAD_REQUEST);
        }

        // Check if the drone can carry the weight of the medication
        if (!drone.canCarryWeight(medication.getWeight())) {
            String errorMessage = "Drone " + drone.getSerialNumber() + " cannot carry the weight of the medication.";
            return new ResponseEntity<>(createErrorResponse(errorMessage), HttpStatus.BAD_REQUEST);
        }

        // Load the medication to the drone
        drone.setState(DroneState.LOADING);
        drone.loadMedication(medication);
        String successMessage = "Drone " + drone.getSerialNumber() + " loaded with medication " + medication.getCode() + ".";
        System.out.println(successMessage);
        return new ResponseEntity<>(createSuccessResponse(successMessage), HttpStatus.OK);
    }

    // Helper method to create a JSON response for successful operations
    private Map<String, String> createSuccessResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return response;
    }

    // Helper method to create a JSON response for error messages
    private Map<String, String> createErrorResponse(String errorMessage) {
        Map<String, String> response = new HashMap<>();
        response.put("error", errorMessage);
        return response;
    }


    @GetMapping("/{serialNumber}/loaded-medications")
    public ResponseEntity<List<Medication>> getLoadedMedicationsForDrone(@PathVariable String serialNumber) {
        Drone drone = findDroneBySerialNumber(serialNumber);

        // Check if the drone exists
        if (drone == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Check if the drone is in the "LOADED" state
        if (drone.getState() != DroneState.LOADED) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Get the list of loaded medications for the drone
        List<Medication> loadedMedications = drone.getLoadedMedications();

        // Return the list of loaded medications in the response
        return new ResponseEntity<>(loadedMedications, HttpStatus.OK);
    }

    @GetMapping("/available-for-loading")
    public List<Drone> getAvailableDronesForLoading() {
        // Implement logic to get the list of available drones for loading
        List<Drone> availableDrones = new ArrayList<>();
        for (Drone drone : drones) {
            if (drone.getState() == DroneState.IDLE && drone.isBatteryLevelAboveThreshold()) {
                availableDrones.add(drone);
            }
        }
        return availableDrones;
    }

    @GetMapping("/{serialNumber}/battery-level")
    public double getDroneBatteryLevel(@PathVariable String serialNumber) {
        // Implement logic to get the battery level of a given drone
        Drone drone = findDroneBySerialNumber(serialNumber);

        if (drone != null) {
            return drone.getBatteryCapacity();
        }

        return -1; // Return -1 if the drone is not found
    }
    @PostMapping("/register-medication")
    public ResponseEntity<Map<String, String>> registerMedication(@RequestBody Medication medication) {
        // Logic to register the medication, for example, adding it to the medications map
        medications.put(medication.getCode(), medication);

        String successMessage = "Medication " + medication.getCode() + " registered successfully.";
        System.out.println(successMessage);

        // Create a JSON response body map
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", successMessage);

        // Return the response with JSON body and HTTP status 200 (OK)
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    private Drone findDroneBySerialNumber(String serialNumber) {
        for (Drone drone : drones) {
            if (drone.getSerialNumber().equals(serialNumber)) {
                return drone;
            }
        }
        return null;
    }
    private class BatteryCheckTask extends TimerTask {
        @Override
        public void run() {
            for (Drone drone : drones) {
                if (drone.isBatteryLevelAboveThreshold()) {
                    // Log battery level and status change to history/audit event log
                    logBatteryAndStatusChange(drone);
                }
            }
        }

    }
    private void logBatteryAndStatusChange(Drone drone) {
        String logMessage = "Drone " + drone.getSerialNumber() + " - Battery level below 25%. Status changed to " + drone.getState();
        // Implement logic to log the message to a history/audit event log or any desired log system.
        // For example, you can use a logger library like Log4j or log the message to a file or database.
        System.out.println(logMessage);
    }

}
