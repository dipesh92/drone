----------------------------------------------
Download the source code.
Open the project in your preferred IDE (e.g., IntelliJ, Eclipse).
Build and run the application as a Spring Boot application.
The API will be accessible at http://localhost:8080.

--------------------------------------------------
Example Usage
To register a new drone:
Send a POST request to http://localhost:8080/drones/register with the drone details in the request body.
Body: JSON representing the drone details
{
  "serialNumber": "ABC123",
  "model": "Middleweight",
  "weightLimit": 400,
  "batteryCapacity": 80
}

To load a medication onto a drone:
Send a POST request to http://localhost:8080/drones/{serialNumber}/load with the serial number of the drone in the path variable and the medication details in the request body.
{serialNumber}=ABC123/XYZ456
Body: JSON representing the drone details
{
  "name": "Paracetamol",
  "weight": 10,
  "code": "MED001",
  "image": "paracetamol.jpg"

To get the list of loaded medications for a drone:
Send a GET request to http://localhost:8080/drones/{serialNumber}/loaded-medications with the serial number of the drone in the path variable.

To get the list of available drones for loading:
Send a GET request to http://localhost:8080/drones/available-for-loading.

To get the battery level of a drone:
Send a GET request to http://localhost:8080/drones/{serialNumber}/battery-level with the serial number of the drone in the path variable.

-------------------------------------